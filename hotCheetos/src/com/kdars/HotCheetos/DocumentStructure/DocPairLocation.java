package com.kdars.HotCheetos.DocumentStructure;
import java.io.Serializable;


public class DocPairLocation implements Serializable{
	public DocInfo leftDoc;
	public DocInfo rightDoc;
	public double simScore;
	public int perfectMatchCount;
	public int matchCount;
}
