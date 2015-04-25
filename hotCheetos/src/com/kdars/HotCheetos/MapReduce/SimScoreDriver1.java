package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

public class SimScoreDriver1 {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job thirdJob = Job.getInstance(conf);
		thirdJob.setJobName("Similarity Score Calculation Within Input Documents");
		thirdJob.setJarByClass(com.kdars.HotCheetos.MapReduce.SimScoreDriver1.class);
		
		//Turning off reducer for this job
		thirdJob.setNumReduceTasks(0);
		
		// specify output types
		thirdJob.setOutputKeyClass(IntWritable.class);  //Title of a PDF file
		thirdJob.setOutputValueClass(MapWritable.class);  //����, �ѱ�, whitespace, period�� ������ content of a PDF file
		
		// specify input and output dirs
		thirdJob.setInputFormatClass(SequenceFileInputFormat.class);
		thirdJob.setOutputFormatClass(NullOutputFormat.class);
		FileInputFormat.addInputPath(thirdJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(thirdJob, new Path(outputPath));
		
		// specify a mapper
		thirdJob.setMapperClass(SimScoreSentenceMapper1.class);
		
		return thirdJob.waitForCompletion(true)?0:1;
	}
}
