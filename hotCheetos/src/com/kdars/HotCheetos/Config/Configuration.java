package com.kdars.HotCheetos.Config;

public class Configuration {
	
	private static  Configuration settings = new Configuration();
	public static Configuration getInstance(){
		return	settings;
	}
	
	/* special character pattern setting */
	private String postFix1 = "은|는|이|가|을|를|에|의|도|만|로|와|과";
	public String getPostFix1(){
		return this.postFix1;
	}
	
	private String postFix2 = "에서|에게|한테|로서|로써|께서|까지|조차|부터|마저";
	public String getPostFix2(){
		return this.postFix2;
	}
	/* special character pattern setting */
	
	/* N-gram setting */
	private int windowSize = 3;
	public int getNgramSetting(){  //N-gram의 size를 windowSize로 setting.
		return this.windowSize;
	}
	/* N-gram setting */
	
	/* Finger-printing setting */
	public int mod = 4;
	public int getFingerprintSetting(){  //mod의 배수로 hashcode 선택하는 동작을 위한 setting.
		return this.mod;
	}
	
	private int substringSize = 15;
	public int getSubstringSetting(){  //substringSize의 갯수만큼의 캐릭터로 substring 생성하는 동작을 위한 setting.
		return this.substringSize;
	}
	/* Finger-printing setting */
	
	/* Similarity score threshold setting for clustering */
	private double scoreThreshold = 0.9;
	public double getSimScoreThreshold(){
		return this.scoreThreshold;
	}
	/* Similarity score threshold setting for clustering */
	
	/* limit settings to prevent excessive memory usage */
	private int fileListLimit = 50; //input으로 받는 file list가 limit을 넘으면 잘라서 처리할 수 있도록 하여 메모리 문제 해결.
	public int getFileListLimit(){
		return this.fileListLimit;
	}
	
	private int docIDListLimit = 100; //docID list size limit to prevent long queries in similarity score retrieval.
	public int getDocIDListLimit(){
		return this.docIDListLimit;
	}
	
	private int docInfoListLimit = 100; //Document Info의 갯수가 limit을 넘으면 잘라서 처리할 수 있도록 하여 메모리 문제 해결.
	public int getDocInfoListLimit(){
		return this.docInfoListLimit;
	}
	
	private int bulkScoreLimit = 500000; //DB에 저장할 score의 갯수가 limit을 넘으면 잘라서 처리할 수 있도록 하여 메모리 문제 해결.
	public int getbulkScoreLimit(){
		return this.bulkScoreLimit;
	}
	/* limit settings to prevent excessive memory usage */
	
	/* Characters to be extracted */
	//영어, 한글, whitespace, 마침표만 남기고 나머지는 다 버림.
	private String extractTextPattern = "[\\x{AC00}-\\x{D7A3}_\\x{0020}_\\x{002E}_\\x{0041}-\\x{005A}_\\x{0061}-\\x{007A}]";
	public String getTextPattern(){
		return this.extractTextPattern;
	}
	/* Characters to be extracted */
	
	/* DB Connect Info */
	public final String DB_JDBC_URL = "jdbc:mysql://192.168.1.4:3306/GraphDB";
	public final String DB_NAME = "plagiarismdb"; 
	public final String DB_USER_ID = "root";
	public final String DB_USER_PASS = "jinqkim69";
	public final String DB_TABLE_NAME_TEXT = "texttable";
	public final String DB_TABLE_NAME_STOPWORD = "stopwordtable";
	
	public final String DB_TABLE_NAME_INDEX = "invertedindextable";
	public final String DB_TABLE_NAME_LOCATION = "charlocationtable";
	public final String DB_TABLE_NAME_SCORE = "simscoretable";
	
	public final String DB_TABLE_NAME_INDEX1 = "invertedindextable";
	public final String DB_TABLE_NAME_LOCATION1 = "charlocationtable";
	public final String DB_TABLE_NAME_SCORE1 = "simscoretable";
	
	public final String DB_TABLE_NAME_INDEX2 = "invertedindextable";
	public final String DB_TABLE_NAME_LOCATION2 = "charlocationtable";
	public final String DB_TABLE_NAME_SCORE2 = "simscoretable";
	/* DB Connect Info */
	
}
