package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.kdars.HotCheetos.DB.DBManager;

public class PdfDriver {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {

		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('start driver')");
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		Job firstJob = Job.getInstance(conf);
		firstJob.setJarByClass(com.kdars.HotCheetos.MapReduce.PdfDriver.class);
		firstJob.setJobName("Extract");

		// specify output types
		firstJob.setOutputKeyClass(LongWritable.class);  //DocID
		firstJob.setOutputValueClass(Text.class);  		 //HashMap of terms and termFrequencies
		firstJob.setInputFormatClass(WholeFileInputFormat.class);
		firstJob.setMapperClass(PdfMapper.class);
		firstJob.setNumReduceTasks(0);		
		firstJob.setReducerClass(PdfReducer.class);
		FileInputFormat.addInputPath(firstJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(firstJob, new Path(outputPath));
		
		return firstJob.waitForCompletion(true)?0:1;
	}
}
