package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_noun_string implements Parse {
	private String postFix1 = Configuration.getInstance().getPostFix1();
	private String postFix2 = Configuration.getInstance().getPostFix2();
	private static Parse_noun_string parse_noun_string = new Parse_noun_string();

	public static Parse_noun_string getInstance() {
		return parse_noun_string;
	}

	@Override
	public DocumentInfo parseDoc(String content, int documentID) {
		int mod = Configuration.getInstance().getFingerprintSetting();
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			
			
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				if(word.hashCode()%mod==0){
					if(docInfo.termFreq.containsKey(word)){
						int value = docInfo.termFreq.get(word);
						//docInfo.termFreq.put(key, value+1);
					}else{
						//docInfo.termFreq.put(key, 1);	
					}
				}
			}			
		}
		return docInfo;
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


	@Override
	public ArrayList<DocumentInfo> parseDocSet(HashMap<Integer, String> textMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocIDArray(ArrayList<Integer> docIDs) {
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();

		for (int docid : docIDs) {
			String content = DBManager.getInstance().getText(docid);
			docInfoSet.add(parseDoc(content, docid));
		}

		return docInfoSet;
	}

	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocumentInfoArray(
			ArrayList<DocumentInfo> textMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean parseDocJINKYUWithTableName(String content, int documentID, String tableName) {
		int mod = Configuration.getInstance().getFingerprintSetting();
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			
			
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				if(word.hashCode()%mod==0){
					if(docInfo.termFreq.containsKey(word)){
						int value = docInfo.termFreq.get(word);
						docInfo.termFreq.put(word, value+1);
					}else{
						docInfo.termFreq.put(word, 1);	
					}
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
