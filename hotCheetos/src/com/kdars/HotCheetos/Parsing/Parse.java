package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public interface Parse {
	public int nGramSetting = Configuration.getInstance().getNgramSetting();
	public int substringSetting = Configuration.getInstance().getSubstringSetting();
	public int fingerprintSetting = Configuration.getInstance().getFingerprintSetting();
	
	public DocumentInfo parseDoc(String content);
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<String> contentSet);
}
