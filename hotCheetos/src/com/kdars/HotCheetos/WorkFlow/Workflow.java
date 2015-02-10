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
		int scoreTableID = 1;
		if(!memoryProbSolvedBatch(docIDList, invertedIndexTableID, scoreTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolvedBatch method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB���� parse�� ������ �����ͼ� simscore ��� �� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		CosineSim cosineSimilarity = new CosineSim();
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
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("�۾� ���� �� ���� DB�� �ִ� ��� docID�� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");

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
		int scoreTableID = 1;
		if (!memoryProbSolved(docIDList, corpusDocIDArray, invertedIndexTableID, scoreTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB���� parse�� ������ �����ͼ� simscore ��� �� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis(); //doc ID list�� �ʹ� Ŭ ��쿡�� query�� �ʹ� �����. �߰� logic �ʿ�.
		CosineSim cosineSimilarity = new CosineSim();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		return highestPairList;
	}
	
	private boolean memoryProbSolved(ArrayList<Integer> docIDList, ArrayList<Integer> corpusDocIDArray, int invertedIndexTableID, int scoreTableID){
		int docIDMemoryLimit = Configuration.getInstance().getDocIDListLimit();
		
		ArrayList<DocumentInfo> docInfoListForIntra = new ArrayList<DocumentInfo>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
				ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
				CosineSim cosineSimilarity = new CosineSim();
				if (!cosineSimilarity.simCalcProcessor(docInfoList, corpusDocIDArray, docInfoListForIntra, invertedIndexTableID, scoreTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = (ArrayList<Integer>) docIDList.subList(0, docIDMemoryLimit - 1);
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(segmentedDocIDList, invertedIndexTableID);
			CosineSim cosineSimilarity = new CosineSim();
			if (!cosineSimilarity.simCalcProcessor(docInfoList, corpusDocIDArray, docInfoListForIntra, invertedIndexTableID, scoreTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			docInfoListForIntra = docInfoList;
			docIDList = (ArrayList<Integer>) docIDList.subList(docIDMemoryLimit, docIDList.size() - 1);
			
		}
		
		return true;
	}
	
	private boolean memoryProbSolvedBatch(ArrayList<Integer> docIDList, int invertedIndexTableID, int scoreTableID){
		int docIDMemoryLimit = Configuration.getInstance().getDocIDListLimit();
		
		
		//intra ���� �ذ� �ʿ�.
		ArrayList<DocumentInfo> docInfoListForIntra = new ArrayList<DocumentInfo>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
				ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
				CosineSim cosineSimilarity = new CosineSim();
				if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docInfoListForIntra, invertedIndexTableID, scoreTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = (ArrayList<Integer>) docIDList.subList(0, docIDMemoryLimit - 1);
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(segmentedDocIDList, invertedIndexTableID);
			CosineSim cosineSimilarity = new CosineSim();
			if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docInfoListForIntra, invertedIndexTableID, scoreTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			docInfoListForIntra = docInfoList;
			docIDList = (ArrayList<Integer>) docIDList.subList(docIDMemoryLimit, docIDList.size() - 1);
			
		}
		
		return true;
	}
	
}
