package com.kdars.HotCheetos.DocumentStructure;

import java.io.Serializable;
import java.util.ArrayList;

public class SenInfo implements Serializable{
	public String sentenceText;		//Actual sentence string
	public ArrayList<Integer> sentenceLines = new ArrayList<Integer>(); //Lines of the sentence
	public ArrayList<Integer> matchLine = new ArrayList<Integer>();	// Matching sentenceIDs (Multiple or single)
	public ArrayList<Integer> perfectMatchLine = new ArrayList<Integer>();
}
