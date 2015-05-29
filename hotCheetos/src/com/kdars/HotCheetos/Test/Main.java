package com.kdars.HotCheetos.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
//import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Experiment.Experiment;
import com.kdars.HotCheetos.MapReduce.PdfDriver;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;
import com.kdars.HotCheetos.Parsing.Parse1_noun_hashcode;
import com.kdars.HotCheetos.Server.UserControl;
import com.kdars.HotCheetos.WorkFlow.Workflow;

public class Main {
	private static int nGramSetting = 2;
	private static int fingerprintSetting = 1;
	
	private static String postFix1 = Configurations.getInstance().getPostFix1();
	private static String postFix2 = Configurations.getInstance().getPostFix2();
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
//	
//		Runtime rt = Runtime.getRuntime();
//		Process pr = rt.exec("hadoop jar /usr/data/test.jar com.kdars.HotCheetos.Test /user/hadoop/num_5 /user/hadoop/out_num_5"); 
//
//		BufferedReader input = new BufferedReader(new InputStreamReader(
//				pr.getInputStream()));
//		String line = null;
//
//		try {
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		input = new BufferedReader(new InputStreamReader(
//				pr.getInputStream()));
//		line = null;
//
//		try {
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		
//		
//		pr = rt.exec("su");
//		pr = rt.exec("tlsgywjd77");
//		pr = rt.exec("hadoop jar /usr/data/test.jar com.kdars.HotCheetos.Test /user/hadoop/num_5 /user/hadoop/out_num_5");
//		input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//		line = null;
//
//		try {
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		UserControl uc = new UserControl();
//		uc.run();
//		
//		
		
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
		
	}
	
}
