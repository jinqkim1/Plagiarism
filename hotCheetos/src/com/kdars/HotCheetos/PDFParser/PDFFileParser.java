package com.kdars.HotCheetos.PDFParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class PDFFileParser {
	
	private static  PDFFileParser pDFFileParser = new PDFFileParser();
	public static PDFFileParser getInstance(){
		return	pDFFileParser;
	}
	
	public String PdfFileParser(String pdffilePath) throws FileNotFoundException, IOException {
		String content = null;
		try{
		
		FileInputStream fi = new FileInputStream(new File(pdffilePath));
		PDFParser parser = new PDFParser(fi);
		parser.parse();
		COSDocument cd = parser.getDocument();
		PDFTextStripper stripper = new PDFTextStripper();
		content = stripper.getText(new PDDocument(cd));
		cd.close();
		
		}catch(Exception e){
			System.out.println("pdf parse error : "+pdffilePath);
		}
		
		return content;
	}

}
