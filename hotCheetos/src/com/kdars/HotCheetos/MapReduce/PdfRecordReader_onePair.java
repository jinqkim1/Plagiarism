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
	
	private String fileName = null;
	private String file = null;
	private Text key = null; //PDF ���� ���� 
	private Text value = null; //PDF ���� �ؽ�Ʈ

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
		
		FileSplit split = (FileSplit) genericSplit;
		this.fileName = split.getPath().getName();
		this.file = split.getPath().toString();
//		FileSplit split = (FileSplit) genericSplit;
//		Configuration job = context.getConfiguration();
//		final Path file = split.getPath();
//
//		/*
//		 * The below code contains the logic for opening the file and seek to
//		 * the start of the split. Here we are applying the Pdf Parsing logic ����
//		 * file system�� PDF file�� ����Ǿ� �־�� ��
//		 */
//
//		try {
//			FileSystem fs = file.getFileSystem(job);
//			FSDataInputStream fileIn = fs.open(split.getPath());
//			PDDocument pdf = PDDocument.load(fileIn);
//			PDFTextStripper stripper = new PDFTextStripper();
//			this.content = stripper.getText(pdf);
//			this.fileName = file.getName();
//		} catch (Exception ex) {
//			this.content = "";
//			this.fileName = "";
//		}

	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {

		if (key == null) {
			key = new Text();
			key.set(this.fileName);
			value = new Text();
			value.set(this.file);
			
//			Path file = this.fileSplit.getPath();
//			key = file;
//			value = PDDocument.load(file.getFileSystem(this.job).open(file));
		} else {
			return false;
		}
		//key�� value ���� �� �������� ����ִٸ� �� �̻� pair�� ��� �ɷ� return. �� �� ���� �־�ٸ� true. ������ �� ��� ù��°�� true, �� ������ �ٷ� false�� ����.
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
		// ���� implement ���� ����
		// �ٸ� ������ Ȱ������ �ʰ� ����
		return 0;
	}

	@Override
	public void close() throws IOException {
		// ���� implement ���� ����
		// �ٸ� ������ Ȱ������ �ʰ� ����
	}

}
