package com.kdars.HotCheetos.MapReduce;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.kdars.HotCheetos.DB.DBManager;

public class PdfMapper extends Mapper<Text, Text, LongWritable, Text>{
	
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";

	@Override
	public void map(Text title, Text file, Context context) throws IOException, InterruptedException {
		Path path = new Path(file.toString());
		Configuration conf = context.getConfiguration();
		
	
		
		
		
		

		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+file.toString()+"')");
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+file.toString()+"'");
		
		
		FileSystem fs = path.getFileSystem(conf);
		FSDataInputStream filein = fs.open(path);
		PDDocument doc = PDDocument.loadNonSeq(filein, null);
		PDFTextStripper stripper = new PDFTextStripper();
		String content = stripper.getText(doc);
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `status`='"+content+"' where `type`='"+file.toString()+"'");
		
		doc.close();
		filein.close();
		fs.close();
		
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+file.toString()+"'");
	
		
		
		//DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+file.toString()+"'");
		//PDDocument pdf = PDDocument.load(tempPath);
		//PDFTextStripper stripper = new PDFTextStripper();
		//String content = stripper.getText(pdf);
		//String fileName = file.toString().replace("map : hdfs://slave1.kdars.com:8020/user/hadoop/num_all/", "");
		
//		Path file = new Path(filePath.toString());
//		FileSystem fs = file.getFileSystem(context.getConfiguration());
//		FSDataInputStream fileIn = fs.open(file);
//		PDDocument pdf = PDDocument.load(fileIn);
//		PDFTextStripper stripper = new PDFTextStripper();
//		String content = stripper.getText(pdf);
//		String fileName = title.toString();
		
		//raw text���� Korean, English, Period, Whitespace�� �ɷ���.
		//StringBuilder processingContent = new StringBuilder();
		//String[] processingLines = content.trim().split("\\r?\\n");
		
		//for(String oneLine : processingLines){
		//	processingContent.append(textExtractor(oneLine) + "/n");
		//}
		
		
		//title�� text�� DB�� ���� �� DB���� auto-increment�� �Ҵ���� docID�� ������ ��.
		//LongWritable docID = new LongWritable();
		//docID.set((long) DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(fileName, processingContent.toString()));
		
		
		//Text processedContent = new Text();
		//processedContent.set(processingContent.toString());
		//context.write(docID, processedContent);
		
		
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