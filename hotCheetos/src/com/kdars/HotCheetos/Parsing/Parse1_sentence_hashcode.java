package com.kdars.HotCheetos.Parsing;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.DocumentStructure.SentenceInfo;

public class Parse1_sentence_hashcode extends Parse1{
	
	/*Temporary measure for experiment.  Need to delete!!!! */
	private int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	/*Temporary measure for experiment.  Need to delete!!!! */
	
	@Override
	boolean parseDoc(String content, int documentID, int invertedIndexTableID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;
		
		String sentenceList[] = content.trim().split(".");
		
		int sentenceID = 0;
		for(String sentence : sentenceList){
			if(sentence.length() > 5){
				sentenceID++;
				docInfo.sentenceInfoMap.put(sentenceID, parseSentence(sentence, sentenceID));
			}
		}
		
		
		return DBManager.getInstance().insertBulkToSentenceTable(docInfo, invertedIndexTableID);
	}
	
	private SentenceInfo parseSentence(String sentence, int sentenceID){
		SentenceInfo senInfo = new SentenceInfo();
		senInfo.sentenceID = sentenceID;
		
		String wordList[] = sentence.trim().split("\\s+");
		
//		senInfo.sentenceSize = wordList.length;
		
		for(String word : wordList){
			addHashToSenInfo(senInfo, word.hashCode());
		}
		
		return senInfo;
	}
	
	private void addHashToSenInfo(SentenceInfo senInfo, int hash){
		if (hash % this.fingerprintSetting != 0) {
			return;
		}
		String hashToString = String.valueOf(hash);
		if (senInfo.termFreq.containsKey(hashToString)) {
			int value = senInfo.termFreq.get(hashToString);
			senInfo.termFreq.replace(hashToString, value + 1);
			return;
		}
		senInfo.termFreq.put(hashToString, 1);
	}
	
}
