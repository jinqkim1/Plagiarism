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
		
		
		ArrayList<String> fileNames = FileDataImport.getInstance().getFileNamesFromDir(path);

		initial = System.currentTimeMillis();
		ArrayList<String> contentsList = FileDataImport.getInstance().getFilesFromFileNames(fileNames);
		finall = System.currentTimeMillis();
		System.out.println("���丮���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		HashMap<Integer, DocumentInfo> corpus = new HashMap<Integer, DocumentInfo>();
		for(int i=0; i<contentsList.size(); ++i){
			DocumentInfo di = new DocumentInfo();
			di = Parse_nGram_hashcode.getInstance().parseDoc(contentsList.get(i), i);
			corpus.put(i, di);
		}
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");

		
	}
	
	public void findSimilaryPairInDB(){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();	
		
		initial = System.currentTimeMillis();
		ArrayList<DocumentInfo> textMap = DBManager.getInstance().getAllTextAsDocumentInforList();
		finall = System.currentTimeMillis();
		System.out.println("DB���� ��� �ؽ�Ʈ ������ �о���µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		textMap = Parse_nGram_hashcode.getInstance().parseDocSetWithDocumentInfoArray(textMap);
		finall = System.currentTimeMillis();
		System.out.println("��� �ؽ�Ʈ ������ hashing �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
		
		initial = System.currentTimeMillis();
		ArrayList<DocPair> docPairs = getDocPairs(textMap);
		finall = System.currentTimeMillis();
		System.out.println("hashing set�� ��� pair�� similarity ��� �ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	
	
	

	private ArrayList<DocPair> getDocPairs(ArrayList<DocumentInfo> textMap) {
		
		ArrayList<DocPair> result = new ArrayList<DocPair>();
		
		for(int i=0; i<textMap.size(); i++){
			for(int j=i; j<textMap.size(); ++j){
				DocPair dp = new DocPair();
				dp.docID1=textMap.get(i).docID;
				dp.docID2=textMap.get(j).docID;
				dp.similarity = CosinSim.getInstance().calcSim(textMap.get(i).termFreq, textMap.get(j).termFreq);	
				result.add(dp);
			}
		}
		return null;
	}
}
