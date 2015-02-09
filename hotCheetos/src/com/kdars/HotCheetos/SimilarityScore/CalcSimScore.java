package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;

public abstract class CalcSimScore {
	
	abstract double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2);
	
	public boolean intraCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		
		int bulkInsertLimitChecker = 0;
		for(int i=0; i<docInfoList.size(); i++){
			for(int j=i+1; j<docInfoList.size(); ++j){
				
				int docid1 = docInfoList.get(i).docID;
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
	
	public boolean interCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID, int invertedIndexTableID){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		
		int bulkInsertLimitChecker = 0;
		
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		for(DocumentInfo docInfo1 : docInfoList){
			int docid1 = docInfo1.docID;
			for(int docid2 : corpusDocIDArray){
				if(docid1 != docid2){
					DocumentInfo docInfo2 = DBManager.getInstance().getDocInfoArray(docid2, invertedIndexTableID);
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
			
		}
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);

	}
	
	public ArrayList<DocPair> getHighestScorePairs(ArrayList<Integer> docIDList, int scoreTableID){
		return DBManager.getInstance().getHighestPairs(docIDList, scoreTableID);
	}
	
}
