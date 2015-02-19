package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class CosineSim extends CalcSimScore {
	
	@Override
	double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2) {
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		doc1 = new HashMap<String, Integer>(doc1);
		doc2 = new HashMap<String, Integer>(doc2);
		Iterator iter1 = doc1.entrySet().iterator();
		while(iter1.hasNext()){
			Map.Entry pair1 = (Map.Entry)iter1.next();
			String key = pair1.getKey().toString();
			double value1 = Double.valueOf(pair1.getValue().toString());
			
			if(doc2.containsKey(key)){
				double value2 = (double)doc2.get(key);
				multiply += (value1 * value2);
				norm2 += (value2 * value2);
				doc2.remove(key);
				
			}
			norm1 += (value1 * value1);
			iter1.remove();
		}
		
		Iterator iter2 = doc2.entrySet().iterator();
		while(iter2.hasNext()){
			Map.Entry pair2 = (Map.Entry)iter2.next();
			double value2 = Double.valueOf(pair2.getValue().toString());
			norm2 += (value2 * value2);
			iter2.remove();
		}
		
		double result =  multiply / Math.sqrt(norm1 * norm2);
		if(Double.isNaN(result)){
			result=0;
		}
		
		return result;
	}

	@Override
	boolean interCalcForIntraCalcSimSet(ArrayList<DocumentInfo> intraDocInfoListForInter1, ArrayList<DocumentInfo> intraDocInfoListForInter2, int scoreTableID) {

		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkScoreLimit();
		
		int bulkInsertLimitChecker = 0;
		for(DocumentInfo docInfo1 : intraDocInfoListForInter1){
			int docid1 = docInfo1.docID;
			for (DocumentInfo docInfo2 : intraDocInfoListForInter2){
				int docid2 = docInfo2.docID;
				double simscore = calcSim(docInfo1.termFreq, docInfo2.termFreq);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
				
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
			HashMap<String, Integer> doc1 = docInfoList.get(i).termFreq;
			for(int j=i+1; j<docInfoList.size(); ++j){
				int docid2 = docInfoList.get(j).docID;				
				double simscore = calcSim(doc1, docInfoList.get(j).termFreq);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
				
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
				double simscore = calcSim(docInfo1.termFreq, docInfo2.termFreq);
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
				
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
	public boolean simCalcProcessorBatch(ArrayList<DocumentInfo> docInfoList, ArrayList<ArrayList<Integer>> docIDListListForIntraInterCalc, int invertedIndexTableID, int scoreTableID){
		if(docIDListListForIntraInterCalc.size() != 0){
			for(ArrayList<Integer> docIDListForInterInIntra : docIDListListForIntraInterCalc){
				ArrayList<DocumentInfo> docInfoListForInterInIntra = DBManager.getInstance().getMultipleDocInfoArray(docIDListForInterInIntra, invertedIndexTableID);
				if(!interCalcForIntraCalcSimSet(docInfoList, docInfoListForInterInIntra, scoreTableID)){
					System.out.println("Intra support score 저장 실패.");
					return false;
				}
			}
		}
		
		if (!intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Intra score 저장 실패.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean simCalcProcessor(ArrayList<DocumentInfo> docInfoList, ArrayList<Integer> corpusDocIDArray, ArrayList<ArrayList<Integer>> docIDListListForIntraInterCalc, int invertedIndexTableID, int scoreTableID){
		if(docIDListListForIntraInterCalc.size() != 0){
			for(ArrayList<Integer> docIDListForInterInIntra : docIDListListForIntraInterCalc){
				ArrayList<DocumentInfo> docInfoListForInterInIntra = DBManager.getInstance().getMultipleDocInfoArray(docIDListForInterInIntra, invertedIndexTableID);
				if(!interCalcForIntraCalcSimSet(docInfoList, docInfoListForInterInIntra, scoreTableID)){
					System.out.println("Intra support score 저장 실패.");
					return false;
				}
			}
		}
		
		if (!interCalcSimSet(docInfoList, corpusDocIDArray, scoreTableID, invertedIndexTableID)){
			System.out.println("Inter score 저장 실패.");
			return false;
		}
		if (!intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Intra score 저장 실패.");
			return false;
		}
		
		return true;
	}
	
	@Override
	boolean interCalcSimSet(ArrayList<DocumentInfo> docInfoList,  ArrayList<Integer> corpusDocIDArray, int scoreTableID, int invertedIndexTableID){
		int docInfoMemoryLimit = Configuration.getInstance().getDocInfoListLimit();
		while(!corpusDocIDArray.isEmpty()){
			if(corpusDocIDArray.size() <= docInfoMemoryLimit){
				ArrayList<DocumentInfo> corpusDocInfoList = DBManager.getInstance().getMultipleDocInfoArray(corpusDocIDArray, invertedIndexTableID);
				if (!interCalculateSegment(docInfoList, corpusDocInfoList, scoreTableID, invertedIndexTableID)){
					return false;
				}
				corpusDocIDArray.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(corpusDocIDArray.subList(0, docInfoMemoryLimit - 1));
			ArrayList<DocumentInfo> corpusDocInfoList = DBManager.getInstance().getMultipleDocInfoArray(segmentedDocIDList, invertedIndexTableID);
			if (!interCalculateSegment(docInfoList, corpusDocInfoList, scoreTableID, invertedIndexTableID)){
				return false;
			}
			corpusDocIDArray = new ArrayList<Integer>(corpusDocIDArray.subList(docInfoMemoryLimit, corpusDocIDArray.size() - 1));
		}
		
		return true;

	}

}
