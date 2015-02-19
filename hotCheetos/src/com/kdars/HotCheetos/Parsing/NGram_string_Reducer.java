package com.kdars.HotCheetos.Parsing;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class NGram_string_Reducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{

	@Override
	public void reduce(Text inputKey, Iterator<IntWritable> inputValues,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		
		int sum = 0;
		while (inputValues.hasNext()){
			IntWritable value = (IntWritable) inputValues.next();
			sum += value.get();
		}
		
		output.collect(inputKey, new IntWritable(sum));
	}
	
}
