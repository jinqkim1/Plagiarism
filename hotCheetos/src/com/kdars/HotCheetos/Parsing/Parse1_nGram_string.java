package com.kdars.HotCheetos.Parsing;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse1_nGram_string extends Parse1{

	@Override
	boolean parseDoc(String content, int documentID, int invertedIndexTableID) {

		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			if(i+Configuration.getInstance().getNgramSetting() >wordList.length){
				break;
			}
			
			String key = wordList[i]+wordList[i+1]+wordList[i+2];
			
			docInfo = addHash(docInfo, key);

		}
		
		return DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID);
	
	}
	
	private DocumentInfo addHash(DocumentInfo docInfo, String term) {
		if (term.hashCode() % Configuration.getInstance().getFingerprintSetting() != 0) {
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
