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
		//invertedIndexTableID, locationTableID, scoreTableID에 따른 table이 없음. 생성 필요.
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> docIDList = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		int invertedIndexTableID = 1;
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair를 DB에서 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		CosineSim cosineSimilarity = new CosineSim();
		int scoreTableID = 1;
		if (!cosineSimilarity.intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Score 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	public ArrayList<DocPair> inputDocWorkFlow(ArrayList<File> zipFileList){
		//invertedIndexTableID, locationTableID, scoreTableID에 따른 table이 없음. 생성 필요.
		//zipFileList가 일정 사이즈를 넘으면 메모리 문제가 생길 수 있으므로 윗단에서 limit(Configuration의 fileListLimit)을 이용하여 잘라서 처리할 수 있도록 하는 것이 필요.
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		ImportContent1 importData = new ImportContent1();
		ArrayList<Integer> docIDList = importData.importProcessor(zipFileList);
		finall = System.currentTimeMillis();
		System.out.println("들어온 압축파일을 unzip하고 string을 뽑은 다음, string에서 필요한 text만 추출한 후에 docIDList를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		int invertedIndexTableID = 1;
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
		finall = System.currentTimeMillis();
		System.out.println("Input 텍스트 파일의 hashmap과 docID를 DB에서 Document Info 자료구조에 담아서 가져오는데 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		CosineSim cosineSimilarity = new CosineSim();
		int scoreTableID = 1;
		if (!cosineSimilarity.interCalcSimSet(docInfoList, scoreTableID, invertedIndexTableID)){
			System.out.println("Inter score 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input 텍스트 파일들과 Corpus 간의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		if (!cosineSimilarity.intraCalcSimSet(docInfoList, scoreTableID)){
			System.out.println("Intra score 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input 텍스트 파일들 간의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis(); //doc ID list가 너무 클 경우에는 query가 너무 길어짐. 추가 logic 필요.
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		return highestPairList;
	}
	
}
