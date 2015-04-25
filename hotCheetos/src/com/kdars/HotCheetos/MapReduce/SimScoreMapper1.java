package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;

public class SimScoreMapper1  extends Mapper<LongWritable, MapWritable, IntWritable, MapWritable>{
	
	@Override
	public void map(LongWritable docID, MapWritable termFreqMap, Context context) throws IOException, InterruptedException {
		
		long time  = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
	
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+docID.toString()+"')");
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+docID.toString()+"'");
		
		String intermediateOUTPUT_PATH1 = Configurations.getInstance().getIntermediateOUTPUT_PATH1();

		int tableID = Configurations.getInstance().getTableID();
		
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
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
							csvContent.append(simScore_Calculation_OneVSInputCorpus(key, value, docID, termFreqMap));

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
		
		time  = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+docID.toString()+"'");
		
		
		
		return;
	}
	
	private String simScore_Calculation_OneVSInputCorpus(LongWritable docID1, MapWritable termFreqMap1, LongWritable docID2, MapWritable termFreqMap2){
		int docid1 = (int) docID1.get();
		int docid2 = (int) docID2.get();
		double simscore = calcSim(termFreqMap1, termFreqMap2);
		return docid1 + "," + docid2 + "," + simscore + "\n";
	}
	
	private double calcSim(MapWritable termFreqMap1, MapWritable termFreqMap2){
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
