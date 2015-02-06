package com.kdars.HotCheetos.Parsing;

import java.util.ArrayList;

import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public interface Parse {
	public DocumentInfo parseDoc(String content);
	public ArrayList<DocumentInfo> parseDocSet(ArrayList<String> contentSet);
}
