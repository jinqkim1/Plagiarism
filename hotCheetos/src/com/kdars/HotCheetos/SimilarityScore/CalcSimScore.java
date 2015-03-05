package com.kdars.HotCheetos.SimilarityScore;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;

public abstract class CalcSimScore {
	
	abstract double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2);
	abstract boolean interCalcForIntraCalcSimSet(ArrayList<DocumentInfo> intraDocInfoListForInter1, ArrayList<DocumentInfo> intraDocInfoListForInter2, int scoreTableID);
	abstract boolean intraCalcSimSet(ArrayList<DocumentInfo> docInfoList, int scoreTableID);
	abstract boolean interCalculateSegment(ArrayList<DocumentInfo> docInfoList, ArrayList<DocumentInfo> corpusDocInfoList, int scoreTableID, int invertedIndexTableID);
	abstract boolean simCalcProcessorBatch(ArrayList<DocumentInfo> docInfoList, ArrayList<ArrayList<Integer>> docIDListListForIntraInterCalc, int invertedIndexTableID, int scoreTableID);
	abstract boolean simCalcProcessor(ArrayList<DocumentInfo> docInfoList, ArrayList<Integer> corpusDocIDArray, ArrayList<ArrayList<Integer>> docIDListListForIntraInterCalc, int invertedIndexTableID, int scoreTableID);
	abstract boolean interCalcSimSet(ArrayList<DocumentInfo> docInfoList,  ArrayList<Integer> corpusDocIDArray, int scoreTableID, int invertedIndexTableID);
	
	public ArrayList<DocPair> getHighestScorePairs(ArrayList<Integer> docIDList, int scoreTableID){
		return DBManager.getInstance().getHighestPairs(docIDList, scoreTableID);
	}
	
}
