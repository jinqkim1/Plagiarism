package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocInfo;
import com.kdars.HotCheetos.DocumentStructure.ObjectFileConverter;
import com.kdars.HotCheetos.TextExtractor.MS_PDF_TextExtractors;

public class PdfMapper extends Mapper<Text, Text, LongWritable, Text>{
	
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";

	@Override
	public void map(Text title, Text file, Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		conf.addResource(new Path(Configurations.getInstance().getCoreSiteXmlLocation()));
		conf.addResource(new Path(Configurations.getInstance().getHdfsSiteXmlLocation()));
		
		if(DBManager.getInstance().checkFile(file.toString())){
			return;
		}
		
		LongWritable docID = new LongWritable();
		
		String processingContent = MS_PDF_TextExtractors.getInstance().chooseFileTypeAndExtract(file.toString(), conf);
		
		try {
			Path path = new Path(file.toString());
			long value = DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(path.getName(), processingContent);
			
			/* Creating docInfo to use in ParsingSentenceMapper later*/
			saveDocInfo(value, title.toString(), conf);
			/* Creating docInfo to use in ParsingSentenceMapper later*/
			
			docID.set(value);
			
		} catch (Exception ex) {
			DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `ex`='exception1' where `type`='"+file.toString()+"'");
		}
		
		long time  = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		
		Text processedContent = new Text();
		
		processedContent.set(processingContent.toString());
		
		context.write(docID, processedContent);

		
		time  = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+file.toString()+"'");
	
		
		
	}
	
	private String escape(String text) {
		String result = StringEscapeUtils.escapeHtml4(text);
		result = result.replaceAll("'", "`");
		return result;
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
	
	// Creating and saving docInfo to use in ParsingSentenceMapper later
	// file name : docID.dat
	// DocInfo only has docID and the title at this point
	private void saveDocInfo(long docID, String title, Configuration conf){
		
		DocInfo docInfo = new DocInfo(String.valueOf(docID), title.toString());  //Ommitting title for now.
		
		FSDataOutputStream fsOutStream = null;
		
		try {
			FileSystem hdfs = FileSystem.get(conf);
			
			Path newFolderPath = new Path(Configurations.getInstance().getDocInfoPathString());
			
			// if the directory does not exist, then create it
			if (!hdfs.exists(newFolderPath)) {
				hdfs.mkdirs(newFolderPath); // Create new Directory
				hdfs.setPermission(newFolderPath, new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL)); //Enable all access to the directory
			}
			
			Path newFilePath=new Path(newFolderPath+"/" + String.valueOf(docID) + ".dat");
			
			// if the file exists, delete then create it
			if(hdfs.exists(newFilePath)){
				hdfs.delete(newFilePath, true);
			}
			hdfs.createNewFile(newFilePath);
			hdfs.setPermission(newFilePath, new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL)); //Enable all access to the created file

			fsOutStream = hdfs.create(newFilePath);
			
			if(!new ObjectFileConverter<DocInfo>().object2File(docInfo, fsOutStream)){
				System.out.println("Failed to convert object to file!");
			}
			
			fsOutStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
}