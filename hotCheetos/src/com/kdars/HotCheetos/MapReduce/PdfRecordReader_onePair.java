package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfRecordReader_onePair extends RecordReader<Text, Text> {

	private String content = null;
	private String fileName = null;
	private Text key = null; //PDF 파일 제목 
	private Text value = null; //PDF 파일 텍스트

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		
		FileSplit split = (FileSplit) genericSplit;
		Configuration job = context.getConfiguration();
		final Path file = split.getPath();

		/*
		 * The below code contains the logic for opening the file and seek to
		 * the start of the split. Here we are applying the Pdf Parsing logic
		 * 먼저 file system에 PDF file이 저장되어 있어야 함
		 */

		FileSystem fs = file.getFileSystem(job);
		FSDataInputStream fileIn = fs.open(split.getPath());
		PDDocument pdf = PDDocument.load(fileIn);
		PDFTextStripper stripper = new PDFTextStripper();
		this.content = stripper.getText(pdf);
		this.fileName = file.getName();
		}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {

		if (key == null) {
			key = new Text();
			key.set(this.fileName);
			value = new Text();
			value.set(this.content);
		} else {
			return false;
		}
		//key나 value 값이 이 시점에서 비어있다면 더 이상 pair가 없는 걸로 return. 둘 다 값이 있었다면 true. 하지만 이 경우 첫번째만 true, 그 다음은 바로 false로 빠짐.
		if (key == null || value == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {

		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {

		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// 아직 implement 하지 않음
		// 다른 곳에서 활용하지 않고 있음
		return 0;
	}

	@Override
	public void close() throws IOException {
		// 아직 implement 하지 않음
		// 다른 곳에서 활용하지 않고 있음
	}

}
