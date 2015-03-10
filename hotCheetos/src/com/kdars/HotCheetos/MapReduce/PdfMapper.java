package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.Parsing.NGram_hashcode_mapReduce;

public class PdfMapper extends Mapper<Text, Text, IntWritable, MapWritable>{
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";
	
	@Override
	public void map(Text title, Text content, Context context) throws IOException, InterruptedException {
		
		//raw text에서 Korean, English, Period, Whitespace만 걸러냄.
		StringBuilder processingContent = new StringBuilder();
		String[] processingLines = content.toString().trim().split("\\r?\\n");
		
		for(String oneLine : processingLines){
			processingContent.append(textExtractor(oneLine) + "/n");
		}
		
		
		//title과 text를 DB에 저장 후 DB에서 auto-increment로 할당받은 docID를 가지고 옴.
		IntWritable docID = new IntWritable();
		docID.set(DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(title.toString(), processingContent.toString()));
		
		
		//Parsing 진행 후 .parsdoc에서 invertedindextable에 저장.
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		
		//여기서 inverted index table에 저장함.
		//일단 test를 위해 default로 3-gram hashcode fingerprint 진행하게 됨.
		MapWritable termFreqMap = test.parseDoc(processingContent.toString(), docID.get(), 77);  // 여기서 0은 table id로 일단 default table인  'invertedindextable'에 저장하게 됨.
		
		context.write(docID, termFreqMap);
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