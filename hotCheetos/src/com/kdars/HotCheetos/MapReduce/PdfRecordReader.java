package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfRecordReader extends RecordReader {

	private String[] lines = null;
	private LongWritable key = null; //line 넘버 
	private Text value = null; //line의 text

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
		String parsedText = null;
		PDFTextStripper stripper = new PDFTextStripper();
		parsedText = stripper.getText(pdf);
		this.lines = parsedText.split("\n");
		}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {

		if (key == null) {
			key = new LongWritable();
			key.set(1);
			value = new Text();
			value.set(lines[0]);
		} else {
			int temp = (int) key.get();
			if (temp < (lines.length - 1)) {
				int count = (int) key.get();
				value = new Text();
				value.set(lines[count]);
				count = count + 1;
				key = new LongWritable(count);
			} else {
				return false;
			}

		}
		if (key == null || value == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {

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
