package com.kdars.HotCheetos.MapReduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocInfo;
import com.kdars.HotCheetos.DocumentStructure.DocPairLocation;
import com.kdars.HotCheetos.DocumentStructure.ObjectFileConverter;

public class SimScoreSentenceMapper1  extends Mapper<LongWritable, MapWritable, IntWritable, MapWritable>{
	
	private double sentenceSimScoreLimit = 0.9;
	
	@Override
	public void map(LongWritable docID, MapWritable sentenceMap, Context context) throws IOException, InterruptedException {
		
		String intermediateOUTPUT_PATH1 = Configurations.getInstance().getIntermediateOUTPUT_PATH1();

		int tableID = Configurations.getInstance().getTableID();
		
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Configuration conf = new Configuration();
		conf.addResource(new Path("/HADOOP_HOME/conf/core-site.xml"));
		conf.addResource(new Path("/HADOOP_HOME/conf/hdfs-site.xml"));
		
		FileSystem fs = FileSystem.get(conf);
		
		Path workingDir = fs.getWorkingDirectory();

		Path newFolderPath1 = new Path("/" + Configurations.getInstance().getDocInfoPathString());

		newFolderPath1 = Path.mergePaths(workingDir, newFolderPath1);
		
		Path newFilePath1 = new Path(newFolderPath1+"/" + String.valueOf((int)docID.get()) + ".dat");
		
		Path newFolderPath2 = new Path("/" + Configurations.getInstance().getDocPairPathString());
		
		newFolderPath2 = Path.mergePaths(workingDir, newFolderPath2);
		
		// if the directory does not exist, then create it
		if (!fs.exists(newFolderPath2)) {
			fs.mkdirs(newFolderPath2); // Create new Directory
		}
		
		Path directory = new Path(intermediateOUTPUT_PATH1);
		FileStatus[] fss = fs.listStatus(directory);
		SequenceFile.Reader reader = null;
		try {
			for (FileStatus status : fss) {
				Path path = status.getPath();
				if(!path.toString().contains("SUCCESS")){
					reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
					LongWritable key = ReflectionUtils.newInstance(LongWritable.class,
							conf);
					MapWritable value = ReflectionUtils.newInstance(MapWritable.class,
							conf);
					
					while (reader.next(key, value)) {
						if (key.compareTo(docID) < 0) {
							
							FSDataInputStream fsInStream1 = fs.open(newFilePath1);
							
							DocInfo docInfo1 = new ObjectFileConverter<DocInfo>().file2Object(fsInStream1);
							
							fsInStream1.close();
							
							Path newFilePath2 = new Path(newFolderPath1+"/" + String.valueOf((int)key.get()) + ".dat");
							
							FSDataInputStream fsInStream2 = fs.open(newFilePath2);
							
							DocInfo docInfo2 = new ObjectFileConverter<DocInfo>().file2Object(fsInStream2);
							
							fsInStream2.close();
							
							csvContent.append(simScore_Calculation_OneVSInputCorpus(key, value, docInfo2, docID, sentenceMap, docInfo1));
							
							DocPairLocation docPair = new DocPairLocation();
							docPair.leftDoc = docInfo2;
							docPair.rightDoc = docInfo1;
							
							Path newFilePath3 =new Path(newFolderPath2+"/" + String.valueOf((int)docID.get()) + "_" + String.valueOf((int)key.get()) + ".dat");
							
							// if the file exists, delete then create it
							if(fs.exists(newFilePath3)){
								fs.delete(newFilePath3, true);
							}
							fs.createNewFile(newFilePath3);

							FSDataOutputStream fsOutStream3 = fs.create(newFilePath3);
							
							if(!new ObjectFileConverter<DocPairLocation>().object2File(docPair, fsOutStream3)){
								System.out.println("Failed to convert object to file!");
							}
							
							fsOutStream3.close();
							
							bulkInsertLimitChecker++;
							if (bulkInsertLimitChecker == bulkInsertLimit) {
								DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), tableID);
								bulkInsertLimitChecker = 0;
								csvContent = new StringBuilder();
							}
						}
					}
				}
			}
		} finally {
		IOUtils.closeStream(reader);
		}
		
		DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), tableID);
		
		return;
	}
	
	private String simScore_Calculation_OneVSInputCorpus(LongWritable docID1, MapWritable sentenceMap1,DocInfo docInfo1, LongWritable docID2, MapWritable sentenceMap2, DocInfo docInfo2){
		int docid1 = (int) docID1.get();
		int docid2 = (int) docID2.get();
		double[] simscores = calcSim_Sentence(sentenceMap1, docInfo1, sentenceMap2, docInfo2);
		return docid1 + "," + docid2 + "," + simscores[0] + "\n" + docid2 + "," + docid1 + "," + simscores[1] + "\n";
	}
	
	private double[] calcSim_Sentence(MapWritable sentenceMap1, DocInfo docInfo1, MapWritable sentenceMap2, DocInfo docInfo2) {  //Calculating similarity between two documents.  Outputs two scores. doc1 to doc2 score & doc2 to doc1 score.
		
		double[] doc1doc2SimScores = new double[2];
		
		ArrayList<Integer> intersectingSentencesFromDoc1 = new ArrayList<Integer>();
		ArrayList<Integer> intersectingSentencesFromDoc2 = new ArrayList<Integer>();
				
		for (Map.Entry<Writable, Writable> entry1 : sentenceMap1.entrySet()){
			int sentenceID1 = (int) ((LongWritable) entry1.getKey()).get();
			MapWritable termFreqMap1 = (MapWritable) entry1.getValue();
			
			for(Map.Entry<Writable, Writable> entry2 : sentenceMap2.entrySet()){
				int sentenceID2 = (int) ((LongWritable) entry2.getKey()).get();
				
				if(calcSim(termFreqMap1, (MapWritable) entry2.getValue()) >= sentenceSimScoreLimit){
					
					if(!intersectingSentencesFromDoc1.contains(sentenceID1)){
						intersectingSentencesFromDoc1.add(sentenceID1);
					}
					
					if(!intersectingSentencesFromDoc2.contains(sentenceID2)){
						intersectingSentencesFromDoc2.add(sentenceID2);
					}
					
					docInfo1.sentenceMap.get(sentenceID1).matchLine.add(sentenceID2);
					docInfo2.sentenceMap.get(sentenceID2).matchLine.add(sentenceID1);

				}
			}
		}
		
		Double doc1Score = (double)intersectingSentencesFromDoc1.size() / (double)sentenceMap1.size();
		if(doc1Score.isNaN()){
			doc1Score = 0d;
		}
		
		Double doc2Score = (double)intersectingSentencesFromDoc2.size() / (double)sentenceMap2.size();
		if(doc2Score.isNaN()){
			doc2Score = 0d;
		}
		
		doc1doc2SimScores[0] = doc1Score;
		doc1doc2SimScores[1] = doc2Score;
		
		return doc1doc2SimScores;
	}
	
	private double calcSim(MapWritable termFreqMap1, MapWritable termFreqMap2){  //Calculating similarity between two sentences. (a sentence from doc1 vs. a sentence from doc2)
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		for(Map.Entry<Writable, Writable> entry : termFreqMap1.entrySet()){
			Text term1 = (Text) entry.getKey();
			double value1 = Double.valueOf(entry.getValue().toString());
			
			if(termFreqMap2.containsKey(term1)){
				double value2 = Double.valueOf(termFreqMap2.get(term1).toString());
				multiply += value1 * value2;
				norm2 += value2 * value2;
				termFreqMap2.remove(term1);
			}
			norm1 += value1 * value1;
		}
		
		for(Map.Entry<Writable, Writable> entry : termFreqMap2.entrySet()){
			double value2 = Double.valueOf(entry.getValue().toString());
			norm2 += (value2 * value2);
		}
		
		double result =  multiply / Math.sqrt(norm1 * norm2);
		if(Double.isNaN(result)){
			result=0;
		}
		
		return result;
	}
}
