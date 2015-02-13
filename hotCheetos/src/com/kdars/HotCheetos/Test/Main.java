package com.kdars.HotCheetos.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Experiment.Experiment;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_noun_hashcode;
import com.kdars.HotCheetos.WorkFlow.Workflow;

public class Main {
	private static int nGramSetting = 2;
	private static int fingerprintSetting = 1;
	
	private static String postFix1 = Configuration.getInstance().getPostFix1();
	private static String postFix2 = Configuration.getInstance().getPostFix2();
	
	public static void main(String[] args) {
		
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
		
		Workflow workflowForExperiment = new Workflow();
		
		
//		workflowForExperiment.workFlowExperiment(51, 10, 1);
		

		int i = 0;
		for(int j = 3; j < 11 ; j++){
			i++;
			String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, j, 1);
			
			i++;
			experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, j, 4);
		}
		
		for(int k = 1; k < 11 ; k++){
			i++;
			String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, k, 1);
			
			i++;
			experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, k, 4);
		}
		
		for(int j = 3; j < 11 ; j++){
			i++;
			String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, j, 1);
			
			i++;
			experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, j, 4);
		}
		
		for(int k = 1; k < 11 ; k++){
			i++;
			String experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, k, 1);
			
			i++;
			experimentTableName = DBManager.getInstance().convertIDtoName_InvertedIndex(i);
			System.out.println(i + "번째 experiment (" + experimentTableName.substring(0,experimentTableName.length()-19) + ")\n");
			workflowForExperiment.workFlowExperiment(i, k, 4);
		}
		
		
//		File zipFile = new File("C:\\Users\\shin\\Desktop\\1.zip");
//		FileDataImport.getInstance().unZipAndSaveZipFile(zipFile);
		
	}
	
}
