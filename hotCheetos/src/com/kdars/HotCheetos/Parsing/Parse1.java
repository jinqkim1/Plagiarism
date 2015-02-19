package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public abstract class Parse1 {
	protected int nGramSetting = Configuration.getInstance().getNgramSetting();
	protected int substringSetting = Configuration.getInstance().getSubstringSetting();
	protected int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	protected ArrayList<String> stopWordList = DBManager.getInstance().getStopwords();
	
	abstract boolean parseDoc(String content, int documentID, int invertedIndexTableID);
	
	public boolean parseDocSet(ArrayList<Integer> docIDList, int invertedIndexTableID){
		
		boolean jobCompleteChecker = true;
		
//		int parsechecker = 0;
//		double initial = System.currentTimeMillis();
		for (int docid : docIDList){
//			parsechecker++;

			String content = DBManager.getInstance().getText(docid);
			if (!parseDoc(content, docid, invertedIndexTableID)){
				jobCompleteChecker = false;
			}
			
//			if(parsechecker == 100){
//				parsechecker = 0;
//				double finall = System.currentTimeMillis();
//				System.out.println("parsing 100개 시간  :  " + (finall - initial)/1000 + "초");
//				initial = System.currentTimeMillis();
//			}
		}
		
		return jobCompleteChecker;
	
	}
	
	public ArrayList<DocumentInfo> getParsedDocs(ArrayList<Integer> docIDList, int invertedIndexTableID){
		return DBManager.getInstance().getMultipleDocInfoArray(docIDList, invertedIndexTableID);
	}
	
	public ArrayList<DocumentInfo> getParsedDocs_Sentence(ArrayList<Integer> docIDList, int invertedIndexTableID){
		return DBManager.getInstance().getMultipleDocInfoArray_Sentence(docIDList, invertedIndexTableID);
	}
	
}
