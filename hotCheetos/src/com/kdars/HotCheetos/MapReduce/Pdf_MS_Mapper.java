package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.kdars.HotCheetos.DB.DBManager;

public class Pdf_MS_Mapper extends Mapper<Text, Text, LongWritable, Text>{
	
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";

	@Override
	public void map(Text title, Text file, Context context) throws IOException, InterruptedException {
		Path path = new Path(file.toString());
		Configuration conf = context.getConfiguration();
			
		if(DBManager.getInstance().checkFile(file.toString())){
			return;
		}
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+file.toString()+"')");
		long time  = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+file.toString()+"'");
		
		
		
		LongWritable docID = new LongWritable();
		StringBuilder processingContent = new StringBuilder();
		
		FileSystem fs = null;
		FSDataInputStream filein= null;
		PDDocument doc= null;
		PDFTextStripper stripper= null;
		try {
			fs = path.getFileSystem(conf);

			filein = fs.open(path);
			doc = PDDocument.loadNonSeq(filein, null);
			stripper = new PDFTextStripper();
			String content = stripper.getText(doc);

			String[] processingLines = content.trim().split("\\r?\\n");

			for (String oneLine : processingLines) {
				processingContent.append(textExtractor(oneLine) + "/n");
			}
					
			try {
				long value = DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(path.getName().toString(), processingContent.toString());
				docID.set(value) ;
				DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `status`='"+value+"' where `type`='"+file.toString()+"'");
				//DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set test='"+escapedText+"' where `type`='"+file.toString()+"'");
				
			} catch (Exception ex) {
				DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `ex`='exception1' where `type`='"+file.toString()+"'");
			}
	
		}catch(Exception e){
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `ex`='exception2' where `type`='"+file.toString()+"'");
		}
		
		Text processedContent = new Text();
		
		processedContent.set(processingContent.toString());
		
		context.write(docID, processedContent);
		
		doc.close();
		filein.close();
		fs.close();
		
		
		
		time  = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+file.toString()+"'");
	
		
		
	}
	
	private String escape(String text) {
		String result = StringEscapeUtils.escapeHtml4(text);
		result = result.replaceAll("'", "`");
		return result;
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