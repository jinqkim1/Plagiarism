package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PdfMapper extends Mapper<Text, Text, Text, Text>{
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";
	
	@Override
	public void map(Text title, Text content, Context context) throws IOException, InterruptedException {
		
		StringBuilder processingContent = new StringBuilder();
		String[] processingLines = content.toString().trim().split("\\r?\\n");
		
		for(String oneLine : processingLines){
			processingContent.append(textExtractor(oneLine));
		}
		
		Text processedContent = new Text();
		processedContent.set(processingContent.toString());
		
		context.write(title, processedContent);
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