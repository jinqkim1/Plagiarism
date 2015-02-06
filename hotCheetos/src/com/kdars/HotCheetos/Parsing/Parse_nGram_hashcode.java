package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_nGram_hashcode implements Parse{
	
	private static  Parse_nGram_hashcode parse_nGram_hashcode = new Parse_nGram_hashcode();
	public static Parse_nGram_hashcode getInstance(){
		return	parse_nGram_hashcode;
	}
	
	
	
	private ArrayList<Integer> stopwordHashList;
	
	public Parse_nGram_hashcode(){
		for (String stopword : stopWordList){
			stopwordHashList.add(stopword.hashCode());
		}
	}
	
	@Override
	public DocumentInfo parseDoc(int documentID) {
		String content = DBManager.getInstance().getText(documentID);
		
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
			if (wholeChar[i-2] == ' '){
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
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<Integer> docIDSet) {
		
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		for (int docID : docIDSet){
			docInfoSet.add(parseDoc(docID));
		}
		
		return docInfoSet;
	}

}