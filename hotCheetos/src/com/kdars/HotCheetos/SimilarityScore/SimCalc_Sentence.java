package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.DocumentStructure.SentenceInfo;

public class SimCalc_Sentence extends CalcSimScore{
	
	private double sentenceSimScoreLimit = 0.9;
	
	@Override
	double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2) {
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;

		doc1 = new HashMap<String, Integer>(doc1);
		doc2 = new HashMap<String, Integer>(doc2);
		Iterator iter1 = doc1.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry pair1 = (Map.Entry) iter1.next();
			String key = pair1.getKey().toString();
			double value1 = Double.valueOf(pair1.getValue().toString());

			if (doc2.containsKey(key)) {
				double value2 = (double) doc2.get(key);
				multiply += (value1 * value2);
				norm2 += (value2 * value2);
				doc2.remove(key);

			}
			norm1 += (value1 * value1);
			iter1.remove();
		}

		Iterator iter2 = doc2.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry pair2 = (Map.Entry) iter2.next();
			double value2 = Double.valueOf(pair2.getValue().toString());
			norm2 += (value2 * value2);
			iter2.remove();
		}

		double result = multiply / Math.sqrt(norm1 * norm2);
		if (Double.isNaN(result)) {
			result = 0;
		}

		return result;
	}
	
	private ArrayList<Double> calcSim_Sentence(HashMap<Integer, SentenceInfo> doc1, HashMap<Integer, SentenceInfo> doc2) {
		
		ArrayList<Double> doc1doc2SimScores = new ArrayList<Double>();
		
		ArrayList<Integer> intersectingSentencesFromDoc1 = new ArrayList<Integer>();
		ArrayList<Integer> intersectingSentencesFromDoc2 = new ArrayList<Integer>();
		
		doc1 = new HashMap<Integer, SentenceInfo>(doc1);
		
		Iterator it1 = doc1.entrySet().iterator();
		while(it1.hasNext()){
			Map.Entry pair1 = (Map.Entry) it1.next();
			SentenceInfo senInfo1 = (SentenceInfo) pair1.getValue();
			int sentenceID1 = senInfo1.sentenceID;
			HashMap<String, Integer> senInfoMap1 = senInfo1.termFreq;
			
			doc2 = new HashMap<Integer, SentenceInfo>(doc2);
			
			Iterator it2 = doc2.entrySet().iterator();
			while(it2.hasNext()){
				Map.Entry pair2 = (Map.Entry) it2.next();
				SentenceInfo senInfo2 = (SentenceInfo) pair2.getValue();
				int sentenceID2 = senInfo2.sentenceID;
				
				if(calcSim(senInfoMap1, senInfo2.termFreq) >= sentenceSimScoreLimit){
					if(!intersectingSentencesFromDoc1.contains(sentenceID1)){
						intersectingSentencesFromDoc1.add(sentenceID1);
					}
					
					if(!intersectingSentencesFromDoc2.contains(sentenceID2)){
						intersectingSentencesFromDoc2.add(sentenceID2);
					}
				}
				
				it2.remove();
			}
			
			it1.remove();
		}
		
		double doc1Score = (double)intersectingSentencesFromDoc1.size() / (double)doc1.size();
		double doc2Score = (double)intersectingSentencesFromDoc2.size() / (double)doc2.size();
		
		doc1doc2SimScores.add(doc1Score);
		doc1doc2SimScores.add(doc2Score);
		
		return doc1doc2SimScores;
	}
	
	@Override
	boolean interCalcForIntraCalcSimSet(ArrayList<DocumentInfo> intraDocInfoListForInter1, ArrayList<DocumentInfo> intraDocInfoListForInter2, int scoreTableID) {

		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkScoreLimit() / 2;
		
		int bulkInsertLimitChecker = 0;
		for(DocumentInfo docInfo1 : intraDocInfoListForInter1){
			int docid1 = docInfo1.docID;
			for (DocumentInfo docInfo2 : intraDocInfoListForInter2){
				int docid2 = docInfo2.docID;
				ArrayList<Double> simscore = calcSim_Sentence(docInfo1.sentenceInfoMap, docInfo2.sentenceInfoMap);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore.get(0))+"\n");
				csvContent.append(String.valueOf(docid2)+","+String.valueOf(docid1)+","+String.valueOf(simscore.get(1))+"\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit){
					if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID)){
						return false;
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
					
				}
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);
	
	}

	@Override
	boolean intraCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID) {
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkScoreLimit();
		
		int bulkInsertLimitChecker = 0;
		for(int i=0; i<docInfoList.size(); i++){
			int docid1 = docInfoList.get(i).docID;
			HashMap<Integer, SentenceInfo> doc1 = docInfoList.get(i).sentenceInfoMap;
			for(int j=i+1; j<docInfoList.size(); ++j){
				int docid2 = docInfoList.get(j).docID;
				
				ArrayList<Double> simscore = calcSim_Sentence(doc1, docInfoList.get(j).sentenceInfoMap);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore.get(0))+"\n");
				csvContent.append(String.valueOf(docid2)+","+String.valueOf(docid1)+","+String.valueOf(simscore.get(1))+"\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit){
					if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID)){
						return false;
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
					
				}
				
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);

	}

	@Override
	boolean interCalculateSegment(ArrayList<DocumentInfo> docInfoList, ArrayList<DocumentInfo> corpusDocInfoList, int scoreTableID, int invertedIndexTableID) {
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		for (DocumentInfo docInfo1 : docInfoList){
			int docid1 = docInfo1.docID;
			for (DocumentInfo docInfo2 : corpusDocInfoList){
				int docid2 = docInfo2.docID;
				ArrayList<Double> simscore = calcSim_Sentence(docInfo1.sentenceInfoMap, docInfo2.sentenceInfoMap);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore.get(0))+"\n");
				csvContent.append(String.valueOf(docid2)+","+String.valueOf(docid1)+","+String.valueOf(simscore.get(1))+"\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit){
					if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID)){
						return false;
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
				}
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);
	}

}
