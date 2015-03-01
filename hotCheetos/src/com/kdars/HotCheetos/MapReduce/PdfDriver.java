package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.kdars.HotCheetos.Parsing.NGram_string_Driver;

public class PdfDriver {
	
	public void driver() throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		// specify output types
		job.setOutputKeyClass(Text.class);  //Term
		job.setOutputValueClass(IntWritable.class);  //TermFrequency
		
		// specify input and output dirs
		job.setInputFormatClass(PdfFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path("input"));
		FileOutputFormat.setOutputPath(job, new Path("output"));
		
		// specify a mapper
		job.setMapperClass(PdfMapper.class);
		
		// specify a reducer
		job.setReducerClass(PdfReducer.class);
		job.setCombinerClass(PdfReducer.class); // Embarrassingly parallel하기 때문에 combiner는 필요 없을 듯?
		
		System.out.println(job.waitForCompletion(true));

	}
}
