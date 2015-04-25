package com.kdars.HotCheetos.DocumentStructure;
import java.io.Serializable;


public class DocListItem implements Serializable{
	public DocListItem(String left, String right, double value){
		this.leftDocName = left;
		this.rightDocName = right;
		this.value = value;
	}
	
	public String leftDocName;
	public String rightDocName;
	public double value;
}
