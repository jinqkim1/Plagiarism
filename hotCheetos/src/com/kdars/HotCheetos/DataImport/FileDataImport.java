package com.kdars.HotCheetos.DataImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FileDataImport implements ImportContent {

	private static  FileDataImport fileDataImport = new FileDataImport();
	public static FileDataImport getInstance(){
		return	fileDataImport;
	}
	
	@Override
	public String importContent(int src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String importDocument(String fileName) {
		File file = new File(fileName); 
		
		char[] c = new char[(int) file.length()]; 
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
			br.read(c);
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String content =  new String(c);
		
		
		//content = removeStopWord(content);
		

		return content;
	}
	
	public ArrayList<String> getFileNamesFromDir(String path) {
		ArrayList<String> filenames = new ArrayList<String>();

		File directory = new File(path);
		File[] files = directory.listFiles();
		for (File file : files) {
			filenames.add(path+file.getName());
		}
		return filenames;
	}

	public ArrayList<String> getFilesFromFileNames(ArrayList<String> fileNames) {
		ArrayList<String> contens = new ArrayList<String>();
		
		for(int i=0; i< fileNames.size(); ++i){
			contens.add(importDocument(fileNames.get(i)));
		}
		
		return contens;
	}

}
