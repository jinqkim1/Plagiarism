package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Noun_string_mapReduce {
	
	private String postFix1 = Configurations.getInstance().getPostFix1();
	private String postFix2 = Configurations.getInstance().getPostFix2();
	
	
	/*Temporary measure for experiment.  Need to delete!!!! */
	private int nGramSetting = Configurations.getInstance().getNgramSetting();
	private int fingerprintSetting = Configurations.getInstance().getFingerprintSetting();
	/*Temporary measure for experiment.  Need to delete!!!! */
	
	
	public DocumentInfo parseDoc(String content, int documentID, int invertedIndexTableID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");
		
		ArrayList<String> nGramMaker = new ArrayList<String>();
		StringBuilder ngramMaker = new StringBuilder();
		
		int ngramCheckIndex = 0;
		int ngramChecker = 0;
		for (int i = 0; i < wordList.length; i++) {
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				
				ngramChecker++;
				ngramMaker.append(word);
				nGramMaker.add(word);
				
				if(ngramChecker != this.nGramSetting){
					continue;
				}
				
				ngramChecker--;
				if(ngramCheckIndex != 0){
					ngramMaker.replace(0, ngramCheckIndex, "");
				}
				
				addHash(docInfo, ngramMaker.toString());
				ngramCheckIndex = nGramMaker.get(0).length();
				nGramMaker.remove(0);
			}			
		}
		
		DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID);
		
		return docInfo;
		
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
	
	private String deletePostFix(String processedString) {

		if (processedString.length() < 3) {
			return processedString;
		}

		String checkStr = processedString
				.substring(processedString.length() - 2);
		if (Pattern.compile(postFix2).matcher(checkStr).find()) {
			processedString = processedString.substring(0,
					processedString.length() - 2);
			return processedString;
		}

		checkStr = processedString.substring(processedString.length() - 1);
		if (Pattern.compile(postFix1).matcher(checkStr).find()) {
			processedString = processedString.substring(0,
					processedString.length() - 1);
			return processedString;
		}

		return processedString;
	}
	
}
