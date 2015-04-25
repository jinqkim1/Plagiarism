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

public class ParsedInvertedIndexSaveDriver {
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job finalJob = Job.getInstance(conf);
		finalJob.setJobName("Insertion of Input Documents in invertedindextable");
		finalJob.setJarByClass(com.kdars.HotCheetos.MapReduce.ParsedInvertedIndexSaveDriver.class);
		
		//Turning off reducer for this job
		finalJob.setNumReduceTasks(0);
		
		// specify output types
		finalJob.setOutputKeyClass(IntWritable.class);  //Title of a PDF file
		finalJob.setOutputValueClass(MapWritable.class);  //����, �ѱ�, whitespace, period�� ������ content of a PDF file
		
		// specify input and output dirs
		finalJob.setInputFormatClass(SequenceFileInputFormat.class);
		finalJob.setOutputFormatClass(NullOutputFormat.class);
		FileInputFormat.addInputPath(finalJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(finalJob, new Path(outputPath));
		
		// specify a mapper
		finalJob.setMapperClass(ParsedInvertedIndexSaveSentenceMapper.class);
		
		return finalJob.waitForCompletion(true)?0:1;
	}
}
