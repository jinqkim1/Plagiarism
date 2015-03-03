package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class NGram_hashcode_mapReduce{
	
	private ArrayList<Integer> stopwordHashList = new ArrayList<Integer>();
	private ArrayList<String> stopWordList = DBManager.getInstance().getStopwords();
	/*Temporary measure for experiment.  Need to delete!!!! */
	private int nGramSetting = Configuration.getInstance().getNgramSetting();
	private int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	/*Temporary measure for experiment.  Need to delete!!!! */
	
	public NGram_hashcode_mapReduce(){
		for (String stopword : stopWordList){
			stopwordHashList.add(stopword.hashCode());
		}
	}
	
	public DocumentInfo parseDoc(String content, int documentID, int invertedIndexTableID) {
		DocumentInfo docInfo = new DocumentInfo();
		docInfo.docID = documentID;
		
		char wholeChar[] = content.toCharArray();
		
		ArrayList<Integer> nGramMaker = new ArrayList<Integer>();
		int ngramMaker = 0;
		
//		int lastIndexOfnonBlankCharacter = 0;
		int hashCharSum = 0;
		for (int i = 0; i < wholeChar.length; i++){
			
			//whitespace�� �ƴ� ĳ���ʹ� �ؽ��ڵ� ����.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
//				lastIndexOfnonBlankCharacter = i;
				
				//������ ������ �ܾ� ó��.
				if (i != wholeChar.length - 1){
					continue;
				}
			}
			
			//�������� whitespace�� ��쿡�� �� ���� ���� Ž.
			if (hashCharSum == 0){
				continue;
			}
			
//			//�� ����¥���� n-gram���� ��ġ�� �� ���� ���� Ž.
//			if ((i >= 2 && wholeChar[i-2] == ' ') || i < 2){
//				nGramMaker.clear();
//				ngramMaker = 0;
//				hashCharSum = 0;
//				continue;
//			}
			
			//�ƹ� ��ó�� ���� ���¿��� �־��� �ܾ stopword list�� ���Ե� �ܾ��� �� ���� ���� Ž.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList�� parameter�� ���� n-gram ������ŭ�� hashcode�� ���� hashcode�� ���ؼ� hashmap �����, 0��° hashcode�� �������ν� ���� ngram ���� �غ�. 
			nGramMaker.add(hashCharSum);
			ngramMaker += hashCharSum;
			
//			//���������� ���� ngramComponent�� ������ ĳ���Ͱ� ��ħǥ�� ��쿡�� nGramMaker�� ���� ó������ �ٽ� ngram ����.
//			char period = '.';
//			if (wholeChar[lastIndexOfnonBlankCharacter] == '.'){
//				ngramMaker -= period;
//				if (nGramMaker.size() == this.nGramSetting){
//					docInfo = addHash(docInfo, ngramMaker);
//				}
//				nGramMaker.clear();
//				ngramMaker = 0;
//				hashCharSum = 0;
//				continue;
//			}
			
			//nGramMaker arrayList�� parameter�� ���� n-gram ������ŭ�� hashcode�� ���� hashcode�� ���ؼ� hashmap �����, 0��° hashcode�� �������ν� ���� ngram ���� �غ�.
			if (nGramMaker.size() == this.nGramSetting){
				addHash(docInfo, ngramMaker);
				ngramMaker -= nGramMaker.get(0);
				nGramMaker.remove(0);
			}
			
			//whitespace�� detect�ǰ� �ѱ��� ¥�� �ܾ �ƴ϶��, ���ο� ngramComponent�� ����� ���� hashCharSum ������. 
			hashCharSum = 0;
			
		}
		
		if(!DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID)){
			System.out.println("parse�� hashmap�� DB�� ���� ��������");
		}
		
		return docInfo;
	}
	
	private void addHash(DocumentInfo docInfo, int hash) {
		if (hash % this.fingerprintSetting != 0) {
			return;
		}
		String hashToString = String.valueOf(hash);
		if (docInfo.termFreq.containsKey(hashToString)) {
			int value = docInfo.termFreq.get(hashToString);
			docInfo.termFreq.put(hashToString, value + 1);
			return;
		}
		docInfo.termFreq.put(hashToString, 1);
	}
}
