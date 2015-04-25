package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class NGram_hashcode_mapReduce{
	
	private ArrayList<Integer> stopwordHashList = new ArrayList<Integer>();
//	private ArrayList<String> stopWordList = DBManager.getInstance().getStopwords();
	/*Temporary measure for experiment.  Need to delete!!!! */
	private int nGramSetting = Configurations.getInstance().getNgramSetting();
	private int fingerprintSetting = Configurations.getInstance().getFingerprintSetting();
	/*Temporary measure for experiment.  Need to delete!!!! */
	
	public NGram_hashcode_mapReduce(){
//		for (String stopword : stopWordList){
//			stopwordHashList.add(stopword.hashCode());
//		}
		stopwordHashList.add(0);
	}
	
	public MapWritable parseDoc(String content, int documentID, int invertedIndexTableID) {
		MapWritable termFreqMap = new MapWritable();
		
		char wholeChar[] = content.toCharArray();
		
		ArrayList<Integer> nGramMaker = new ArrayList<Integer>();
		int ngramMaker = 0;
		
		//��ħǥ�� �پ��ִ� �ܾ��� ���, 2���ڱ����� ����. ��ħǥ�� �پ����� �ʴٸ� 1���ڱ����� ����.
		int validTermChecker = 0; //�ܾ� ���� ����.
		boolean periodChecker = false;
		
//		int lastIndexOfnonBlankCharacter = 0;
		int hashCharSum = 0;
		for (int i = 0; i < wholeChar.length; i++){
			
			//whitespace�� �ƴ� ĳ���ʹ� �ؽ��ڵ� ����.
			if (wholeChar[i] != ' '){
				
				validTermChecker++;
				
				hashCharSum += wholeChar[i];
//				lastIndexOfnonBlankCharacter = i;
				
				if (wholeChar[i] == '.'){
					periodChecker = true;
				}
				
				//������ ������ �ܾ� ó��.
				if (i != wholeChar.length - 1){
					continue;
				}
			}
			
			//�������� whitespace�� ��쿡�� �� ���� ���� Ž.
			if (hashCharSum == 0){
				continue;
			}
			
			//��ħǥ�� �پ��ִ� �ܾ��� ���, 2���ڱ����� ����. ��ħǥ�� �پ����� �ʴٸ� 1���ڱ����� ����.
			if (periodChecker && validTermChecker <= 2){
				periodChecker = false;
				validTermChecker = 0;
				continue;
			}else if ((!periodChecker) && validTermChecker < 2){
				validTermChecker = 0;
				continue;
			}
			
//			//�� ����¥���� n-gram���� ��ġ�� �� ���� ���� Ž.
//			if ((i >= 2 && wholeChar[i-2] == ' ') || i < 2){
//				nGramMaker.clear();
//				ngramMaker = 0;
//				hashCharSum = 0;
//				continue;
//			}
			
			//�ƹ� ��ó�� ��� ���¿��� �־��� �ܾ stopword list�� ���Ե� �ܾ��� �� ���� ���� Ž.
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
				addHash(termFreqMap, ngramMaker);
				ngramMaker -= nGramMaker.get(0);
				nGramMaker.remove(0);
			}
			
			//whitespace�� detect�ǰ� �ѱ��� ¥�� �ܾ �ƴ϶��, ���ο� ngramComponent�� ����� ���� hashCharSum ������. 
			hashCharSum = 0;
			// �ܾ� ���� üĿ 0���� ����. ��ħǥ ���� Ȯ�� boolean ����.
			validTermChecker = 0;
			periodChecker = false;
		}

//		DBManager.getInstance().insertBulkToHashTable_MapReduce(documentID, termFreqMap, invertedIndexTableID);
		
		return termFreqMap;
	}
	
	private void addHash(MapWritable termFreqMap, int hash) {
		if (hash % this.fingerprintSetting != 0) {
			return;
		}
		Text hashToText = new Text();
		hashToText.set(String.valueOf(hash));
		
		IntWritable value = new IntWritable();
		if (termFreqMap.containsKey(hashToText)) {
			value = (IntWritable) termFreqMap.get(hashToText);
			value.set(value.get() + 1);
			termFreqMap.put(hashToText, value);
			return;
		}
		value.set(1);
		termFreqMap.put(hashToText, value);
	}
}
