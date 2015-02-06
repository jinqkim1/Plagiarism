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
			
			//white space가 없는 상태의 text에 대해서 이런 방식을 사용하는게 보통. 하지만 white space가 있는 text여도 어느 정도 맞출 수 있을 것으로 예상.
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
