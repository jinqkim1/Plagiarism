package com.kdars.HotCheetos.DataImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.PDFParser.PDFFileParser;


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
	
	public void unZipAndSaveZipFile(File zipFile) {
		byte[] buffer = new byte[1024];
		String outputFolder = "D:\\UnzipTestComplete";

		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				
				len = 0;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
				
				
				double initial = System.currentTimeMillis();
				double finall = System.currentTimeMillis();	
				
				initial = System.currentTimeMillis();
				String content = PDFFileParser.getInstance().PdfFileParser(outputFolder + File.separator + fileName);
				finall = System.currentTimeMillis();
				System.out.println("pdf parsing 하는데 걸린 시간  :  " + (finall - initial)/1000 + "초");
				System.out.println(content);
				
				
			}

			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String extractOnlyText(String text){
		StringBuilder result = new StringBuilder();
		Pattern p = Pattern.compile(extractTextPattern);
		Matcher m = p.matcher(text);
		
		while(m.find()){
			result.append(String.valueOf(text.charAt(m.start())));
		}
		
		return result.toString();
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
