package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;

public abstract class CalcSimScore {
	
	abstract double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2);
	
	public ArrayList<DocPair> getHighestScorePairs(ArrayList<Integer> docIDList, int scoreTableID){
		return DBManager.getInstance().getHighestPairs(docIDList, scoreTableID);
	}
	
	public boolean simCalcProcessorBatch(ArrayList<DocumentInfo> docInfoList, ArrayList<DocumentInfo> docInfoListForIntra, int invertedIndexTableID, int scoreTableID){
		if(docInfoListForIntra.size() != 0){
			if(!interCalcForIntraCalcSimSet(docInfoList, docInfoListForIntra, scoreTableID)){
				System.out.println("Intra support score 저장 실패.");
				return false;
			}
		}
		if (!intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Intra score 저장 실패.");
			return false;
		}
		
		return true;
	}
	
	public boolean simCalcProcessor(ArrayList<DocumentInfo> docInfoList, ArrayList<Integer> corpusDocIDArray, ArrayList<DocumentInfo> docInfoListForIntra, int invertedIndexTableID, int scoreTableID){
		if(docInfoListForIntra.size() != 0){
			if(!interCalcForIntraCalcSimSet(docInfoList, docInfoListForIntra, scoreTableID)){
				System.out.println("Intra support score 저장 실패.");
				return false;
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
	
	private boolean interCalcForIntraCalcSimSet(ArrayList<DocumentInfo> intraDocInfoListForInter1, ArrayList<DocumentInfo> intraDocInfoListForInter2, int scoreTableID){
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
					csvContent.delete(0,csvContent.length());
					
				}
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);
	}
	
	private boolean intraCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkScoreLimit();
		
		int bulkInsertLimitChecker = 0;
		for(int i=0; i<docInfoList.size(); i++){
			int docid1 = docInfoList.get(i).docID;
			for(int j=i+1; j<docInfoList.size(); ++j){
				int docid2 = docInfoList.get(j).docID;
				double simscore = calcSim(docInfoList.get(i).termFreq, docInfoList.get(j).termFreq);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit){
					if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID)){
						return false;
					}
					bulkInsertLimitChecker = 0;
					csvContent.delete(0,csvContent.length());
					
				}
				
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);

	}
	
	private boolean interCalcSimSet(ArrayList<DocumentInfo> docInfoList,  ArrayList<Integer> corpusDocIDArray, int scoreTableID, int invertedIndexTableID){
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
	
	private boolean interCalculateSegment(ArrayList<DocumentInfo> docInfoList, ArrayList<DocumentInfo> corpusDocInfoList, int scoreTableID, int invertedIndexTableID){
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
					csvContent.delete(0,csvContent.length());
				}
			}
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);
	}
	
}
