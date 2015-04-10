package com.kdars.HotCheetos.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Experiment.Experiment;
import com.kdars.HotCheetos.MapReduce.PdfDriver;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_noun_hashcode;
import com.kdars.HotCheetos.WorkFlow.Workflow;

public class Main {
	private static int nGramSetting = 2;
	private static int fingerprintSetting = 1;
	
	private static String postFix1 = Configurations.getInstance().getPostFix1();
	private static String postFix2 = Configurations.getInstance().getPostFix2();
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		
//		System.out.println("\nexperiment1");
//		Experiment.getInstance().experiment1();
//		System.out.println("\nexperiment2");
//		Experiment.getInstance().experiment2();
//		System.out.println("\nexperiment3");
//		Experiment.getInstance().experiment3();
//		System.out.println("\nexperiment4");
//		Experiment.getInstance().experiment4();
//		System.out.println("\nexperiment5");
//		Experiment.getInstance().experiment5();
//		System.out.println("\nexperiment6");
//		Experiment.getInstance().experiment6();
//		System.out.println("\nexperiment7");
//		Experiment.getInstance().experiment7();
//		System.out.println("\nexperiment8");
//		Experiment.getInstance().experiment8();
		
		
//		System.out.println("\nexperiment11");
//		Experiment.getInstance().experiment11();
//		System.out.println("\nexperiment22");
//		Experiment.getInstance().experiment22();
//		System.out.println("\nexperiment33");
//		Experiment.getInstance().experiment33();
//		System.out.println("\nexperiment44");
//		Experiment.getInstance().experiment44();
//		System.out.println("\nexperiment55");
//		Experiment.getInstance().experiment55();
//		System.out.println("\nexperiment66");
//		Experiment.getInstance().experiment66();
//		System.out.println("\nexperiment77");
//		Experiment.getInstance().experiment77();
//		System.out.println("\nexperiment88");
//		Experiment.getInstance().experiment88();
		
		
		
		
		
//		String scoreRange = String.valueOf(0.8);
//		for (int i = 1 ; i < 73 ; i++){
//			String tableName = DBManager.getInstance().convertIDtoName_Score(i);
////			String query = "select plagiarismdb.texttable.DocID , plagiarismdb.texttable.Title, plagiarismdb.`" + tableName + "`.DocID , plagiarismdb.`" + tableName + "`.ComparedDocID , plagiarismdb.`" + tableName + "`.SimilarityScore into outfile '" + tableName + "_" + scoreRange + ".txt' fields terminated by ',' enclosed by '\"' lines terminated by '\\n' from  plagiarismdb.texttable, plagiarismdb.`" + tableName + "` where (plagiarismdb.`" + tableName + "`.DocID = plagiarismdb.texttable.DocID or plagiarismdb.`" + tableName + "`.ComparedDocID = plagiarismdb.texttable.DocID) and plagiarismdb.`" + tableName + "`.SimilarityScore > " + scoreRange + " order by similarityscore desc;";
//			String query = "select DocID, ComparedDocID, SimilarityScore into outfile '" + tableName + "_" + scoreRange + ".txt'  fields terminated by ',' enclosed by '\"' lines terminated by '\\n' from plagiarismdb.`" + tableName + "` where SimilarityScore >= " + scoreRange + " order by similarityscore desc;";
//			System.out.println(query);
//		}
		
		
		
		Workflow workflowForExperiment = new Workflow();
		
		String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(77);
		System.out.println(77 + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
		
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('start experiment')");
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='start experiment'");
		
		int jobComplete = workflowForExperiment.TEMPORARYprismWorkFlow(args);
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='start experiment'");
		
		
		
		System.out.println("���� ������ text table drop �� �ٽ� create �ؾ���!!!!!!!!!!!!");
		System.out.println("���� ������ Mapper���� DB table id �̸� �ٲ������!!!!!!!!!!!!!");
		System.exit(jobComplete);  //�� �� ������ 0�� ���� system exit�� ����.
		
//		experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(78);
//		System.out.println(78 + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//		workflowForExperiment.TEMPORARYprismWorkFlow(args, 78, 78);
//		System.out.println("���� ������ text table drop �� �ٽ� create �ؾ���!!!!!!!!!!!!");
//		System.out.println("���� ������ Reducer�� DB table id �̸� �ٲ������!!!!!!!!!!!!!");
//		
//		experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(79);
//		System.out.println(79 + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//		workflowForExperiment.TEMPORARYprismWorkFlow(args, 79, 79);
//		System.out.println("���� ������ text table drop �� �ٽ� create �ؾ���!!!!!!!!!!!!");
//		System.out.println("���� ������ Reducer�� DB table id �̸� �ٲ������!!!!!!!!!!!!!");
//		
//		experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(80);
//		System.out.println(80 + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//		workflowForExperiment.TEMPORARYprismWorkFlow(args, 80, 80);
//		System.out.println("���� ������ text table drop �� �ٽ� create �ؾ���!!!!!!!!!!!!");
		
		
		
//		Workflow workflowForExperiment = new Workflow();
//		
//		ArrayList<Integer> fingerprintList = new ArrayList<Integer>();
//		fingerprintList.add(1);
//		fingerprintList.add(4);
//		
//		int i = 0;
//		for(int j = 3; j < 11 ; j++){
//			for(int fp : fingerprintList){
//				i++;
//				String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
//				System.out.println(i + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//				workflowForExperiment.workFlowExperiment(i, j, fp);
//			}
//		}
//		
//		for(int k = 1; k < 11 ; k++){
//			for(int fp : fingerprintList){
//				i++;
//				String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
//				System.out.println(i + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//				workflowForExperiment.workFlowExperiment(i, k, fp);
//			}
//		}
//		
//		for(int j = 3; j < 11 ; j++){
//			for(int fp : fingerprintList){
//				i++;
//				String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
//				System.out.println(i + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//				workflowForExperiment.workFlowExperiment(i, j, fp);
//			}
//		}
//		
//		for(int k = 1; k < 11 ; k++){
//			for(int fp : fingerprintList){
//				i++;
//				String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
//				System.out.println(i + "��° experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
//				workflowForExperiment.workFlowExperiment(i, k, fp);
//			}
//		}
		
		
//		File zipFile = new File("C:\\Users\\shin\\Desktop\\1.zip");
//		FileDataImport.getInstance().unZipAndSaveZipFile(zipFile);
		
	}
	
}
