package com.kdars.HotCheetos.DataImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.TextExtractor.MS_PDF_TextExtractors;


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
	
	private ArrayList<String> unZipAndExtractContent(File zipFile) {
		byte[] buffer = new byte[1024];
		String outputFolder = "D:\\UnzipTestComplete";
		ArrayList<String> titleAndContent = new ArrayList<String>();
		String content = null;
		
		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("UTF8"));
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
				
//				content = MS_PDF_TextExtractors.getInstance().chooseFileTypeAndExtract(outputFolder + File.separator + fileName);
				titleAndContent.add(fileName);
				titleAndContent.add(content);
				
				File forDelete = new File(outputFolder + File.separator + fileName);
				if(forDelete.exists()){
					forDelete.delete();
				}
				
			}

			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return titleAndContent;
	}
	
	private String extractOnlyText(String text){
		StringBuilder result = new StringBuilder();
		Pattern p = Pattern.compile(extractTextPattern);
		Matcher m = p.matcher(text);
		
		while(m.find()){
			result.append(String.valueOf(text.charAt(m.start())));
		}
		
		return result.toString();
	}
	
	private int saveAndGetDocInfo(String title, String processedContent){
		return DBManager.getInstance().insertRowAndGetDocIDArray(title, processedContent);
	}
	
	public ArrayList<Integer> importProcessor(ArrayList<File> zipFileList){
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		for(File zipFile : zipFileList){
			ArrayList<String> titleAndContent = unZipAndExtractContent(zipFile);
			String title = titleAndContent.get(0);
			String processedContent = extractOnlyText(titleAndContent.get(1));
			docIDList.add(saveAndGetDocInfo(title, processedContent));
		}
		return docIDList;
	}
	
}
