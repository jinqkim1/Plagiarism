package com.kdars.HotCheetos.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PdfReducer extends Reducer<Text, Text, Text, IntWritable>{
	
	@Override
	public void reduce(Text title, Iterable<Text> content, Context context){
		
	}
}
