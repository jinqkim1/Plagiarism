package com.kdars.HotCheetos.Experiment;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Parsing.Parse_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse_noun_hashcode;
import com.kdars.HotCheetos.SimilarityScore.CosinSim;
import com.kdars.HotCheetos.Config.*;

public class Experiment {
	private static  Experiment experiment = new Experiment();
	public static Experiment getInstance(){
		return	experiment;
	}

	
	
	public void experiment1(){
		Configuration.getInstance().mod=0;
		
		System.out.println(Configuration.getInstance().mod);
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex1");
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	public void experiment2(){
		Configuration.getInstance().mod=4;
		
		System.out.println(Configuration.getInstance().mod);
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex2");
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	public void experiment3(){
		Configuration.getInstance().mod=0;
		
		System.out.println(Configuration.getInstance().mod);
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex3");
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	public void experiment5(){
		Configuration.getInstance().mod=4;
		
		System.out.println(Configuration.getInstance().mod);
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	
	private void getDocPairsJin(ArrayList<DocumentInfo> docInfoList, String tableName){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		
		int bulkInsertLimitChecker = 0;
		for(int i=0; i<docInfoList.size(); i++){
			for(int j=i+1; j<docInfoList.size(); ++j){
				
				int docid1 = docInfoList.get(i).docID;
				int docid2 = docInfoList.get(j).docID;
				double simscore = CosinSim.getInstance().calcSim(docInfoList.get(i).termFreq, docInfoList.get(j).termFreq);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit){
					if(!DBManager.getInstance().insertBulkToScoreTableWithTableName(csvContent.toString(), tableName)){
						System.out.println("Similarity score bulk insert failed.");
					}
					bulkInsertLimitChecker = 0;
					csvContent.delete(0,csvContent.length());
				}
			}
		}
		
		if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString())){
			System.out.println("Similarity score bulk insert failed.");
		}
		
	}

}
