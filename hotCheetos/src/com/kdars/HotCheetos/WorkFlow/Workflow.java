package com.kdars.HotCheetos.WorkFlow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.ImportContent1;
import com.kdars.HotCheetos.DocumentStructure.DocInfo;
import com.kdars.HotCheetos.DocumentStructure.DocPairLocation;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.DocumentStructure.ObjectFileConverter;
import com.kdars.HotCheetos.MapReduce.ParsedInvertedIndexSaveDriver;
import com.kdars.HotCheetos.MapReduce.ParsingDriver;
import com.kdars.HotCheetos.MapReduce.PdfDriver;
import com.kdars.HotCheetos.MapReduce.SimScoreDriver2;
import com.kdars.HotCheetos.MapReduce.SimScoreDriver1;
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
		//invertedIndexTableID, locationTableID, scoreTableID�� �� table�� ����. �� �ʿ�.
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
		
//		initial = System.currentTimeMillis();
//		CosineSim cosineSimilarity = new CosineSim();
//		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(docIDList, scoreTableID);
//		finall = System.currentTimeMillis();
//		System.out.println("hashing set�� ��� pair�� similarity ����ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
	}
	
	public ArrayList<DocPair> inputDocWorkFlow(ArrayList<File> zipFileList, int invertedIndexTableID, int scoreTableID){
		
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
		if (!test.parseDocSet(docIDList, invertedIndexTableID)){
			System.out.println("Inverted index ���� ����.");
		}
		finall = System.currentTimeMillis();
		System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	public Integer TEMPORARYprismWorkFlow(String[] args) throws ClassNotFoundException, IOException, InterruptedException{
		
		String finalINPUT_PATH = args[1];
		String finalOUTPUT_PATH = args[2];
		
		String intermediateOUTPUT_PATH = Configurations.getInstance().getIntermediateOUTPUT_PATH();
		String intermediateOUTPUT_PATH1 = Configurations.getInstance().getIntermediateOUTPUT_PATH1();
		String intermediateOUTPUT_PATH2 = Configurations.getInstance().getIntermediateOUTPUT_PATH2();
				
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		initial = System.currentTimeMillis();
		//input�� PDF file���� �� �ִٰ� ���� ��, pdf ������ �о Text extract �� preprocessing�� �� ���� testdb�� �����ϰ� parsing ����. parsing�� data�� <docID, hashMap> ���·� hdfs�� �����. (parse�� data�� ���� db�� �������� ����)
		PdfDriver drive = new PdfDriver(); 
		int jobComplete = drive.driver(finalINPUT_PATH, intermediateOUTPUT_PATH);
		finall = System.currentTimeMillis();
		System.out.println("PDF Driver done  :  " + (finall - initial)/1000 + " sec");
		System.out.println();
		initial = System.currentTimeMillis();

		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('parse start')");
		//input�� PDF file���� �� �ִٰ� ���� ��, pdf ������ �о Text extract �� preprocessing�� �� ���� testdb�� �����ϰ� parsing ����. parsing�� data�� <docID, hashMap> ���·� hdfs�� �����. (parse�� data�� ���� db�� �������� ����)
		ParsingDriver drive1 = new ParsingDriver(); 
		int jobComplete1 = drive1.driver(intermediateOUTPUT_PATH, intermediateOUTPUT_PATH1);
		finall = System.currentTimeMillis();
		System.out.println("Parse Driver done  :  " + (finall - initial)/1000 + "sec");
		System.out.println();
		
		initial = System.currentTimeMillis();
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('SimScoreDriver1 start')");
		
		//parse�Ǿ� hdfs�� ����� data�� �� pair�� (��, �� document��) mapper�� ó����. �� mapper������ �� document �� "INPUT" corpus�� similairty���. (input document ���� similarity��길 ��.)
		SimScoreDriver1 drive2 = new SimScoreDriver1();
		int jobComplete2 = drive2.driver(intermediateOUTPUT_PATH1, intermediateOUTPUT_PATH2);
		finall = System.currentTimeMillis();
		System.out.println("SimScoreDriver1 done :  " + (finall - initial)/1000 + "sec");
		System.out.println();
		//�� ���������� ��� input document���� invertedindextable���� 2�� flag ������ ���� ��. 0���� ����� �ֱ�.
		
		initial = System.currentTimeMillis();
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('SimScoreDriver2 start')");
		
		int jobComplete3 = 0;
		ArrayList<Integer> corpusDocIDList = DBManager.getInstance().getCurrentDocIDsFromInvertedIndexTable(Configurations.getInstance().getTableID());
		if(!corpusDocIDList.isEmpty()){
			SimScoreDriver2 drive3 = new SimScoreDriver2();
			jobComplete3 = drive3.driver(intermediateOUTPUT_PATH1, intermediateOUTPUT_PATH2);
		}
		finall = System.currentTimeMillis();
		System.out.println("SimScoreDriver2 done  :  " + (finall - initial)/1000 + "sec");
		System.out.println();
		
		initial = System.currentTimeMillis();
		
		ParsedInvertedIndexSaveDriver drive4 = new ParsedInvertedIndexSaveDriver();
		int jobComplete4 = drive4.driver(intermediateOUTPUT_PATH1, finalOUTPUT_PATH);
		finall = System.currentTimeMillis();
		System.out.println("finally invertedindex of input documents saved and done  :  " + (finall - initial)/1000 + "sec");
		System.out.println();
		
//		DBManager.getInstance().flagCompleteDocuments(Configurations.getInstance().getTableID());
		
		//������� output directory�� ��� ����.


		
		FileSystem hdfs = FileSystem.get(new Configuration());
		
		Path workingDir = hdfs.getWorkingDirectory();
		Path newFolderPath1 = new Path("/" + Configurations.getInstance().getDocInfoPathString());
		newFolderPath1 = Path.mergePaths(workingDir, newFolderPath1);
		Path newFolderPath2 = new Path("/" + Configurations.getInstance().getDocPairPathString());
		newFolderPath2 = Path.mergePaths(workingDir, newFolderPath2);
		
		FileStatus[] fss = hdfs.listStatus(newFolderPath2);
		for(FileStatus fs : fss){
			System.out.println(fs.getPath().toString());
			FSDataInputStream fsInStream = hdfs.open(fs.getPath());
			DocPairLocation docPair = new ObjectFileConverter<DocPairLocation>().file2Object(fsInStream);
			System.out.println(docPair.leftDoc.docID);
			System.out.println(docPair.rightDoc.docID);
			fsInStream.close();
		}
		
		Path deleteIntermediateOutputPath = new Path(intermediateOUTPUT_PATH);
		Path deleteIntermediateOutputPath1 = new Path(intermediateOUTPUT_PATH1);
		Path deleteIntermediateOutputPath2 = new Path(intermediateOUTPUT_PATH2);
		Path deleteOutputPath = new Path(finalOUTPUT_PATH);
		
		if(hdfs.exists(newFolderPath1)){
			hdfs.delete(newFolderPath1, true);  //delete existing directory
		}
		
		if(hdfs.exists(newFolderPath2)){
			hdfs.delete(newFolderPath2, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteIntermediateOutputPath)){
			hdfs.delete(deleteIntermediateOutputPath, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteIntermediateOutputPath1)){
			hdfs.delete(deleteIntermediateOutputPath1, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteIntermediateOutputPath2)){
			hdfs.delete(deleteIntermediateOutputPath2, true);  //delete existing directory
		}
		
		if(hdfs.exists(deleteOutputPath)){
			hdfs.delete(deleteOutputPath, true);  //delete existing directory
		}
		
		if (jobComplete == 0 && jobComplete1 == 0 && jobComplete2 == 0 && jobComplete3 == 0 && jobComplete4 == 0){  //�� ���� job�� ��� �Ϻ��ϰ� �����Ͽ��� ���� 0���� ����. �ƴϸ� 1�� ����.
			return 0;
		}
		
		return 1;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	
	
	public ArrayList<DocPair> workFlowExperiment_Sentence(int experimentTableID, int fingerprint){
		double initial = System.currentTimeMillis();
		double finall = System.currentTimeMillis();
		
		Configurations.getInstance().setFingerprintSetting(fingerprint);
		
		initial = System.currentTimeMillis();
		ArrayList<Integer> corpusDocIDArray = DBManager.getInstance().getAllTextAsDocIDArray();
		finall = System.currentTimeMillis();
		System.out.println("�۾� ���� �� ���� DB�� �ִ� ��� docID�� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		
		if(experimentTableID == 73 || experimentTableID == 74){
			Parse1_sentence_hashcode test = new Parse1_sentence_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}else if(experimentTableID == 75 || experimentTableID == 76){
			Parse1_sentence_string test = new Parse1_sentence_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}
		
		initial = System.currentTimeMillis();
		if (!experimentMemoryProbSolvedBatch_Sentence(corpusDocIDArray, experimentTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB���� parse�� ������ �����ͼ� simscore ��� �� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis(); //doc ID list�� �ʹ� Ŭ ��쿡�� query�� �ʹ� �����. �߰� logic �ʿ�.
		SimCalc_Sentence cosineSimilarity = new SimCalc_Sentence();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(corpusDocIDArray, experimentTableID);
		finall = System.currentTimeMillis();
		System.out.println("highest score�� ���� doc pair list�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��\n\n\n");
		
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
		System.out.println("�۾� ���� �� ���� DB�� �ִ� ��� docID�� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");

//		initial = System.currentTimeMillis();
//		ImportContent1 importData = new ImportContent1();
//		ArrayList<Integer> docIDList = importData.importProcessor(zipFileList);
//		finall = System.currentTimeMillis();
//		System.out.println("���� ���������� unzip�ϰ� string�� ���� ����, string���� �ʿ��� text�� ������ �Ŀ� docIDList�� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis();
		
		if(experimentTableID <= 16){
			Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}else if(experimentTableID > 16 && experimentTableID <= 36){
			Parse1_noun_hashcode test = new Parse1_noun_hashcode();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}else if(experimentTableID > 36 && experimentTableID <= 52){
			Parse1_nGram_string test = new Parse1_nGram_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}else{
			Parse1_noun_string test = new Parse1_noun_string();
			if (!test.parseDocSet(corpusDocIDArray, experimentTableID)){
				System.out.println("Inverted index ���� ����.");
			}
			finall = System.currentTimeMillis();
			System.out.println("Input �ؽ�Ʈ ������ hashing �ϰ� DB�� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		}
		
		initial = System.currentTimeMillis();
		if (!experimentMemoryProbSolvedBatch(corpusDocIDArray, experimentTableID)){
			System.out.println("Processing failed. Need to check memoryProbSolved method.");
		}
		finall = System.currentTimeMillis();
		System.out.println("DB���� parse�� ������ �����ͼ� simscore ��� �� �����ϴµ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
		
		initial = System.currentTimeMillis(); //doc ID list�� �ʹ� Ŭ ��쿡�� query�� �ʹ� �����. �߰� logic �ʿ�.
		CosineSim cosineSimilarity = new CosineSim();
		ArrayList<DocPair> highestPairList = cosineSimilarity.getHighestScorePairs(corpusDocIDArray, experimentTableID);
		finall = System.currentTimeMillis();
		System.out.println("highest score�� ���� doc pair list�� DB���� �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��\n\n\n");
		
		return highestPairList;
	}
	
	private boolean experimentMemoryProbSolvedBatch_Sentence(ArrayList<Integer> docIDList, int experimentTableID){
		int docIDMemoryLimit = Configurations.getInstance().getDocIDListLimit();
		docIDList = new ArrayList<Integer>(docIDList);
//		double initial = System.currentTimeMillis();
//		double finall = System.currentTimeMillis();
		
		//intra ���� �ذ� �ʿ�.
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
//				System.out.println("DB���� docInfo list �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
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
//			System.out.println("DB���� docInfo list �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
			
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
		
		//intra ���� �ذ� �ʿ�.
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
//				System.out.println("DB���� docInfo list �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
				
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
//			System.out.println("DB���� docInfo list �������µ� �ɸ� �ð�  :  " + (finall - initial)/1000 + "��");
			
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
		
		//intra ���� �ذ� �ʿ�.
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
