package com.kdars.HotCheetos.DocumentStructure;

import java.util.ArrayList;
import java.util.HashMap;

public class SentenceInfo {
	public int sentenceID;
	public HashMap<String, Integer> termFreq = new HashMap<String, Integer>();
	public HashMap<String, ArrayList<Integer>> location = new HashMap<String, ArrayList<Integer>>();
}
