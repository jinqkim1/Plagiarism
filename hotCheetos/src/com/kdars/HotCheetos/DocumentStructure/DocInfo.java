package com.kdars.HotCheetos.DocumentStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class DocInfo implements Serializable{
	public String docID;
	public String title;
	public HashMap<Integer, SenInfo> sentenceMap = new HashMap<Integer, SenInfo>();
					//SentenceID, sentence information
	
	public DocInfo(String docID, String title){
		this.docID = docID;
		this.title = title;
	}
}
