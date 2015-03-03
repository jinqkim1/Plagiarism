package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_noun_hashcode implements Parse {
	private static Parse_noun_hashcode parse_noun_hashcode = new Parse_noun_hashcode();

	public static Parse_noun_hashcode getInstance() {
		return parse_noun_hashcode;
	}

	private String postFix1 = Configuration.getInstance().getPostFix1();
	private String postFix2 = Configuration.getInstance().getPostFix2();

	@Override
	public DocumentInfo parseDoc(String content, int documentID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				docInfo = addHash(docInfo, word.hashCode());
			}
		}
		return docInfo;
	}

	public DocumentInfo addHash(DocumentInfo docInfo, int hash) {
		if (hash % Configuration.getInstance().getFingerprintSetting() != 0) {
			return docInfo;
		}
		String hashToString = String.valueOf(hash);
		if (docInfo.termFreq.containsKey(hashToString)) {
			int value = docInfo.termFreq.get(hashToString);
			docInfo.termFreq.put(hashToString, value + 1);
			return docInfo;
		}
		docInfo.termFreq.put(hashToString, 1);
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

		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();

		Set<Integer> set = textMap.keySet();
		Iterator<Integer> iter = set.iterator();

		while (iter.hasNext()) {
			int docID = iter.next();
			docInfoSet.add(parseDoc(textMap.get(docID), docID));
		}

		return docInfoSet;
	}

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
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;

		String wordList[] = content.trim().split("\\s+");

		for (int i = 0; i < wordList.length; i++) {
			int postFixChecker = wordList[i].length();
			String word = deletePostFix(wordList[i]);
			if (word.length() != postFixChecker) {
				docInfo = addHash(docInfo, word.hashCode());
			}
		}
		return DBManager.getInstance().insertBulkToHashTableWithTableName(docInfo, tableName);
	}
	
	public boolean parseDocSetWithDocIDArrayJINKYUWithTableName(ArrayList<Integer> docIDs, String tableName) {
		boolean jobCompleteChecker = true;
		
		for (int docid : docIDs){
			String content = DBManager.getInstance().getText(docid);
			if (!parseDocJINKYUWithTableName(content, docid, tableName)){
				jobCompleteChecker = false;
			}
		}
		
		return jobCompleteChecker;
	}
	
	public ArrayList<DocumentInfo> getParsedDocsWithTableName(ArrayList<Integer> docIDs, String tableName){
		return DBManager.getInstance().getDocInfoArrayWithTableName(docIDs, tableName);
	}
	

}
