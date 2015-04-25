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
	
		int a;
		double start = System.currentTimeMillis();
		Workflow workflowForExperiment = new Workflow();
		
		String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(77);
		System.out.println(77 + " experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
		
		int jobComplete = workflowForExperiment.TEMPORARYprismWorkFlow(args);
		
		System.out.println("���� ������ text table drop �� �ٽ� create �ؾ���!!!!!!!!!!!!");
		System.out.println("���� ������ Mapper���� DB table id �̸� �ٲ������!!!!!!!!!!!!!");
		double end = System.currentTimeMillis();
		System.out.println("Total calculation time!!!!!!!!! : " + (end-start)/1000 + " seconds");
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
