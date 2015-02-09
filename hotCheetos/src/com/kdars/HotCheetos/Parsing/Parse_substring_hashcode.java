package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_substring_hashcode implements Parse{
	
	
	public int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	
	
	private static  Parse_substring_hashcode parse_substring_hashcode = new Parse_substring_hashcode();
	public static Parse_substring_hashcode getInstance(){
		return	parse_substring_hashcode;
	}
	
	@Override
	public DocumentInfo parseDoc(String content, int documentID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;
		
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
	public ArrayList<DocumentInfo> parseDocSet(HashMap<Integer,String> textMap) {
		
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		Set<Integer> set = textMap.keySet();
		Iterator<Integer> iter = set.iterator();
		
		while(iter.hasNext()){
			int docID = iter.next();
			docInfoSet.add(parseDoc(textMap.get(docID), docID));
		}
		
		return docInfoSet;
	}

	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocIDArray(ArrayList<Integer> docIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocumentInfoArray(
			ArrayList<DocumentInfo> textMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
