package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;


public class PdfFileInputFormat extends FileInputFormat<Text, Object> {

	@Override
	public RecordReader<Text, Object> createRecordReader(InputSplit arg0, TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
//		return new PdfRecordReader();
		return new PdfRecordReader_onePair();
	}

}