package com.kdars.HotCheetos.TextExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;


public class MS_PDF_TextExtractors {
	
	private static  MS_PDF_TextExtractors pDFFileParser = new MS_PDF_TextExtractors();
	public static MS_PDF_TextExtractors getInstance(){
		return	pDFFileParser;
	}
	
	public String chooseFileTypeAndExtract(String filePath){
		String content = null;
		String fileType = filePath.substring(filePath.lastIndexOf('.')+1, filePath.length());
		
		if (fileType.equals("doc")){
			content = OLE2wordFileParser(filePath);
		}else if (fileType.equals("docx")){
			content = OOXMLwordFileParser(filePath);
		}else if (fileType.equals("ppt")){
			content = OLE2pptFileParser(filePath);
		}else if (fileType.equals("pptx")){
			content = OOXMLpptFileParser(filePath);
		}else if (fileType.equals("xls")){
			content = OLE2excelFileParser(filePath);
		}else if (fileType.equals("xlsx")){
			content = OOXMLexcelFileParser(filePath);
		}else if (fileType.equals("pdf")){
			content = PdfFileParser(filePath);
		}else{
			System.out.println(fileType);
		}
		
		return content;
	}
	
	private String PdfFileParser(String pdffilePath){
		String content = null;
		FileInputStream fi = null;
		PDFParser parser = null;
		COSDocument cd = null;
		PDDocument pddoc = null;
		PDFTextStripper stripper = null;
		try {

			fi = new FileInputStream(new File(pdffilePath));
			parser = new PDFParser(fi);
			parser.parse();
			cd = parser.getDocument();
			pddoc = new PDDocument(cd);
			if (pddoc.isEncrypted()) {  //encryption 있으면, 풀고 진행.
				try {
					pddoc.decrypt("");
					pddoc.setAllSecurityToBeRemoved(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			stripper = new PDFTextStripper();
			content = stripper.getText(pddoc);
            fi.close();
			cd.close();
			pddoc.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pdf parse error : " + pdffilePath);
		}
		System.gc();
		return content;
	}
	
	private String OLE2pptFileParser(String filePath){
		PowerPointExtractor pptExtractor = null;
		String content = null;
		try {
			pptExtractor = new PowerPointExtractor(new FileInputStream(new File(filePath)));
			content = pptExtractor.getText();
			pptExtractor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private String OOXMLpptFileParser(String filePath) {
		File file = null;
		XSLFSlideShow pptx = null;
		XSLFPowerPointExtractor pptExtractor = null;
		String content = null;
		try {
			file = new File(filePath);
			pptx = new XSLFSlideShow(file.toString());
			pptExtractor = new XSLFPowerPointExtractor(pptx);
			content = pptExtractor.getText();
			pptExtractor.close();
		} catch (IOException | OpenXML4JException | XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private String OLE2wordFileParser(String filePath){
		HWPFDocument doc = null;
		WordExtractor wrdExtractor = null;
		String content = null;
		try{
			doc = new HWPFDocument(new FileInputStream(new File(filePath)));
			wrdExtractor = new WordExtractor(doc);
			content = wrdExtractor.getText();
			wrdExtractor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private String OOXMLwordFileParser(String filePath){
		XWPFDocument docx = null;
		XWPFWordExtractor wrdExtractor = null;
		String content = null;
		try {
			docx = new XWPFDocument(new FileInputStream(new File(filePath)));
			wrdExtractor = new XWPFWordExtractor(docx);
			content = wrdExtractor.getText();
			wrdExtractor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	public String OLE2excelFileParser(String filePath) {
		InputStream file = null;
		ExcelExtractor extractor = null;
		String content = null;
		try {
			file = new FileInputStream(new File(filePath));
			HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file));
			extractor = new ExcelExtractor(workbook);

		    extractor.setFormulasNotResults(true);
		    extractor.setIncludeSheetNames(false);
		    content = extractor.getText();
		    extractor.close();
		    file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	private String OOXMLexcelFileParser(String filePath){
		StringBuilder content = new StringBuilder();
		 try
	        {
	            FileInputStream file = new FileInputStream(new File(filePath));
	 
	            //Create Workbook instance holding reference to .xlsx file
	            XSSFWorkbook workbook = new XSSFWorkbook(file);
	            int numberOfSheets = workbook.getNumberOfSheets();
	            //Get first/desired sheet from the workbook
	            for (int i = 0; i < numberOfSheets ; i++){
	            	XSSFSheet sheet = workbook.getSheetAt(i);
	           	 	
		            //Iterate through each rows one by one
		            Iterator<Row> rowIterator = sheet.iterator();
		            while (rowIterator.hasNext()) {
		                Row row = rowIterator.next();
		                //For each row, iterate through all the columns
		                Iterator<Cell> cellIterator = row.cellIterator();
		                 
		                while (cellIterator.hasNext()) {
		                    Cell cell = cellIterator.next();
		                    //Check the cell type and format accordingly
		                    switch (cell.getCellType()) 
		                    {
		                        case Cell.CELL_TYPE_NUMERIC:
		                        	content.append(cell.getNumericCellValue() + " ");
		                            break;
		                        case Cell.CELL_TYPE_STRING:
		                        	content.append(cell.getStringCellValue() + " ");
		                        	break;
		                    }
		                }
		                content.append("\n");
		            }
	            }
	            workbook.close();
	            file.close();
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
		 
		return content.toString();
	}
	
}
