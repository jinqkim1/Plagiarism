package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_substring_hashcode implements Parse{

	@Override
	public DocumentInfo parseDoc(String content) {
		DocumentInfo docInfo = new DocumentInfo();
		
		char wholeChar[] = content.toCharArray();
		int hashCharSum = 0;
		for (int i = 0; i < wholeChar.length; i++){
			
			hashCharSum += wholeChar[i];
			
			if ( i < substringSetting - 1){
				continue;
			}else if(i == substringSetting -1){
				docInfo = addHash(docInfo, hashCharSum);
				continue;
			}
						
			hashCharSum -= wholeChar[i-substringSetting];
			docInfo = addHash(docInfo, hashCharSum);
			
		}
		
		return docInfo;
		
	}
	
	public DocumentInfo addHash(DocumentInfo docInfo, int hash) {
		if (hash % fingerprintSetting != 0) {
			return docInfo;
		}
		String hashToString = String.valueOf(hash);
		if (docInfo.termFreq.containsKey(hashToString)) {
			int value = docInfo.termFreq.get(hashToString);
			docInfo.termFreq.replace(hashToString, value + 1);
			return docInfo;
		}
		docInfo.termFreq.put(hashToString, 1);
		return docInfo;
	}
	
	@Override
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<String> contentSet) {
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();

		for (String content : contentSet) {
			docInfoSet.add(parseDoc(content));
		}

		return docInfoSet;
	}

}
