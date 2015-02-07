package com.kdars.HotCheetos.WorkFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;
import com.kdars.HotCheetos.Parsing.Parse_nGram_hashcode;
import com.kdars.HotCheetos.SimilarityScore.CosinSim;

public class Workflow {
	
	private static  Workflow workflow = new Workflow();
	public static Workflow getInstance(){
		return	workflow;
	}

	public void findSimilaryPairInFolder(String path){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		
		ArrayList<String> fileNames = FileDataImport.getInstance().getFileNamesFromDir(path);

		initial = System.currentTimeMillis();
		ArrayList<String> contentsList = FileDataImport.getInstance().getFilesFromFileNames(fileNames);
		finall = System.currentTimeMillis();
		System.out.println("디렉토리에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		HashMap<Integer, DocumentInfo> corpus = new HashMap<Integer, DocumentInfo>();
		for(int i=0; i<contentsList.size(); ++i){
			DocumentInfo di = new DocumentInfo();
			di = Parse_nGram_hashcode.getInstance().parseDoc(contentsList.get(i), i);
			corpus.put(i, di);
		}
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");

		
	}
	
	public void findSimilaryPairInDB(){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> textMap = DBManager.getInstance().getAllTextAsDocumentInforList();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		textMap = Parse_nGram_hashcode.getInstance().parseDocSetWithDocumentInfoArray(textMap);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		
		initial = System.currentTimeMillis();
		ArrayList<DocPair> docPairs = getDocPairs(textMap);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		
		initial = System.currentTimeMillis();
		String csvContent = "";
		for(int i=0; i<docPairs.size(); i++){
			csvContent += String.valueOf(docPairs.get(i).docID1)+","+String.valueOf(docPairs.get(i).docID2)+","+String.valueOf(docPairs.get(i).similarity)+"\n";
		}
		DBManager.getInstance().insertBulkToScoreTable(csvContent);
		finall = System.currentTimeMillis();
		System.out.println("모든 pair의 similarity를 DB에 넣는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
	}
	
	public void findSimilaryPairJin(){
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
		getDocPairsJin(docInfoList);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	
	private void getDocPairsJin(ArrayList<DocumentInfo> docInfoList){
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
					if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString())){
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
	

	private ArrayList<DocPair> getDocPairs(ArrayList<DocumentInfo> textMap) {
		StringBuilder csvContent = new StringBuilder();
		ArrayList<DocPair> result = new ArrayList<DocPair>();
		
		for(int i=0; i<textMap.size(); i++){
			for(int j=i; j<textMap.size(); ++j){
				DocPair dp = new DocPair();
				int docid1 = textMap.get(i).docID;
				int docid2 = textMap.get(j).docID;
				double simscore = CosinSim.getInstance().calcSim(textMap.get(i).termFreq, textMap.get(j).termFreq);
				dp.docID1=docid1;
				dp.docID2=docid2;
				dp.similarity = simscore;
				result.add(dp);
				
				csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
			}
		}
		
		DBManager.getInstance().insertBulkToScoreTable(csvContent.toString());
		
		return result;
	}
}
