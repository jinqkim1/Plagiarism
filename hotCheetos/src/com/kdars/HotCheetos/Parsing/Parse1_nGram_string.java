package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse1_nGram_string extends Parse1{
	
	
	/*Temporary measure for experiment.  Need to delete!!!! */
	private int nGramSetting = Configuration.getInstance().getNgramSetting();
	private int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	/*Temporary measure for experiment.  Need to delete!!!! */
	
	
	@Override
	boolean parseDoc(String content, int documentID, int invertedIndexTableID) {

		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");
		
		StringBuilder ngramMaker = new StringBuilder();
		
		//word가 마침표를 포함한 경우는 2 글자까지는 무시. 마침표를 포함하지 않은 단어는 1 글자까지는 무시.
		//이 동작을 위해 wordListIndexSupporter 변수 필요.
		ArrayList<Integer> wordListIndexSupporter = new ArrayList<Integer>();
		
		int ngramCheckIndex = 0;
		for (int i = 0; i < wordList.length; i++) {
			String word = wordList[i];
			
			//word가 마침표를 포함한 경우는 2 글자까지는 무시. 마침표를 포함하지 않은 단어는 1 글자까지는 무시.
			if (word.contains(".") && word.length() <= 2){
				continue;
			}else if ((!word.contains(".")) && word.length() < 2){
				continue;
			}
			
//			//한 글자짜리는 n-gram으로 안치고 그 다음 포문 탐.
//			if (word.length() == 1){
//				ngramMaker = new StringBuilder();
//				ngramCheckIndex = 0;
//				ngramChecker = 0;
//				continue;
//			}
			
			//나중에 ngram 에서 뺄 단어의 위치들을 기억하면 됨.
			wordListIndexSupporter.add(i);
			
			ngramMaker.append(word);
			
			if(wordListIndexSupporter.size() != this.nGramSetting){
				
//				//마침표로 끝나는 단어일 경우, skip하고 다음 포문 탐.
//				if (word.charAt(word.length()-1) == '.'){
//					ngramMaker = new StringBuilder();
//					ngramCheckIndex = 0;
//					ngramChecker = 0;
//				}
				
				continue;
			}
			
//			//마침표로 끝나는 단어일 경우, 해쉬맵에 더하고 리셋 후 다음 포문 탐.
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
			
			if(ngramCheckIndex != 0){
				ngramMaker.replace(0, ngramCheckIndex, "");
			}
			
			addHash(docInfo, ngramMaker.toString());
			
			int deleteTermIndex = wordListIndexSupporter.get(0);
			
			ngramCheckIndex = wordList[deleteTermIndex].length();
			wordListIndexSupporter.remove(0);
		}
		
		return DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID);
	
	}
	
	private void addHash(DocumentInfo docInfo, String term) {
		if (term.hashCode() % this.fingerprintSetting != 0) {
			return;
		}
		if (docInfo.termFreq.containsKey(term)) {
			int value = docInfo.termFreq.get(term);
			docInfo.termFreq.put(term, value + 1);
			return;
		}
		docInfo.termFreq.put(term, 1);
	}
	
}
