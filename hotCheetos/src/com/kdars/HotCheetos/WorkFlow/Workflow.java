package com.kdars.HotCheetos.WorkFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DataImport.ImportContent1;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse_noun_hashcode;
import com.kdars.HotCheetos.SimilarityScore.CosineSim;

public class Workflow {
	
	private static  Workflow workflow = new Workflow();
	public static Workflow getInstance(){
		return	workflow;
	}

	public void batchDocsWorkFlow(){
		//invertedIndexTableID, locationTableID, scoreTableID�� ���� table�� ����. ���� �ʿ�.
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		int invertedIndexTableID = 1;
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		CosineSim cosineSimilarity = new CosineSim();
		int scoreTableID = 1;
		if (!cosineSimilarity.intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Score ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public ArrayList<DocPair> inputDocWorkFlow(ArrayList<File> zipFileList){
		//invertedIndexTableID, locationTableID, scoreTableID�� ���� table�� ����. ���� �ʿ�.
		//zipFileList�� ���� ����� ������ �޸� ������ ���� �� �����Ƿ� ���ܿ��� limit(Configuration�� fileListLimit)�� �̿��Ͽ� �߶� ó���� �� �ֵ��� �ϴ� ���� �ʿ�.
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		ImportContent1 importData = new ImportContent1();
		ArrayList<Integer> docIDList = importData.importProcessor(zipFileList);
		finall = System.currentTimeMillis();
		System.out.println("���� ���������� unzip�ϰ� string�� ���� ����, string���� �ʿ��� text�� ������ �Ŀ� docIDList�� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		int invertedIndexTableID = 1;
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
		finall = System.currentTimeMillis();
		System.out.println("Input �ؽ�Ʈ ������ hashmap�� docID�� DB���� Document Info �ڷᱸ���� ��Ƽ� �������µ� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		CosineSim cosineSimilarity = new CosineSim();
		int scoreTableID = 1;
		if (!cosineSimilarity.interCalcSimSet(docInfoList, scoreTableID, invertedIndexTableID)){
			System.out.println("Inter score ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input �ؽ�Ʈ ���ϵ�� Corpus ���� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		if (!cosineSimilarity.intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Intra score ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input �ؽ�Ʈ ���ϵ� ���� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis(); //doc ID list�� �ʹ� Ŭ ��쿡�� query�� �ʹ� �����. �߰� logic �ʿ�.
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		return highestPairList;
	}
	
}
