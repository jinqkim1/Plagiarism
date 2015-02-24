package com.kdars.HotCheetos.SimilarityScore;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class SimilarityCalculation_Mapper extends MapReduceBase implements Mapper<MapWritable, MapWritable, Text, IntWritable>{

	@Override
	public void map(MapWritable arg0, MapWritable arg1,
			OutputCollector<Text, IntWritable> arg2, Reporter arg3)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
