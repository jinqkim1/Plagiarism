package com.kdars.HotCheetos.WorkFlow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.ImportContent1;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.MapReduce.PdfDriver;
import com.kdars.HotCheetos.MapReduce.SimScoreDriver1;
import com.kdars.HotCheetos.MapReduce.SimScoreDriver2;
import com.kdars.HotCheetos.PairStructure.DocPair;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_string;
import com.kdars.HotCheetos.Parsing.Parse1_noun_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_noun_string;
import com.kdars.HotCheetos.Parsing.Parse1_sentence_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_sentence_string;
import com.kdars.HotCheetos.SimilarityScore.CosineSim;
import com.kdars.HotCheetos.SimilarityScore.SimCalc_Sentence;

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
		int scoreTableID = 1;
		if(!memoryProbSolvedBatch(docIDList, invertedIndexTableID, scoreTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolvedBatch method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB에서 parse된 데이터 가져와서 simscore 계산 후 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
//		initial = System.currentTimeMillis();
//		CosineSim cosineSimilarity = new CosineSim();
//		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
//		finall = System.currentTimeMillis();
//		System.out.println("hashing set의 모든 pair의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
	}
	
	public ArrayList<DocPair> inputDocWorkFlow(ArrayList<File> zipFileList, int invertedIndexTableID, int scoreTableID){
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("작업 시작 전 먼저 DB에 있는 모든 docID를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");

		initial = System.currentTimeMillis();
		ImportContent1 importData = new ImportContent1();
		ArrayList<Integer> docIDList = importData.importProcessor(zipFileList);
		finall = System.currentTimeMillis();
		System.out.println("들어온 압축파일을 unzip하고 string을 뽑은 다음, string에서 필요한 text만 추출한 후에 docIDList를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index 저장 실패.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		if (!memoryProbSolved(docIDList, corpusDocIDArray, invertedIndexTableID, scoreTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB에서 parse된 데이터 가져와서 simscore 계산 후 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis(); //doc ID list가 너무 클 경우에는 query가 너무 길어짐. 추가 logic 필요.
		CosineSim cosineSimilarity = new CosineSim();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
		finall = System.currentTimeMillis();
		System.out.println("hashing set의 모든 pair의 similarity 계산하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		return highestPairList;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public Integer TEMPORARYprismWorkFlow(String[] args) throws ClassNotFoundException, IOException, InterruptedException{
		
		String finalINPUT_PATH = args[1];
		String finalOUTPUT_PATH = args[2];
		
		String intermediateOUTPUT_PATH = Configurations.getInstance().getIntermediateOUTPUT_PATH();
		String intermediateOUTPUT_PATH1 = Configurations.getInstance().getIntermediateOUTPUT_PATH1();
		
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		//input에 PDF file들이 들어가 있다고 가정 후, pdf 파일을 읽어서 Text extract 후 preprocessing을 한 다음 testdb에 저장하고 parsing 진행. parsing된 data는 <docID, hashMap> 형태로 hdfs에 저장됨. (parse된 data는 아직 db에 저장하지 않음)
		PdfDriver drive = new PdfDriver(); 
		int jobComplete = drive.driver(finalINPUT_PATH, intermediateOUTPUT_PATH);
		finall = System.currentTimeMillis();
		System.out.println("PDF Driver 도는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		System.out.println();
		
		initial = System.currentTimeMillis();
		//parse되어 hdfs에 저장된 data를 한 pair씩 (즉, 한 document씩) mapper가 처리함. 각 mapper에서는 한 document 대 기존에 저장되어 있던 corpus와 similairty계산. (input document 간의 similarity계산은 하지 않음)
		SimScoreDriver1 drive1 = new SimScoreDriver1();
		int jobComplete1 = drive1.driver(intermediateOUTPUT_PATH, intermediateOUTPUT_PATH1);
		finall = System.currentTimeMillis();
		System.out.println("SimScore Driver1 도는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		System.out.println();
		
		initial = System.currentTimeMillis();
		//parse되어 hdfs에 저장된 data를 한 pair씩 (즉, 한 document씩) mapper가 처리함. 각 mapper에서는 한 document 대 "INPUT" corpus와 similairty계산. (input document 간의 similarity계산만 함.)
		SimScoreDriver2 drive2 = new SimScoreDriver2();
		int jobComplete2 = drive2.driver(intermediateOUTPUT_PATH, intermediateOUTPUT_PATH1);
		finall = System.currentTimeMillis();
		System.out.println("SimScore Driver1 도는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		System.out.println();
		
		//이 시점에서는 모든 input document들은 invertedindextable에서 2로 flag 세워져 있을 것. 0으로 만들어 주기.
		DBManager.getInstance().flagCompleteDocuments(Configurations.getInstance().getTableID());
		
		//만들어진 output directory들 모두 지움.
		FileSystem hdfs = FileSystem.get(new Configuration());
		Path deleteIntermediateOutputPath = new Path(intermediateOUTPUT_PATH);
		Path deleteIntermediateOutputPath1 = new Path(intermediateOUTPUT_PATH1);
		Path deleteOutputPath = new Path(finalOUTPUT_PATH);
		if(hdfs.exists(deleteIntermediateOutputPath)){
			hdfs.delete(deleteIntermediateOutputPath, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteIntermediateOutputPath1)){
			hdfs.delete(deleteIntermediateOutputPath1, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteOutputPath)){
			hdfs.delete(deleteOutputPath, true);  //delete existing directory
		}
		
		if (jobComplete == 0 && jobComplete1 == 0 && jobComplete2 == 0){  //세 가지 job을 모두 완벽하게 수행하였을 때만 0으로 리턴. 아니면 1로 리턴.
			return 0;
		}
		
		return 1;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	
	public ArrayList<DocPair> workFlowExperiment_Sentence(int experimentTableID, int fingerprint){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		Configurations.getInstance().setFingerprintSetting(fingerprint);
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("작업 시작 전 먼저 DB에 있는 모든 docID를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		
		if(experimentTableID == 73 || experimentTableID == 74){
			Parse1_sentence_hashcode test = new Parse1_sentence_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}else if(experimentTableID == 75 || experimentTableID == 76){
			Parse1_sentence_string test = new Parse1_sentence_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}
		
		initial = System.currentTimeMillis();
		if (!experimentMemoryProbSolvedBatch_Sentence(corpusDocIDArray, experimentTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB에서 parse된 데이터 가져와서 simscore 계산 후 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis(); //doc ID list가 너무 클 경우에는 query가 너무 길어짐. 추가 logic 필요.
		SimCalc_Sentence cosineSimilarity = new SimCalc_Sentence();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(corpusDocIDArray, experimentTableID);
		finall = System.currentTimeMillis();
		System.out.println("highest score를 가진 doc pair list를 DB에서 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초\n\n\n");
		
		return highestPairList;
	}
	
	public ArrayList<DocPair> workFlowExperiment(int experimentTableID, int ngramwindow, int fingerprint){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		Configurations.getInstance().setNgramSetting(ngramwindow);
		Configurations.getInstance().setFingerprintSetting(fingerprint);
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("작업 시작 전 먼저 DB에 있는 모든 docID를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");

//		initial = System.currentTimeMillis();
//		ImportContent1 importData = new ImportContent1();
//		ArrayList<Integer> docIDList = importData.importProcessor(zipFileList);
//		finall = System.currentTimeMillis();
//		System.out.println("들어온 압축파일을 unzip하고 string을 뽑은 다음, string에서 필요한 text만 추출한 후에 docIDList를 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis();
		
		if(experimentTableID <= 16){
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}else if(experimentTableID > 16 && experimentTableID <= 36){
			Parse1_noun_hashcode test = new Parse1_noun_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}else if(experimentTableID > 36 && experimentTableID <= 52){
			Parse1_nGram_string test = new Parse1_nGram_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}else{
			Parse1_noun_string test = new Parse1_noun_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index 저장 실패.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input 텍스트 파일을 hashing 하고 DB에 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		}
		
		initial = System.currentTimeMillis();
		if (!experimentMemoryProbSolvedBatch(corpusDocIDArray, experimentTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB에서 parse된 데이터 가져와서 simscore 계산 후 저장하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
		
		initial = System.currentTimeMillis(); //doc ID list가 너무 클 경우에는 query가 너무 길어짐. 추가 logic 필요.
		CosineSim cosineSimilarity = new CosineSim();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(corpusDocIDArray, experimentTableID);
		finall = System.currentTimeMillis();
		System.out.println("highest score를 가진 doc pair list를 DB에서 가져오는데 걸린 시간  :  " + (finall - initial)/1000 + "초\n\n\n");
		
		return highestPairList;
	}
	
	private boolean experimentMemoryProbSolvedBatch_Sentence(ArrayList<Integer> docIDList, int experimentTableID){
		int docIDMemoryLimit = Configurations.getInstance().getDocIDListLimit();
		docIDList = new ArrayList<Integer>(docIDList);
//		double initial = System.currentTimeMillis();
//		double finall = System.currentTimeMillis();
		
		//intra 문제 해결 필요.
		ArrayList<ArrayList<Integer>> docIDListList = new ArrayList<ArrayList<Integer>>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				
//				initial = System.currentTimeMillis();
				ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
				
				if(experimentTableID == 73 || experimentTableID == 74){
					Parse1_sentence_hashcode test = new Parse1_sentence_hashcode();
					docInfoList = test.getParsedDocs_Sentence(docIDList, experimentTableID);
				}else if(experimentTableID == 75 || experimentTableID == 76){
					Parse1_sentence_string test = new Parse1_sentence_string();
					docInfoList = test.getParsedDocs_Sentence(docIDList, experimentTableID);
				}
				
//				finall = System.currentTimeMillis();
//				System.out.println("DB에서 docInfo list 만들어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
				SimCalc_Sentence cosineSimilarity = new SimCalc_Sentence();
				if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, experimentTableID, experimentTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(docIDList.subList(0, docIDMemoryLimit));
//			initial = System.currentTimeMillis();
			ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
			if(experimentTableID == 73 || experimentTableID == 74){
				Parse1_sentence_hashcode test = new Parse1_sentence_hashcode();
				docInfoList = test.getParsedDocs_Sentence(segmentedDocIDList, experimentTableID);
			}else if(experimentTableID == 75 || experimentTableID == 76){
				Parse1_sentence_string test = new Parse1_sentence_string();
				docInfoList = test.getParsedDocs_Sentence(segmentedDocIDList, experimentTableID);
			}
//			finall = System.currentTimeMillis();
//			System.out.println("DB에서 docInfo list 만들어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
			
			SimCalc_Sentence cosineSimilarity = new SimCalc_Sentence();
			if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, experimentTableID, experimentTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			
			docIDListList.add(segmentedDocIDList);
			docIDList = new ArrayList<Integer>(docIDList.subList(docIDMemoryLimit, docIDList.size()));
			
		}
		
		return true;
	}
	
	private boolean experimentMemoryProbSolvedBatch(ArrayList<Integer> docIDList, int experimentTableID){
		int docIDMemoryLimit = Configurations.getInstance().getDocIDListLimit();
		docIDList = new ArrayList<Integer>(docIDList);
//		double initial = System.currentTimeMillis();
//		double finall = System.currentTimeMillis();
		
		//intra 문제 해결 필요.
		ArrayList<ArrayList<Integer>> docIDListList = new ArrayList<ArrayList<Integer>>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				
//				initial = System.currentTimeMillis();
				ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
				if(experimentTableID <= 16){
					Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
					docInfoList = test.getParsedDocs(docIDList, experimentTableID);
				}else if(experimentTableID > 16 && experimentTableID <= 36){
					Parse1_noun_hashcode test = new Parse1_noun_hashcode();
					docInfoList = test.getParsedDocs(docIDList, experimentTableID);
				}else if(experimentTableID > 36 && experimentTableID <= 52){
					Parse1_nGram_string test = new Parse1_nGram_string();
					docInfoList = test.getParsedDocs(docIDList, experimentTableID);
				}else{
					Parse1_noun_string test = new Parse1_noun_string();
					docInfoList = test.getParsedDocs(docIDList, experimentTableID);
				}
//				finall = System.currentTimeMillis();
//				System.out.println("DB에서 docInfo list 만들어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				
				CosineSim cosineSimilarity = new CosineSim();
				if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, experimentTableID, experimentTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(docIDList.subList(0, docIDMemoryLimit));
//			initial = System.currentTimeMillis();
			ArrayList<DocumentInfo> docInfoList = new ArrayList<DocumentInfo>();
			if(experimentTableID <= 16){
				Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
				docInfoList = test.getParsedDocs(segmentedDocIDList, experimentTableID);
			}else if(experimentTableID > 16 && experimentTableID <= 36){
				Parse1_noun_hashcode test = new Parse1_noun_hashcode();
				docInfoList = test.getParsedDocs(segmentedDocIDList, experimentTableID);
			}else if(experimentTableID > 36 && experimentTableID <= 52){
				Parse1_nGram_string test = new Parse1_nGram_string();
				docInfoList = test.getParsedDocs(segmentedDocIDList, experimentTableID);
			}else{
				Parse1_noun_string test = new Parse1_noun_string();
				docInfoList = test.getParsedDocs(segmentedDocIDList, experimentTableID);
			}
//			finall = System.currentTimeMillis();
//			System.out.println("DB에서 docInfo list 만들어오는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
			
			CosineSim cosineSimilarity = new CosineSim();
			if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, experimentTableID, experimentTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			
			docIDListList.add(segmentedDocIDList);
			docIDList = new ArrayList<Integer>(docIDList.subList(docIDMemoryLimit, docIDList.size()));
			
		}
		
		return true;
	}
	
	private boolean memoryProbSolved(ArrayList<Integer> docIDList, ArrayList<Integer> corpusDocIDArray, int invertedIndexTableID, int scoreTableID){
		int docIDMemoryLimit = Configurations.getInstance().getDocIDListLimit();
		docIDList = new ArrayList<Integer>(docIDList);
		
		ArrayList<ArrayList<Integer>> docIDListList = new ArrayList<ArrayList<Integer>>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
				ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
				CosineSim cosineSimilarity = new CosineSim();
				if (!cosineSimilarity.simCalcProcessor(docInfoList, corpusDocIDArray, docIDListList, invertedIndexTableID, scoreTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(docIDList.subList(0, docIDMemoryLimit));
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(segmentedDocIDList, invertedIndexTableID);
			CosineSim cosineSimilarity = new CosineSim();
			if (!cosineSimilarity.simCalcProcessor(docInfoList, corpusDocIDArray, docIDListList, invertedIndexTableID, scoreTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			
			docIDListList.add(segmentedDocIDList);
			docIDList = new ArrayList<Integer>(docIDList.subList(docIDMemoryLimit, docIDList.size()));
			
		}
		
		return true;
	}
	
	private boolean memoryProbSolvedBatch(ArrayList<Integer> docIDList, int invertedIndexTableID, int scoreTableID){
		int docIDMemoryLimit = Configurations.getInstance().getDocIDListLimit();
		docIDList = new ArrayList<Integer>(docIDList);
		
		//intra 문제 해결 필요.
		ArrayList<ArrayList<Integer>> docIDListList = new ArrayList<ArrayList<Integer>>();
		while (!docIDList.isEmpty()){
			
			if(docIDList.size() <= docIDMemoryLimit){
				Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
				ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(docIDList, invertedIndexTableID);
				CosineSim cosineSimilarity = new CosineSim();
				if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, invertedIndexTableID, scoreTableID)){
					System.out.println("Processing failed. Need to check workflow.");
					return false;
				}
				docIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(docIDList.subList(0, docIDMemoryLimit));
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			ArrayList<DocumentInfo> docInfoList = test.getParsedDocs(segmentedDocIDList, invertedIndexTableID);
			CosineSim cosineSimilarity = new CosineSim();
			if (!cosineSimilarity.simCalcProcessorBatch(docInfoList, docIDListList, invertedIndexTableID, scoreTableID)){
				System.out.println("Processing failed. Need to check workflow.");
				return false;
			}
			docIDListList.add(segmentedDocIDList);
			docIDList = new ArrayList<Integer>(docIDList.subList(docIDMemoryLimit, docIDList.size()));
			
		}
		
		return true;
	}
	
}
