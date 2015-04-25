package com.kdars.HotCheetos.DocumentStructure;
import java.io.Serializable;
import java.util.ArrayList;


public class LineInfo implements Serializable{
	public String lineText;		// 라인의 순수 택스트
	public ArrayList<Integer> matchLine = new ArrayList<Integer>();	// 메치된 라인 넘버, 복수개 가능.
	
	public void addLineNumber(int num){
		matchLine.add(num);
	}
}
