package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.kdars.HotCheetos.Config.Configurations;

public class SimScoreDriver {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		driver(args);
	}
	
	public static void driver(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		Job secondJob = Job.getInstance(conf);
		secondJob.setJobName("Similarity Score Calculation");
		secondJob.setJarByClass(com.kdars.HotCheetos.MapReduce.SimScoreDriver.class);
		
		// specify output types
		secondJob.setMapOutputKeyClass(Text.class);  //Title of a PDF file
		secondJob.setMapOutputValueClass(Text.class);  //영어, 한글, whitespace, period만 포함한 content of a PDF file
		secondJob.setOutputKeyClass(Text.class);  //Term
		secondJob.setOutputValueClass(IntWritable.class);  //TermFrequency
		
		// specify input and output dirs
		secondJob.setInputFormatClass(DBInputFormat.class);
		secondJob.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(secondJob, new Path(args[1]));
		FileOutputFormat.setOutputPath(secondJob, new Path(args[2]));
		
		//DBConfiguration
		String DBurl = Configurations.getInstance().DB_JDBC_URL;
		String DBuserID = Configurations.getInstance().DB_USER_ID;
		String DBpassword = Configurations.getInstance().DB_USER_PASS;
		String tableName = Configurations.getInstance().DB_TABLE_NAME_INDEX; //invertedIndexTable 이름.
		String[] fields = {"Index", "DocID", "Term", "TermFrequency"}; //invertedIndexTable column들.
		
		DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", DBurl, DBuserID, DBpassword);
		
		String inputQuery = "SELECT Term, TermFrequency FROM " + tableName + " WHERE " + " ORDER BY ";  //condition 여기서 지정해줘야 함!
		String inputCountQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE " + " ORDER BY ";
		
		DBInputFormat.setInput(secondJob, inputDBRecords.class, inputQuery, inputCountQuery);
		
		// specify a mapper
		secondJob.setMapperClass(SimScoreMapper.class);
		
		// specify a reducer
		secondJob.setReducerClass(SimScoreReducer.class);
//		firstJob.setCombinerClass(PdfReducer.class); // Embarrassingly parallel하기 때문에 combiner는 필요 없을 듯?
		
		System.exit(secondJob.waitForCompletion(true)?0:1);

	}
}
