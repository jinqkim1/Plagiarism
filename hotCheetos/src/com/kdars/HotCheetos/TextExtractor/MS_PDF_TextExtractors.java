package com.kdars.HotCheetos.TextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
	
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";
	
	public String chooseFileTypeAndExtract(String filePath, Configuration conf){
		String content = null;
		String fileType = filePath.substring(filePath.lastIndexOf('.')+1, filePath.length());
		
		if (fileType.equals("doc") || fileType.equals("dot")){
			content = OLE2wordFileParser(filePath, conf);
		}else if (fileType.equals("docx") || fileType.equals("docm") || fileType.equals("dotm")){
			content = OOXMLwordFileParser(filePath, conf);
		}else if (fileType.equals("ppt")){
			content = OLE2pptFileParser(filePath, conf);
		}else if (fileType.equals("pptx") || fileType.equals("pptm") || fileType.equals("ppsm")){
			content = OOXMLpptFileParser(filePath);
		}else if (fileType.equals("xls")){
			content = OLE2excelFileParser(filePath, conf);
		}else if (fileType.equals("xlsx") || fileType.equals("xlsm") || fileType.equals("xlsb")){
			content = OOXMLexcelFileParser(filePath, conf);
		}else if (fileType.equals("pdf")){
			content = PdfFileParser(filePath, conf);
		}else if (fileType.equals("txt")){
			content = txtFileParser(filePath, conf);
		}else{
			System.out.println(fileType);
		}
		
		return content;
	}
	
	private String PdfFileParser(String pdffilePath, Configuration conf){
		Path path = new Path(pdffilePath);
		StringBuilder processingContent = new StringBuilder();
		
		String content = null;
		FileSystem fs = null;
		FSDataInputStream filein = null;
		PDDocument doc = null;
		PDFTextStripper stripper = null;
		try {

			fs = path.getFileSystem(conf);

			filein = fs.open(path);
			doc = PDDocument.loadNonSeq(filein, null);
			stripper = new PDFTextStripper();
			content = stripper.getText(doc);

			String[] processingLines = content.trim().split("\\r?\\n");

			for (String oneLine : processingLines) {
				processingContent.append(textExtractor(oneLine) + "\n");
			}
			
//			fi = new FileInputStream(new File(pdffilePath));
//			parser = new PDFParser(fi);
//			parser.parse();
//			cd = parser.getDocument();
//			pddoc = new PDDocument(cd);
//			if (pddoc.isEncrypted()) {  //encryption ������, Ǯ�� ����.
//				try {
//					pddoc.decrypt("");
//					pddoc.setAllSecurityToBeRemoved(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			stripper = new PDFTextStripper();
//			content = stripper.getText(pddoc);
			doc.close();
			filein.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pdf parse error : " + pdffilePath);
		}
		return processingContent.toString();
	}
	
	private String OLE2pptFileParser(String filePath, Configuration conf){
		PowerPointExtractor pptExtractor = null;
		String content = null;
		try {
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);
			pptExtractor = new PowerPointExtractor(fs.open(path));
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
	
	private String OLE2wordFileParser(String filePath, Configuration conf){
		HWPFDocument doc = null;
		WordExtractor wrdExtractor = null;
		String content = null;
		try{
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);
			doc = new HWPFDocument(fs.open(path));
			wrdExtractor = new WordExtractor(doc);
			content = wrdExtractor.getText();
			wrdExtractor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private String OOXMLwordFileParser(String filePath, Configuration conf){
		XWPFDocument docx = null;
		XWPFWordExtractor wrdExtractor = null;
		String content = null;
		try {
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);
			docx = new XWPFDocument(fs.open(path));
			wrdExtractor = new XWPFWordExtractor(docx);
			content = wrdExtractor.getText();
			wrdExtractor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	public String OLE2excelFileParser(String filePath, Configuration conf) {
		InputStream file = null;
		ExcelExtractor extractor = null;
		String content = null;
		try {
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);
			file = fs.open(path);
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
	
	private String OOXMLexcelFileParser(String filePath, Configuration conf) {
		StringBuilder content = new StringBuilder();
		try {
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(fs.open(path));
			int numberOfSheets = workbook.getNumberOfSheets();
			// Get first/desired sheet from the workbook
			for (int i = 0; i < numberOfSheets; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						// Check the cell type and format accordingly
						switch (cell.getCellType()) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content.toString();
	}
	
	private String txtFileParser(String filePath, Configuration conf){
		StringBuilder processingContent = new StringBuilder();
		
		try {
			Path path = new Path(filePath);
			FileSystem fs = path.getFileSystem(conf);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));

			String line;
			line = br.readLine();
			while (line != null){
				processingContent.append(textExtractor(line) + "\n");
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processingContent.toString();
	}
	
	private String textExtractor(String str){
		StringBuilder result = new StringBuilder();
		Pattern p = Pattern.compile(extractTextPattern);
		Matcher m = p.matcher(str);
		
		while(m.find()){
			result.append(String.valueOf(str.charAt(m.start())));		
		}
		
		return result.toString();
	}
}
