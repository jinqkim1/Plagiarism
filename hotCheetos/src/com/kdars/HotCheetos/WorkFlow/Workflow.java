package com.kdars.HotCheetos.WorkFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
		
		
//		ArrayList<String> fileNames = FileDataImport.getInstance().getFileNamesFromDir(path);
//
//		initial = System.currentTimeMillis();
//		ArrayList<String> contentsList = FileDataImport.getInstance().getFilesFromFileNames(fileNames);
//		finall = System.currentTimeMillis();
//		System.out.println("디렉토리에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
//		initial = System.currentTimeMillis();
//		HashMap<Integer, DocumentInfo> corpus = new HashMap<Integer, DocumentInfo>();
//		for(int i=0; i<contentsList.size(); ++i){
//			DocumentInfo di = new DocumentInfo();
//			di = Parse_nGram_hashcode.getInstance().parseDoc(contentsList.get(i));
//			corpus.put(i, di);
//		}
//		finall = System.currentTimeMillis();
//		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		HashMap<Integer,String> textMap = DBManager.getInstance().getAllText();
		finall = System.currentTimeMillis();
		System.out.println("DB에서 모든 텍스트 파일을 읽어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		HashMap<Integer, DocumentInfo> corpus = new HashMap<Integer, DocumentInfo>();
		Parse_nGram_hashcode.getInstance().parseDocSet(textMap);
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
		ArrayList<DocPair> docPairs = getDocPairs(corpus);
		
		
		
	}

	private ArrayList<DocPair> getDocPairs(HashMap<Integer, DocumentInfo> corpus) {
		ArrayList<DocPair> docPairs = new ArrayList<DocPair>();
		Set<Integer> set = corpus.keySet();
		Iterator<Integer> iter = set.iterator();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		while(iter.hasNext()){
			ids.add(iter.next());
		}
		
		
		for(int i=0; i<ids.size(); i++){
			for(int j=i; j<ids.size(); ++j){
				DocPair dp = new DocPair();
				dp.docID1=i;
				dp.docID2=j;
				dp.similarity = CosinSim.getInstance().calcSim(corpus.get(i).termFreq, corpus.get(j).termFreq);				
			}
		}
		return null;
	}
}
