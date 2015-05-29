package com.kdars.HotCheetos.MapReduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;

public class ParsedInvertedIndexSaveSentenceMapper extends Mapper<LongWritable, BytesWritable, IntWritable, MapWritable>{
	
	@Override
	public void map(LongWritable docID, BytesWritable termFreqMap, Context context) throws IOException, InterruptedException {
		
		int tableID = Configurations.getInstance().getTableID();
		
		HashMap<Integer, HashMap<String, Integer>> sentenceMap = bytesWritableToHashMap(termFreqMap);
		
		DBManager.getInstance().insertBulkToSentenceTable_MapReduce((int) docID.get(), sentenceMap, tableID);
		
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
	
}
