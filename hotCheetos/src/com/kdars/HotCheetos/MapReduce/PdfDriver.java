package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class PdfDriver {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job firstJob = Job.getInstance(conf);
		firstJob.setJobName("ExtractAndParse");
		firstJob.setJarByClass(com.kdars.HotCheetos.MapReduce.PdfDriver.class);
		
		//Turning off reducer for this job
		firstJob.setNumReduceTasks(0);
		
		// specify output types
		firstJob.setMapOutputKeyClass(IntWritable.class);  //DocID
		firstJob.setMapOutputValueClass(MapWritable.class);  //HashMap of terms and termFrequencies

//		firstJob.setMapOutputKeyClass(Text.class);  //Title of a PDF file
//		firstJob.setMapOutputValueClass(Text.class);  //영어, 한글, whitespace, period만 포함한 content of a PDF file
//		firstJob.setOutputKeyClass(IntWritable.class);  //DocID
//		firstJob.setOutputValueClass(MapWritable.class);  //HashMap of terms and termFrequencies
		
		// specify input and output dirs
		firstJob.setInputFormatClass(PdfFileInputFormat.class);
		firstJob.setOutputFormatClass(SequenceFileOutputFormat.class);
		FileInputFormat.addInputPath(firstJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(firstJob, new Path(outputPath));
		
		// specify a mapper
		firstJob.setMapperClass(PdfMapper.class);
		
//		// specify a reducer
//		firstJob.setReducerClass(PdfReducer.class);
		
		return firstJob.waitForCompletion(true)?0:1;
	}
}
