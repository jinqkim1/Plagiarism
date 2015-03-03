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
			
			//whitespace가 아닌 캐릭터는 해쉬코드 더함.
			if (wholeChar[i] != ' '){
				hashCharSum += wholeChar[i];
//				lastIndexOfnonBlankCharacter = i;
				
				//문장의 마지막 단어 처리.
				if (i != wholeChar.length - 1){
					continue;
				}
			}
			
			//연속으로 whitespace일 경우에는 그 다음 포문 탐.
			if (hashCharSum == 0){
				continue;
			}
			
//			//한 글자짜리는 n-gram으로 안치고 그 다음 포문 탐.
//			if ((i >= 2 && wholeChar[i-2] == ' ') || i < 2){
//				nGramMaker.clear();
//				ngramMaker = 0;
//				hashCharSum = 0;
//				continue;
//			}
			
			//아무 전처리 없는 상태에서 주어진 단어가 stopword list에 포함된 단어라면 그 다음 포문 탐.
			if (stopwordHashList.contains(hashCharSum)){
				hashCharSum = 0;
				continue;
			}
			
			//nGramMaker arrayList에 parameter로 받은 n-gram 갯수만큼의 hashcode가 차면 hashcode를 더해서 hashmap 만들고, 0번째 hashcode를 버림으로써 다음 ngram 만들 준비. 
			nGramMaker.add(hashCharSum);
			ngramMaker += hashCharSum;
			
//			//마지막으로 더한 ngramComponent의 마지막 캐릭터가 마침표일 경우에는 nGramMaker를 비우고 처음부터 다시 ngram 만듬.
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
			
			//nGramMaker arrayList에 parameter로 받은 n-gram 갯수만큼의 hashcode가 차면 hashcode를 더해서 hashmap 만들고, 0번째 hashcode를 버림으로써 다음 ngram 만들 준비.
			if (nGramMaker.size() == this.nGramSetting){
				addHash(docInfo, ngramMaker);
				ngramMaker -= nGramMaker.get(0);
				nGramMaker.remove(0);
			}
			
			//whitespace가 detect되고 한글자 짜리 단어가 아니라면, 새로운 ngramComponent를 만들기 위해 hashCharSum 리셋함. 
			hashCharSum = 0;
			
		}
		
		if(!DBManager.getInstance().insertBulkToHashTable(docInfo, invertedIndexTableID)){
			System.out.println("parse된 hashmap을 DB에 저장 실패했음");
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
