package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class ParsingDriver {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job firstJob = Job.getInstance(conf);
		firstJob.setJobName("Parse");
		firstJob.setJarByClass(com.kdars.HotCheetos.MapReduce.ParsingDriver.class);
		
		//Turning off reducer for this job
		firstJob.setNumReduceTasks(0);
		
		// specify output types
		firstJob.setOutputKeyClass(LongWritable.class);  //DocID
		firstJob.setOutputValueClass(MapWritable.class);  //HashMap of terms and termFrequencies
		
		
//		firstJob.setMapOutputKeyClass(Text.class);  //Title of a PDF file
//		firstJob.setMapOutputValueClass(Text.class);  //����, �ѱ�, whitespace, period�� ������ content of a PDF file
//		firstJob.setOutputKeyClass(IntWritable.class);  //DocID
//		firstJob.setOutputValueClass(MapWritable.class);  //HashMap of terms and termFrequencies
		
		// specify input and output dirs
		firstJob.setInputFormatClass(SequenceFileInputFormat.class);
		firstJob.setOutputFormatClass(SequenceFileOutputFormat.class);
		FileInputFormat.addInputPath(firstJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(firstJob, new Path(outputPath));
		
		// specify a mapper
		firstJob.setMapperClass(ParsingMapper.class);
		
//		// specify a reducer
//		firstJob.setReducerClass(PdfReducer.class);
		
		return firstJob.waitForCompletion(true)?0:1;
	}
}
