package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Noun_hashcode_mapReduce {
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
		
		ArrayList<Integer> nGramMaker = new ArrayList<Integer>();
		int ngramMaker = 0;
		int ngramSupportNumber = 0;
		int ngramChecker = 0;
		for (int i = 0; i < wordList.length; i++) {
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				ngramChecker++;
				ngramMaker += word.hashCode();
				nGramMaker.add(word.hashCode());
				
				if (ngramChecker != this.nGramSetting){
					continue;
				}
				
				ngramChecker--;
				
				if (ngramSupportNumber != 0){
					ngramMaker -= ngramSupportNumber;
				}
				addHash(docInfo, ngramMaker);
				ngramSupportNumber = nGramMaker.get(0);
				nGramMaker.remove(0);
				
			}
		}
		
		DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID);
		
		return docInfo;
		
	}
	
	private void addHash(DocumentInfo docInfo, int hash) {
		if (hash % this.fingerprintSetting != 0) {
			return;
		}
		String hashToString = String.valueOf(hash);
		if (docInfo.termFreq.containsKey(hashToString)) {
			int value = docInfo.termFreq.get(hashToString);
			docInfo.termFreq.put(hashToString, value + 1);
			return;
		}
		docInfo.termFreq.put(hashToString, 1);
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
