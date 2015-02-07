package com.kdars.HotCheetos.DocumentStructure;

import java.util.ArrayList;
import java.util.HashMap;

public class DocumentInfo {
	public int docID;
	public String contents;
	public HashMap<String, Integer> termFreq = new HashMap<String, Integer>();
	public HashMap<String, ArrayList<Integer>> location = new HashMap<String, ArrayList<Integer>>();
	public String termFreqContent;
	public String locationContent;
}
