package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;
import java.util.HashMap;

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
		
		for (int docid : docIDList){
			String content = DBManager.getInstance().getText(docid);
			if (!parseDoc(content, docid, invertedIndexTableID)){
				jobCompleteChecker = false;
			}
		}
		
		return jobCompleteChecker;
	
	}
	
	public ArrayList<DocumentInfo> getParsedDocs(ArrayList<Integer> docIDList, int invertedIndexTableID){
		return DBManager.getInstance().getMultipleDocInfoArray(docIDList, invertedIndexTableID);
	}
	
}
