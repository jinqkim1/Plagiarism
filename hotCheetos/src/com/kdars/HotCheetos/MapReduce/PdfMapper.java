package com.kdars.HotCheetos.MapReduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.kdars.HotCheetos.DB.DBManager;

public class PdfMapper extends Mapper<Text, BytesWritable, LongWritable, Text>{
	
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";

	@Override
	public void map(Text title, BytesWritable file, Context context) throws IOException, InterruptedException {
		
		PDDocument pdf = PDDocument.load(new ByteArrayInputStream(file.getBytes()));
		PDFTextStripper stripper = new PDFTextStripper();
		String content = stripper.getText(pdf);
		String fileName = title.toString();
		
//		Path file = new Path(filePath.toString());
//		FileSystem fs = file.getFileSystem(context.getConfiguration());
//		FSDataInputStream fileIn = fs.open(file);
//		PDDocument pdf = PDDocument.load(fileIn);
//		PDFTextStripper stripper = new PDFTextStripper();
//		String content = stripper.getText(pdf);
//		String fileName = title.toString();
		
		//raw text���� Korean, English, Period, Whitespace�� �ɷ���.
		StringBuilder processingContent = new StringBuilder();
		String[] processingLines = content.trim().split("\\r?\\n");
		
		for(String oneLine : processingLines){
			processingContent.append(textExtractor(oneLine) + "/n");
		}
		
		
		//title�� text�� DB�� ���� �� DB���� auto-increment�� �Ҵ���� docID�� ������ ��.
		LongWritable docID = new LongWritable();
		docID.set((long) DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(fileName, processingContent.toString()));
		
		
		Text processedContent = new Text();
		processedContent.set(processingContent.toString());
		context.write(docID, processedContent);
	}
	
	private String textExtractor(String str){
		StringBuilder result = new StringBuilder();
		Pattern p = Pattern.compile(extractTextPattern);
		Matcher m = p.matcher(str);
		
		while(m.find()){
			result.append(String.valueOf(str.charAt(m.start())));
		}
		
		return result.toString();
	}
	
}