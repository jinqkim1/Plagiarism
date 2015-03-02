package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PdfDriver {
	
	public void driver() throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job firstJob = Job.getInstance(conf);
		firstJob.setJobName("ExtractAndParse");
		
		// specify output types
		firstJob.setMapOutputKeyClass(Text.class);  //Title of a PDF file
		firstJob.setMapOutputValueClass(Text.class);  //����, �ѱ�, whitespace, period�� ������ content of a PDF file
		firstJob.setOutputKeyClass(Text.class);  //Term
		firstJob.setOutputValueClass(IntWritable.class);  //TermFrequency
		
		// specify input and output dirs
		firstJob.setInputFormatClass(PdfFileInputFormat.class);
		firstJob.setOutputFormatClass(FileOutputFormat.class);
		FileInputFormat.addInputPath(firstJob, new Path("input"));
		FileOutputFormat.setOutputPath(firstJob, new Path("output"));
		
		// specify a mapper
		firstJob.setMapperClass(PdfMapper.class);
		
		// specify a reducer
		firstJob.setReducerClass(PdfReducer.class);
		firstJob.setCombinerClass(PdfReducer.class); // Embarrassingly parallel�ϱ� ������ combiner�� �ʿ� ���� ��?
		
		System.exit(firstJob.waitForCompletion(true)?0:1);

	}
}