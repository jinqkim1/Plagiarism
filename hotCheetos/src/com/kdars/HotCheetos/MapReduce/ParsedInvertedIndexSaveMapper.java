package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;

public class ParsedInvertedIndexSaveMapper extends Mapper<LongWritable, MapWritable, IntWritable, MapWritable>{
	
	@Override
	public void map(LongWritable docID, MapWritable termFreqMap, Context context) throws IOException, InterruptedException {
		
		int tableID = Configurations.getInstance().getTableID();
		DBManager.getInstance().insertBulkToHashTable_MapReduce((int) docID.get(), termFreqMap, tableID);
		
		return;
	}
}
