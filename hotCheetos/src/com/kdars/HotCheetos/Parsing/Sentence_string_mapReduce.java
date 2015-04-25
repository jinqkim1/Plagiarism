package com.kdars.HotCheetos.Parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DocumentStructure.DocInfo;
import com.kdars.HotCheetos.DocumentStructure.ObjectFileConverter;
import com.kdars.HotCheetos.DocumentStructure.SenInfo;

public class Sentence_string_mapReduce {

	private String preFix = Configurations.getInstance().getPreFix();
	private String postFix1 = Configurations.getInstance().getPostFix1();
	private String postFix2 = Configurations.getInstance().getPostFix2();

	/* Temporary measure for experiment. Need to delete!!!! */
	private int fingerprintSetting = Configurations.getInstance()
			.getFingerprintSetting();

	/* Temporary measure for experiment. Need to delete!!!! */

	public MapWritable parseDoc(int docID, String content) {
		// output of this method : MapWritable<sentenceID, MapWritable<term,termFreq>>
		// output format : MapWritable<LongWritable, MapWritable<Text,IntWritable>>
		// output contains all the terms and term frequencies in all the sentences in a document
		// Also, this method saves a Serializable object containing sentence location information. object file number and docids are matched and saved, so docids can be used to identify which object to read.
		// This object will later be used in SimScoreSentenceMappers to identify the locations of the similar sentences between two documents
		
		DocInfo docInfo = new DocInfo(String.valueOf(docID), "");  //Ommitting title for now.
		
		MapWritable sentenceMap = new MapWritable(); // key : sentenceID , value : Map of (term, termFreq) <--termFreqMap

		String sentenceList[] = content.trim().split("\\.");

		int newLineChecker = 0;

		LongWritable sentenceID = new LongWritable();

		try {
			
			//AddResource for permission to write to hdfs
			Configuration conf = new Configuration();
			conf.addResource(new Path("/HADOOP_HOME/conf/core-site.xml"));
			conf.addResource(new Path("/HADOOP_HOME/conf/hdfs-site.xml"));
			
			FileSystem hdfs = FileSystem.get(conf);

			Path workingDir = hdfs.getWorkingDirectory();

			Path newFolderPath = new Path("/" + Configurations.getInstance().getDocInfoPathString());

			newFolderPath = Path.mergePaths(workingDir, newFolderPath);
			
			// if the directory does not exist, then create it
			if (!hdfs.exists(newFolderPath)) {
				hdfs.mkdirs(newFolderPath); // Create new Directory
			}
			
			Path newFilePath=new Path(newFolderPath+"/" + String.valueOf(docID) + ".dat");
			
			// if the file exists, delete then create it
			if(hdfs.exists(newFilePath)){
				hdfs.delete(newFilePath, true);
			}
			hdfs.createNewFile(newFilePath);

			FSDataOutputStream fsOutStream = hdfs.create(newFilePath);
			
			sentenceID.set(0);
			for (String sentence : sentenceList) {
				
				int lines = 0;
				if (sentence.contains("\n")) {
					Matcher m = Pattern.compile("//n").matcher(sentence);
					while (m.find()) {
						if (sentence.substring(0, m.start()).trim().equals("")) {
							newLineChecker++;
							continue;
						}
						lines++;
					}
				}

				sentenceID.set(sentenceID.get() + 1);
				MapWritable termFreqMap = parseSentence(sentence); // key : term , value : termFreq within sentence
				if (termFreqMap != null) {
					int senID = Integer.valueOf(sentenceID.toString());
					SenInfo senInfo = new SenInfo();
					senInfo.sentenceText = sentence.trim();
					
					ArrayList<Integer> sentenceLines = new ArrayList<Integer>();
					for (int i = 0; i <= lines; i++) {
						sentenceLines.add(newLineChecker);
						newLineChecker++;
					}
					
					senInfo.sentenceLines = sentenceLines;
					docInfo.sentenceMap.put(senID, senInfo);
					sentenceMap.put(sentenceID, termFreqMap);
				}

			}
			
			if(!new ObjectFileConverter<DocInfo>().object2File(docInfo, fsOutStream)){
				System.out.println("Failed to convert object to file!");
			}
			
			fsOutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sentenceMap;
	}

	private MapWritable parseSentence(String sentence) {
		MapWritable termFreqMap = new MapWritable();

		String wordList[] = sentence.trim().split("\\s+");

		if (wordList.length < 5) {
			return null;
		}

		for (int i = 0; i < wordList.length; i++) {
			int postFixChecker = wordList[i].trim().length();
			
			String word = deletePostFix(wordList[i].trim());
			if (word.length() != postFixChecker) {
				addTerm(termFreqMap, word);
			}
		}

		return termFreqMap;
	}

	private String deletePostFix(String processedString) {

		if (processedString.length() > 3 && !processedString.substring(0, 1).matches(preFix)) {
			int wordLen = processedString.length();

			if (processedString.substring(wordLen - 2).matches(postFix2)) {
				return processedString.substring(0, wordLen - 2);
			}

			if (processedString.substring(wordLen - 1).matches(postFix1)) {
				return processedString.substring(0, wordLen - 1);
			}
		}

		return processedString;
	}

	private void addTerm(MapWritable termFreqMap, String word) {
		if (word.hashCode() % this.fingerprintSetting != 0) {
			return;
		}

		Text word_textFormat = new Text();
		word_textFormat.set(word);

		IntWritable value = new IntWritable();
		if (termFreqMap.containsKey(word_textFormat)) {
			value = (IntWritable) termFreqMap.get(word_textFormat);
			value.set(value.get() + 1);
			termFreqMap.put(word_textFormat, value);
			return;
		}
		value.set(1);
		termFreqMap.put(word_textFormat, value);
	}

}
