package com.kdars.HotCheetos.Experiment;

import java.util.ArrayList;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Parsing.Parse_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse_nGram_string;
import com.kdars.HotCheetos.Parsing.Parse_noun_hashcode;
import com.kdars.HotCheetos.Parsing.Parse_noun_string;
import com.kdars.HotCheetos.SimilarityScore.CosineSim;

public class Experiment {
	private static  Experiment experiment = new Experiment();
	public static Experiment getInstance(){
		return	experiment;
	}

	
	
	public void experiment1(){
		Configuration.getInstance().mod=1;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex1");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment2(){
		Configuration.getInstance().mod=4;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex2");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment3(){
		Configuration.getInstance().mod=1;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex3");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment4(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment5(){
		Configuration.getInstance().mod=1;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_string.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		//getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment6(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_nGram_string.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		//getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment7(){
		Configuration.getInstance().mod=1;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_string.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		//getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment8(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
		docInfoList = Parse_noun_string.getInstance().parseDocSetWithDocIDArray(docIDList);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		//getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment11(){
		Configuration.getInstance().mod=1;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArrayJINKYUWithTableName(docIDList,"ex1_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_nGram_hashcode.getInstance().getParsedDocsWithTableName(docIDList, "ex1_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex1");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		
	}
	
	public void experiment22(){
		Configuration.getInstance().mod=4;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_nGram_hashcode.getInstance().parseDocSetWithDocIDArrayJINKYUWithTableName(docIDList,"ex2_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_nGram_hashcode.getInstance().getParsedDocsWithTableName(docIDList, "ex2_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex2");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment33(){
		Configuration.getInstance().mod=1;
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArrayJINKYUWithTableName(docIDList,"ex3_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_noun_hashcode.getInstance().getParsedDocsWithTableName(docIDList, "ex3_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex3");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment44(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_noun_hashcode.getInstance().parseDocSetWithDocIDArrayJINKYUWithTableName(docIDList,"ex4_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_noun_hashcode.getInstance().getParsedDocsWithTableName(docIDList, "ex4_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex4");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment55(){
		Configuration.getInstance().mod=1;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_nGram_string.getInstance().parseDocSetWithDocIDArrayJINKYUWithStringTableName(docIDList,"ex5_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_nGram_string.getInstance().getParsedDocsWithStringTableName(docIDList, "ex5_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex5");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment66(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_nGram_string.getInstance().parseDocSetWithDocIDArrayJINKYUWithStringTableName(docIDList,"ex6_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_nGram_string.getInstance().getParsedDocsWithStringTableName(docIDList, "ex6_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex6");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
	}
	
	public void experiment77(){
		Configuration.getInstance().mod=1;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_noun_string.getInstance().parseDocSetWithDocIDArrayJINKYUWithStringTableName(docIDList,"ex7_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_noun_string.getInstance().getParsedDocsWithStringTableName(docIDList, "ex7_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex7");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public void experiment88(){
		Configuration.getInstance().mod=4;
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!Parse_noun_string.getInstance().parseDocSetWithDocIDArrayJINKYUWithStringTableName(docIDList,"ex8_hashtable")){
			System.out.println("inverted index table ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = Parse_noun_string.getInstance().getParsedDocsWithStringTableName(docIDList, "ex8_hashtable");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		getDocPairsJin(docInfoList, "ex8");
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	private void getDocPairsJin(ArrayList<DocumentInfo> docInfoList, String tableName){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		
		int bulkInsertLimitChecker = 0;
		for(int i=0; i<docInfoList.size(); i++){
			for(int j=i+1; j<docInfoList.size(); ++j){
				
				int docid1 = docInfoList.get(i).docID;
				int docid2 = docInfoList.get(j).docID;
				CosineSim cosineSimilarity = new CosineSim();
				double simscore = cosineSimilarity.calcSim(docInfoList.get(i).termFreq, docInfoList.get(j).termFreq);
				
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

		if(!DBManager.getInstance().insertBulkToScoreTableWithTableName(csvContent.toString(), tableName)){
			System.out.println("Similarity score bulk insert failed.");
		}
		
	}

}
