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
		
		
		DBManager.getInstance().insertSQLMapperin("insert into `plagiarismdb`.`mapperin` (`type`) value ('"+file.toString()+"')");
		
		
		if(DBManager.getInstance().checkFile(file.toString())){
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `duple`=`duple`+1 where `type`='"+file.toString()+"'");
			return;
		}
		
		

		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+file.toString()+"')");
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+file.toString()+"'");
		
		
		try{
			FileSystem fs = path.getFileSystem(conf);
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `size`='"+fs.getLength(path)+"' where `type`='"+file.toString()+"'");
			
			FSDataInputStream filein = fs.open(path);
			PDDocument doc = PDDocument.loadNonSeq(filein, null);
			PDFTextStripper stripper = new PDFTextStripper();
			String content = stripper.getText(doc);
	
			doc.close();
			filein.close();
			fs.close();
			
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `status`='"+content+"' where `type`='"+file.toString()+"'");
		}catch(Exception e){
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='exception : "+e.toString()+"' where `type`='"+file.toString()+"'");
		}
		
		
		
		
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+file.toString()+"'");
		
		
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