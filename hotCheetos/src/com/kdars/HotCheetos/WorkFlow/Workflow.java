package com.kdars.HotCheetos.WorkFlow;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Parsing.Parse_nGram_hashcode;

public class Workflow {

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
			di = Parse_nGram_hashcode.getInstance().parseDoc(contentsList.get(i));
			corpus.put(i, di);
		}
		finall = System.currentTimeMillis();
		System.out.println("모든 텍스트 파일을 hashing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		
		
		
		
		//for(int i=0; i<)
		
		
	}
}
