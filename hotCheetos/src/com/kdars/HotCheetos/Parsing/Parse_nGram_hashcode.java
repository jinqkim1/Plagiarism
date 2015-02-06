package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class Parse_nGram_hashcode implements Parse{

	@Override
	public DocumentInfo parseDoc(String content) {
		DocumentInfo docInfo = new DocumentInfo();
		
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
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<String> contentSet) {
		
		ArrayList<DocumentInfo> docInfoSet = new ArrayList<DocumentInfo>();
		
		for (String content : contentSet){
			docInfoSet.add(parseDoc(content));
		}
		
		
		return docInfoSet;
	}

}
