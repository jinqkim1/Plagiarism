package com.kdars.HotCheetos.MapReduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.BytesWritable;
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

public class SimScoreSentenceMapper1  extends Mapper<LongWritable, BytesWritable, IntWritable, MapWritable>{
	
	private double sentenceSimScoreLimit = 0.9;
	
	@Override
	public void map(LongWritable docID, BytesWritable sentenceMap, Context context) throws IOException, InterruptedException {
		
		HashMap<Integer, HashMap<String, Integer>> sentenceMap2 = bytesWritableToHashMap(sentenceMap);
		
		String intermediateOUTPUT_PATH1 = Configurations.getInstance().getIntermediateOUTPUT_PATH1();

		int tableID = Configurations.getInstance().getTableID();
		
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Configuration conf = new Configuration();
		conf.addResource(new Path(Configurations.getInstance().getCoreSiteXmlLocation()));
		conf.addResource(new Path(Configurations.getInstance().getHdfsSiteXmlLocation()));
		
		FileSystem fs = FileSystem.get(conf);
		
		Path newFolderPath1 = new Path(Configurations.getInstance().getDocInfoPathString());

		Path newFilePath1 = new Path(newFolderPath1+"/" + String.valueOf((int)docID.get()) + ".dat");
		
		Path newFolderPath2 = new Path(Configurations.getInstance().getDocPairPathString());
		
		FSDataInputStream fsInStream1 = fs.open(newFilePath1);
		
		DocInfo docInfo2 = new ObjectFileConverter<DocInfo>().file2Object(fsInStream1);
		
		fsInStream1.close();
		
		// if the directory does not exist, then create it
		if (!fs.exists(newFolderPath2)) {
			fs.mkdirs(newFolderPath2); // Create new Directory
			fs.setPermission(newFolderPath2, new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL));
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
					BytesWritable value = ReflectionUtils.newInstance(BytesWritable.class,
							conf);
					
					while (reader.next(key, value)) {
						if (key.compareTo(docID) < 0) {
							
							HashMap<Integer, HashMap<String, Integer>> sentenceMap1 = bytesWritableToHashMap(value);
							
							Path newFilePath2 = new Path(newFolderPath1+"/" + String.valueOf((int)key.get()) + ".dat");
							
							FSDataInputStream fsInStream2 = fs.open(newFilePath2);
							
							DocInfo docInfo1 = new ObjectFileConverter<DocInfo>().file2Object(fsInStream2);
							
							fsInStream2.close();
							
							csvContent.append(simScore_Calculation_OneVSInputCorpus(key, sentenceMap1, docInfo1, docID, sentenceMap2, docInfo2));
							
							DocPairLocation docPair = new DocPairLocation();
							docPair.leftDoc = docInfo1;
							docPair.rightDoc = docInfo2;
							
							Path newFilePath3 =new Path(newFolderPath2+"/" + String.valueOf((int)docID.get()) + "_" + String.valueOf((int)key.get()) + ".dat");
							
							// if the file exists, delete then create it
							if(fs.exists(newFilePath3)){
								fs.delete(newFilePath3, true);
							}
							fs.createNewFile(newFilePath3);
							fs.setPermission(newFilePath3, new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL));
							
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
	
	private HashMap<Integer, HashMap<String, Integer>> bytesWritableToHashMap(BytesWritable sentenceMap) {
		try {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(sentenceMap.getBytes());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			@SuppressWarnings("unchecked")
			HashMap<Integer, HashMap<String, Integer>> sentenceMap2 = (HashMap<Integer, HashMap<String, Integer>>) in.readObject();
			byteIn.close();
			in.close();
			return sentenceMap2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String simScore_Calculation_OneVSInputCorpus(LongWritable docID1, HashMap<Integer, HashMap<String, Integer>> sentenceMap1,DocInfo docInfo1, LongWritable docID2, HashMap<Integer, HashMap<String, Integer>> sentenceMap2, DocInfo docInfo2){
		int docid1 = (int) docID1.get();
		int docid2 = (int) docID2.get();
		double[] simscores = calcSim_Sentence(sentenceMap1, docInfo1, sentenceMap2, docInfo2);
		
		//Saving whichever score is bigger.  Not BOTH!
		if(simscores[0] >= simscores[1]){
			return docid1 + "," + docid2 + "," + simscores[0] + "\n";
		}else{
			return docid2 + "," + docid1 + "," + simscores[1] + "\n";
		}
		
//		return docid1 + "," + docid2 + "," + simscores[0] + "\n" + docid2 + "," + docid1 + "," + simscores[1] + "\n";
	}
	
	private double[] calcSim_Sentence(HashMap<Integer, HashMap<String, Integer>> sentenceMap1, DocInfo docInfo1, HashMap<Integer, HashMap<String, Integer>> sentenceMap2, DocInfo docInfo2) {  //Calculating similarity between two documents.  Outputs two scores. doc1 to doc2 score & doc2 to doc1 score.
		
		double[] doc1doc2SimScores = new double[2];
		
		double mapSize1 = sentenceMap1.size();
		double mapSize2 = sentenceMap2.size();
		
		ArrayList<Integer> intersectingSentencesFromDoc1 = new ArrayList<Integer>();
		ArrayList<Integer> intersectingSentencesFromDoc2 = new ArrayList<Integer>();
				
		for (Map.Entry<Integer, HashMap<String, Integer>> entry1 : sentenceMap1.entrySet()){
			int sentenceID1 = entry1.getKey();
			HashMap<String, Integer> termFreqMap1 = entry1.getValue();
			
			for(Map.Entry<Integer, HashMap<String, Integer>> entry2 : sentenceMap2.entrySet()){
				int sentenceID2 = entry2.getKey();
				
				if(calcSim(termFreqMap1, entry2.getValue()) >= sentenceSimScoreLimit){
					
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
		
		Double doc1Score = (double)intersectingSentencesFromDoc1.size() / mapSize1;
		if(doc1Score.isNaN()){
			doc1Score = 0d;
		}
		
		Double doc2Score = (double)intersectingSentencesFromDoc2.size() / mapSize2;
		if(doc2Score.isNaN()){
			doc2Score = 0d;
		}
		
		doc1doc2SimScores[0] = doc1Score;
		doc1doc2SimScores[1] = doc2Score;
		
		return doc1doc2SimScores;
	}
	
	private double calcSim(HashMap<String, Integer> termFreqMap1, HashMap<String, Integer> termFreqMap2){  //Calculating similarity between two sentences. (a sentence from doc1 vs. a sentence from doc2)
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		HashMap<String, Integer> termFreqMap22 = new HashMap<String, Integer>(termFreqMap2);
		
		for(Map.Entry<String, Integer> entry : termFreqMap1.entrySet()){
			String term1 = entry.getKey();
			double value1 = entry.getValue();
			
			if(termFreqMap22.containsKey(term1)){
				double value2 = Double.valueOf(termFreqMap22.get(term1).toString());
				multiply += value1 * value2;
				norm2 += value2 * value2;
				termFreqMap22.remove(term1);
			}
			norm1 += value1 * value1;
		}
		
		for(Map.Entry<String, Integer> entry : termFreqMap22.entrySet()){
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
