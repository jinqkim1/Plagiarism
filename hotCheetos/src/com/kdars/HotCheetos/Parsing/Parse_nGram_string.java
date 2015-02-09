package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_nGram_string implements Parse {
	private static  Parse_nGram_string parse_nGram_string = new Parse_nGram_string();
	public static Parse_nGram_string getInstance(){
		return	parse_nGram_string;
	}
	@Override
	public DocumentInfo parseDoc(String content, int documentID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			if(i+Configuration.getInstance().getNgramSetting() >wordList.length){
				break;
			}
			String key = wordList[i]+wordList[i+1]+wordList[i+2];
			
			if(key.hashCode()%Configuration.getInstance().mod==0){
				if(docInfo.termFreq.containsKey(key)){
					int value = docInfo.termFreq.get(key);
					//docInfo.termFreq.put(key, value+1);
				}else{
					//docInfo.termFreq.put(key, 1);	
				}
			}
		}
		return docInfo;
	}
	@Override
	public ArrayList<DocumentInfo> parseDocSet(HashMap<Integer, String> textMap) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocIDArray(ArrayList<Integer> docIDs) {
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		for (int docid : docIDs){
			//System.out.println(docid);
			String content = DBManager.getInstance().getText(docid);
			docInfoSet.add(parseDoc(content, docid));
		}
		
		return docInfoSet;
	}
	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocumentInfoArray(ArrayList<DocumentInfo> textMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean parseDocJINKYUWithTableName(String content, int documentID, String tableName) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			if(i+Configuration.getInstance().getNgramSetting() >wordList.length){
				break;
			}
			String key = wordList[i]+wordList[i+1]+wordList[i+2];
			
			if(key.hashCode()%Configuration.getInstance().mod==0){
				if(docInfo.termFreq.containsKey(key)){
					int value = docInfo.termFreq.get(key);
					docInfo.termFreq.put(key, value+1);
				}else{
					docInfo.termFreq.put(key, 1);	
				}
			}
		}
		return DBManager.getInstance().insertBulkToHashTableWithStringTableName(docInfo, tableName);
	}
	
	public boolean parseDocSetWithDocIDArrayJINKYUWithStringTableName(ArrayList<Integer> docIDs, String tableName) {
		boolean jobCompleteChecker = true;
		
		for (int docid : docIDs){
			String content = DBManager.getInstance().getText(docid);
			if (!parseDocJINKYUWithTableName(content, docid, tableName)){
				jobCompleteChecker = false;
			}
		}
		
		return jobCompleteChecker;
	}
	
	public ArrayList<DocumentInfo> getParsedDocsWithStringTableName(ArrayList<Integer> docIDs, String tableName){
		return DBManager.getInstance().getDocInfoArrayWithStringTableName(docIDs, tableName);
	}
	
}
