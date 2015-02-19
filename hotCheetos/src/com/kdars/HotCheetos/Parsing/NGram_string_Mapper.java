package com.kdars.HotCheetos.Parsing;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class NGram_string_Mapper extends MapReduceBase implements Mapper<IntWritable, Text, Text, IntWritable>{
	// Mapper< inputDocID, inputDocumentContent, outputNgram, outputOne>
	
	private final IntWritable one = new IntWritable(1);
	private Text word = new Text();
	private int nGramSetting = Configuration.getInstance().getNgramSetting();
	private int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	
	private int invertedIndexTableID = 37;  //3-gram string fingerprint
	
	@Override
	public void map(IntWritable inputKey, Text inputValue, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String documentContent = inputValue.toString();
		int documentID = inputKey.get();
		
		parseDoc(documentContent, output);
	}
	
	private void parseDoc(String content, OutputCollector<Text, IntWritable> output) throws IOException {
		
		String wordList[] = content.trim().split("\\s+");
		
		StringBuilder ngramMaker = new StringBuilder();
		
		int ngramCheckIndex = 0;
		int ngramChecker = 0;
		for (int i = 0; i < wordList.length; i++) {
			String word = wordList[i];
//			//�� ����¥���� n-gram���� ��ġ�� �� ���� ���� Ž.
//			if (word.length() == 1){
//				ngramMaker = new StringBuilder();
//				ngramCheckIndex = 0;
//				ngramChecker = 0;
//				continue;
//			}
			
			ngramChecker++;
			ngramMaker.append(word);
			
			if(ngramChecker != nGramSetting){
				
//				//��ħǥ�� ������ �ܾ��� ���, skip�ϰ� ���� ���� Ž.
//				if (word.charAt(word.length()-1) == '.'){
//					ngramMaker = new StringBuilder();
//					ngramCheckIndex = 0;
//					ngramChecker = 0;
//				}
				
				continue;
			}
			
//			//��ħǥ�� ������ �ܾ��� ���, �ؽ��ʿ� ���ϰ� ���� �� ���� ���� Ž.
//			if (word.charAt(word.length()-1) == '.'){
//				if(ngramCheckIndex != 0){
//					ngramMaker.replace(0, ngramCheckIndex, "");
//				}
//				String ngram = ngramMaker.toString();
//				docInfo = addHash(docInfo, ngram.substring(0, ngram.length() - 1));
//				ngramMaker = new StringBuilder();
//				ngramCheckIndex = 0;
//				ngramChecker = 0;
//				continue;
//			}
			
			ngramChecker--;
			if(ngramCheckIndex != 0){
				ngramMaker.replace(0, ngramCheckIndex, "");
			}
			
			addHash(ngramMaker.toString(), output);
			ngramCheckIndex = wordList[i- nGramSetting + 1].length();
			
		}
	
	}
	
	private void addHash(String term, OutputCollector<Text, IntWritable> output) throws IOException {
		if (term.hashCode() % fingerprintSetting == 0) {
			word.set(term);
			output.collect(word, one);
		}
	}
	
}
