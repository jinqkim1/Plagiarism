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

public class SimScoreDriver1 {
	
	public int driver(String inputPath, String outputPath) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job secondJob = Job.getInstance(conf);
		secondJob.setJobName("Similarity Score Calculation");
		secondJob.setJarByClass(com.kdars.HotCheetos.MapReduce.SimScoreDriver1.class);
		
		//Turning off reducer for this job
		secondJob.setNumReduceTasks(0);
		
		// specify output types
		secondJob.setOutputKeyClass(IntWritable.class);  //Title of a PDF file
		secondJob.setOutputValueClass(MapWritable.class);  //영어, 한글, whitespace, period만 포함한 content of a PDF file
		
		// specify input and output dirs
		secondJob.setInputFormatClass(SequenceFileInputFormat.class);
		secondJob.setOutputFormatClass(NullOutputFormat.class);
		FileInputFormat.addInputPath(secondJob, new Path(inputPath));
		FileOutputFormat.setOutputPath(secondJob, new Path(outputPath));
		
		// specify a mapper
		secondJob.setMapperClass(SimScoreMapper1.class);
		
//		//DBConfiguration
//		String DBurl = Configurations.getInstance().DB_JDBC_URL;
//		String DBuserID = Configurations.getInstance().DB_USER_ID;
//		String DBpassword = Configurations.getInstance().DB_USER_PASS;
//		String tableName = Configurations.getInstance().DB_TABLE_NAME_INDEX; //invertedIndexTable 이름.
//		String[] fields = {"Index", "DocID", "Term", "TermFrequency"}; //invertedIndexTable column들.
//		
//		DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", DBurl, DBuserID, DBpassword);
//		
//		String inputQuery = "SELECT Term, TermFrequency FROM " + tableName + " WHERE " + " ORDER BY ";  //condition 여기서 지정해줘야 함!
//		String inputCountQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE " + " ORDER BY ";
//		
//		DBInputFormat.setInput(secondJob, inputDBRecords.class, inputQuery, inputCountQuery);
		
		// specify a reducer
		//secondJob.setReducerClass(SimScoreReducer.class);
//		firstJob.setCombinerClass(PdfReducer.class); // Embarrassingly parallel하기 때문에 combiner는 필요 없을 듯?
		
		
		int jobComplete1 = secondJob.waitForCompletion(true)?0:1;  //job이 끝나야 jobcomplete에 값이 들어감.
		
		if (jobComplete1 == 0){  //job이 끝나면 outputpath 지움. 결과는 file system에 저장되지 않지만 directory는 만들어지기 때문에, 다음 job을 위해 지우는 것임.
			FileSystem hdfs = FileSystem.get(conf);
			Path deletePath = new Path(outputPath);
			if(hdfs.exists(deletePath)){
				hdfs.delete(deletePath, true);  //delete existing directory
			}
		}
		
		return jobComplete1;
	}
}
