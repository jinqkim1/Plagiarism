package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.Parsing.Sentence_string_mapReduce_forSEUNGCHUL;
import com.kdars.HotCheetos.Parsing.Sentence_string_mapReduce_forSEUNGCHUL_nonWritable;

public class ParsingSentenceMapper1 extends Mapper<LongWritable, Text, LongWritable, BytesWritable>{

	@Override
	public void map(LongWritable docID, Text processedContent, Context context) throws IOException, InterruptedException {
		
		Sentence_string_mapReduce_forSEUNGCHUL_nonWritable test = new Sentence_string_mapReduce_forSEUNGCHUL_nonWritable();
		
		BytesWritable sentenceMap = test.parseDoc((int) docID.get(), processedContent.toString());
		
		context.write(docID, sentenceMap);
	}
	
}
