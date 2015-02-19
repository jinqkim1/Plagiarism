package com.kdars.HotCheetos.Parsing;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class NGram_string_Driver {
	
	public void driver(){
		JobClient client = new JobClient();
		JobConf conf = new JobConf(NGram_string_Driver.class);
		
		// specify output types
		conf.setOutputKeyClass(IntWritable.class);  //Document ID.
		conf.setOutputValueClass(Text.class);  //CSVcontent. (한 문서의 InvertedIndex hashmap을 csv형태로 string화한 것.)
		
		// specify input and output dirs
		FileInputFormat.addInputPath(conf, new Path("input"));
		FileOutputFormat.setOutputPath(conf, new Path("output"));
		
		// specify a mapper
		conf.setMapperClass(NGram_string_Mapper.class);
		
		// specify a reducer
		conf.setReducerClass(NGram_string_Reducer.class);
		conf.setCombinerClass(NGram_string_Reducer.class); // Embarrassingly parallel하기 때문에 combiner는 필요 없을 듯?
		
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
