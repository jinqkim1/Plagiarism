package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public interface Parse {
	public int nGramSetting = Configuration.getInstance().getNgramSetting();
	public int substringSetting = Configuration.getInstance().getSubstringSetting();
	public int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	public ArrayList<String> stopWordList = DBManager.getInstance().getStopwords();
	
	public DocumentInfo parseDoc(int documentID);
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<Integer> docIDSet);
}
