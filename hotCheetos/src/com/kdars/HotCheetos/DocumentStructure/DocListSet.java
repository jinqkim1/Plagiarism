package com.kdars.HotCheetos.DocumentStructure;
import java.io.Serializable;
import java.util.ArrayList;


public class DocListSet implements Serializable{
	public ArrayList<DocListItem> docListItem = new ArrayList<DocListItem>();
	
	public boolean addDocListItem(DocListItem item){
		return docListItem.add(item);
	}
}
