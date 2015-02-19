package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;

public abstract class CalcSimScore {
	
	abstract double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2);
	abstract boolean interCalcForIntraCalcSimSet(ArrayList<DocumentInfo> intraDocInfoListForInter1, ArrayList<DocumentInfo> intraDocInfoListForInter2, int scoreTableID);
	abstract boolean intraCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID);
	abstract boolean interCalculateSegment(ArrayList<DocumentInfo> docInfoList, ArrayList<DocumentInfo> corpusDocInfoList, int scoreTableID, int invertedIndexTableID);
	
	public ArrayList<DocPair> getHighestScorePairs(ArrayList<Integer> docIDList, int scoreTableID){
		return DBManager.getInstance().getHighestPairs(docIDList, scoreTableID);
	}
	
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
	
}
