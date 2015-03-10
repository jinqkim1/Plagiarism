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
		
		//raw text���� Korean, English, Period, Whitespace�� �ɷ���.
		StringBuilder processingContent = new StringBuilder();
		String[] processingLines = content.toString().trim().split("\\r?\\n");
		
		for(String oneLine : processingLines){
			processingContent.append(textExtractor(oneLine) + "/n");
		}
		
		
		//title�� text�� DB�� ���� �� DB���� auto-increment�� �Ҵ���� docID�� ������ ��.
		IntWritable docID = new IntWritable();
		docID.set(DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(title.toString(), processingContent.toString()));
		
		
		//Parsing ���� �� .parsdoc���� invertedindextable�� ����.
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		
		//���⼭ inverted index table�� ������.
		//�ϴ� test�� ���� default�� 3-gram hashcode fingerprint �����ϰ� ��.
		MapWritable termFreqMap = test.parseDoc(processingContent.toString(), docID.get(), 77);  // ���⼭ 0�� table id�� �ϴ� default table��  'invertedindextable'�� �����ϰ� ��.
		
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