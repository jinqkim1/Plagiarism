package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
//import com.kdars.HotCheetos.DataImport.FileDataImport;
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
			
			//whitespace�� �ƴ� ĳ���ʹ� �ؽ��ڵ� ����.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
				lastIndexOfnonBlankCharacter = i;
				continue;
			}
			
			//�������� whitespace�� ��쿡�� �� ���� ���� Ž.
			if (hashCharSum == 0){
				continue;
			}
			
			//�� ����¥���� n-gram���� ��ġ�� �� ���� ���� Ž.
			if (i >= 2 && wholeChar[i-2] == ' '){
				nGramMaker.clear();
				hashCharSum = 0;
				continue;
			}
			
			//�ƹ� ��ó�� ���� ���¿��� �־��� �ܾ stopword list�� ���Ե� �ܾ��� �� ���� ���� Ž.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList�� parameter�� ���� n-gram ������ŭ�� hashcode�� ���� hashcode�� ���ؼ� hashmap �����, 0��° hashcode�� �������ν� ���� ngram ���� �غ�. 
			nGramMaker.add(hashCharSum);
			if (nGramMaker.size() == nGramSetting){
				int nGramHash = 0;
				for (int component : nGramMaker){
					nGramHash += component;
				}
				docInfo = addHash(docInfo, nGramHash);
				nGramMaker.remove(0);
			}
			
			//���������� ���� ngramComponent�� ������ ĳ���Ͱ� ��ħǥ�� ��쿡�� nGramMaker�� ���� ó������ �ٽ� ngram ����.
			if (wholeChar[lastIndexOfnonBlankCharacter] == '.'){
				nGramMaker.clear();
			}
			
			//whitespace�� detect�ǰ� �ѱ��� ¥�� �ܾ �ƴ϶��, ���ο� ngramComponent�� ����� ���� hashCharSum ������. 
			hashCharSum = 0;
			
		}
		
		return docInfo;
	}
	
	public DocumentInfo addHash(DocumentInfo docInfo, int hash) {
		if (hash % Configurations.getInstance().getFingerprintSetting() != 0) {
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
			
			//whitespace�� �ƴ� ĳ���ʹ� �ؽ��ڵ� ����.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
				lastIndexOfnonBlankCharacter = i;
				continue;
			}
			
			//�������� whitespace�� ��쿡�� �� ���� ���� Ž.
			if (hashCharSum == 0){
				continue;
			}
			
			//�� ����¥���� n-gram���� ��ġ�� �� ���� ���� Ž.
			if (i >= 2 && wholeChar[i-2] == ' '){
				nGramMaker.clear();
				hashCharSum = 0;
				continue;
			}
			
			//�ƹ� ��ó�� ���� ���¿��� �־��� �ܾ stopword list�� ���Ե� �ܾ��� �� ���� ���� Ž.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList�� parameter�� ���� n-gram ������ŭ�� hashcode�� ���� hashcode�� ���ؼ� hashmap �����, 0��° hashcode�� �������ν� ���� ngram ���� �غ�. 
			nGramMaker.add(hashCharSum);
			if (nGramMaker.size() == nGramSetting){
				int nGramHash = 0;
				for (int component : nGramMaker){
					nGramHash += component;
				}
				docInfo = addHash(docInfo, nGramHash);
				nGramMaker.remove(0);
			}
			
			//���������� ���� ngramComponent�� ������ ĳ���Ͱ� ��ħǥ�� ��쿡�� nGramMaker�� ���� ó������ �ٽ� ngram ����.
			if (wholeChar[lastIndexOfnonBlankCharacter] == '.'){
				nGramMaker.clear();
			}
			
			//whitespace�� detect�ǰ� �ѱ��� ¥�� �ܾ �ƴ϶��, ���ο� ngramComponent�� ����� ���� hashCharSum ������. 
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
