package com.kdars.HotCheetos.Parsing;

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
			
			if(ngramChecker != this.nGramSetting){
				
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
			
			docInfo = addHash(docInfo, ngramMaker.toString());
			ngramCheckIndex = wordList[i- nGramSetting + 1].length();
			
		}
		
		return DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID);
	
	}
	
	private DocumentInfo addHash(DocumentInfo docInfo, String term) {
		if (term.hashCode() % this.fingerprintSetting != 0) {
			return docInfo;
		}
		if (docInfo.termFreq.containsKey(term)) {
			int value = docInfo.termFreq.get(term);
			docInfo.termFreq.replace(term, value + 1);
			return docInfo;
		}
		docInfo.termFreq.put(term, 1);
		return docInfo;
	}
	
}
