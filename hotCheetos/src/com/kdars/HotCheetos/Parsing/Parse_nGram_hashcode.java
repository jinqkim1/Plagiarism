package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_nGram_hashcode implements Parse{
	
	private static  Parse_nGram_hashcode parse_nGram_hashcode = new Parse_nGram_hashcode();
	public static Parse_nGram_hashcode getInstance(){
		return	parse_nGram_hashcode;
	}
	
	
	
	private ArrayList<Integer> stopwordHashList = new ArrayList<Integer>();
	
	public Parse_nGram_hashcode(){
		for (String stopword : stopWordList){
			stopwordHashList.add(stopword.hashCode());
		}
	}
	
	@Override
	public DocumentInfo parseDoc(String content, int documentID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;
		
		char wholeChar[] = content.toCharArray();
		
		ArrayList<Integer> nGramMaker = new ArrayList<Integer>();
		
		int lastIndexOfnonBlankCharacter = 0;
		int hashCharSum = 0;
		for (int i = 0; i < wholeChar.length; i++){
			
			//whitespace가 아닌 캐릭터는 해쉬코드 더함.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
				lastIndexOfnonBlankCharacter = i;
				continue;
			}
			
			//연속으로 whitespace일 경우에는 그 다음 포문 탐.
			if (hashCharSum == 0){
				continue;
			}
			
			//한 글자짜리는 n-gram으로 안치고 그 다음 포문 탐.
			if (i >= 2 && wholeChar[i-2] == ' '){
				nGramMaker.clear();
				hashCharSum = 0;
				continue;
			}
			
			//아무 전처리 없는 상태에서 주어진 단어가 stopword list에 포함된 단어라면 그 다음 포문 탐.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList에 parameter로 받은 n-gram 갯수만큼의 hashcode가 차면 hashcode를 더해서 hashmap 만들고, 0번째 hashcode를 버림으로써 다음 ngram 만들 준비. 
			nGramMaker.add(hashCharSum);
			if (nGramMaker.size() == nGramSetting){
				int nGramHash = 0;
				for (int component : nGramMaker){
					nGramHash += component;
				}
				docInfo = addHash(docInfo, nGramHash);
				nGramMaker.remove(0);
			}
			
			//마지막으로 더한 ngramComponent의 마지막 캐릭터가 마침표일 경우에는 nGramMaker를 비우고 처음부터 다시 ngram 만듬.
			if (wholeChar[lastIndexOfnonBlankCharacter] == '.'){
				nGramMaker.clear();
			}
			
			//whitespace가 detect되고 한글자 짜리 단어가 아니라면, 새로운 ngramComponent를 만들기 위해 hashCharSum 리셋함. 
			hashCharSum = 0;
			
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

	public ArrayList<DocumentInfo> parseDocSetWithDocumentInfoArray(ArrayList<DocumentInfo> textMap) {
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		
		for(int i=0; i<textMap.size(); ++i){
			docInfoSet.add(parseDoc(textMap.get(i).contents, textMap.get(i).docID));
		}
		
		return docInfoSet;
	}
	
	@Override
	public ArrayList<DocumentInfo> parseDocSetWithDocIDArray(ArrayList<Integer> docIDs) {
		
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		for (int docid : docIDs){
			String content = DBManager.getInstance().getText(docid);
			docInfoSet.add(parseDoc(content, docid));
		}
		
		return docInfoSet;
	}
	
	public boolean parseDocJINKYUWithTableName(String content, int documentID, String tableName) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;
		
		char wholeChar[] = content.toCharArray();
		
		ArrayList<Integer> nGramMaker = new ArrayList<Integer>();
		
		int lastIndexOfnonBlankCharacter = 0;
		int hashCharSum = 0;
		for (int i = 0; i < wholeChar.length; i++){
			
			//whitespace가 아닌 캐릭터는 해쉬코드 더함.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
				lastIndexOfnonBlankCharacter = i;
				continue;
			}
			
			//연속으로 whitespace일 경우에는 그 다음 포문 탐.
			if (hashCharSum == 0){
				continue;
			}
			
			//한 글자짜리는 n-gram으로 안치고 그 다음 포문 탐.
			if (i >= 2 && wholeChar[i-2] == ' '){
				nGramMaker.clear();
				hashCharSum = 0;
				continue;
			}
			
			//아무 전처리 없는 상태에서 주어진 단어가 stopword list에 포함된 단어라면 그 다음 포문 탐.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList에 parameter로 받은 n-gram 갯수만큼의 hashcode가 차면 hashcode를 더해서 hashmap 만들고, 0번째 hashcode를 버림으로써 다음 ngram 만들 준비. 
			nGramMaker.add(hashCharSum);
			if (nGramMaker.size() == nGramSetting){
				int nGramHash = 0;
				for (int component : nGramMaker){
					nGramHash += component;
				}
				docInfo = addHash(docInfo, nGramHash);
				nGramMaker.remove(0);
			}
			
			//마지막으로 더한 ngramComponent의 마지막 캐릭터가 마침표일 경우에는 nGramMaker를 비우고 처음부터 다시 ngram 만듬.
			if (wholeChar[lastIndexOfnonBlankCharacter] == '.'){
				nGramMaker.clear();
			}
			
			//whitespace가 detect되고 한글자 짜리 단어가 아니라면, 새로운 ngramComponent를 만들기 위해 hashCharSum 리셋함. 
			hashCharSum = 0;
			
		}

		return DBManager.getInstance().insertBulkToHashTableWithTableName(docInfo,tableName);
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
