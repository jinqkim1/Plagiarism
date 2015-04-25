package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

public class SimScoreDriver2 {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job secondJob = Job.getInstance(conf);
		secondJob.setJobName("Similarity Score Calculation");
		secondJob.setJarByClass(com.kdars.HotCheetos.MapReduce.SimScoreDriver2.class);
		
		//Turning off reducer for this job
		secondJob.setNumReduceTasks(0);
		
		// specify output types
		secondJob.setOutputKeyClass(IntWritable.class);  //Title of a PDF file
		secondJob.setOutputValueClass(MapWritable.class);  //����, �ѱ�, whitespace, period�� ������ content of a PDF file
		
		// specify input and output dirs
		secondJob.setInputFormatClass(SequenceFileInputFormat.class);
		secondJob.setOutputFormatClass(NullOutputFormat.class);
		FileInputFormat.addInputPath(secondJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(secondJob, new Path(outputPath));
		
		// specify a mapper
		secondJob.setMapperClass(SimScoreSentenceMapper2.class);
		
//		//DBConfiguration
//		String DBurl = Configurations.getInstance().DB_JDBC_URL;
//		String DBuserID = Configurations.getInstance().DB_USER_ID;
//		String DBpassword = Configurations.getInstance().DB_USER_PASS;
//		String tableName = Configurations.getInstance().DB_TABLE_NAME_INDEX; //invertedIndexTable �̸�.
//		String[] fields = {"Index", "DocID", "Term", "TermFrequency"}; //invertedIndexTable column��.
//		
//		DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", DBurl, DBuserID, DBpassword);
//		
//		String inputQuery = "SELECT Term, TermFrequency FROM " + tableName + " WHERE " + " ORDER BY ";  //condition ���⼭ ��������� ��!
//		String inputCountQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE " + " ORDER BY ";
//		
//		DBInputFormat.setInput(secondJob, inputDBRecords.class, inputQuery, inputCountQuery);
		
		// specify a reducer
		//secondJob.setReducerClass(SimScoreReducer.class);
//		firstJob.setCombinerClass(PdfReducer.class); // Embarrassingly parallel�ϱ� ������ combiner�� �ʿ� ���� ��?
		
		
		int jobComplete1 = secondJob.waitForCompletion(true)?0:1;  //job�� ������ jobcomplete�� ���� ��.
		
		if (jobComplete1 == 0){  //job�� ������ outputpath ����. ���� file system�� ������� ������ directory�� ��������� ������, ���� job�� ���� ����� ����.
			FileSystem hdfs = FileSystem.get(conf);
			Path deletePath = new Path(outputPath);
			if(hdfs.exists(deletePath)){
				hdfs.delete(deletePath, true);  //delete existing directory
			}
		}
		
		return jobComplete1;
	}
}
