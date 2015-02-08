package com.kdars.HotCheetos.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.DataImport.FileDataImport;
import com.kdars.HotCheetos.Experiment.Experiment;
import com.kdars.HotCheetos.WorkFlow.Workflow;

public class Main {

	public static void main(String[] args) {
		/*
		System.out.println("\nexperiment1");
		Experiment.getInstance().experiment1();
		System.out.println("\nexperiment2");
		Experiment.getInstance().experiment2();
		System.out.println("\nexperiment3");
		Experiment.getInstance().experiment3();
		System.out.println("\nexperiment4");
		Experiment.getInstance().experiment4();
		System.out.println("\nexperiment5");
		Experiment.getInstance().experiment5();
		System.out.println("\nexperiment6");
		Experiment.getInstance().experiment6();
		System.out.println("\nexperiment7");
		Experiment.getInstance().experiment7();
		System.out.println("\nexperiment8");
		Experiment.getInstance().experiment8();
		*/
		
		File zipFile = new File("C:\\Users\\shin\\Desktop\\1.zip");
		FileDataImport.getInstance().unZipAndSaveZipFile(zipFile);
		
	}
}
