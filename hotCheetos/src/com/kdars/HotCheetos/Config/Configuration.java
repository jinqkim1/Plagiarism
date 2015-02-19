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
	public void setNgramSetting(int windowSize){
		this.windowSize = windowSize;
	}
	/* N-gram setting */
	
	/* Finger-printing setting */
	private int mod = 4;
	public int getFingerprintSetting(){  //mod의 배수로 hashcode 선택하는 동작을 위한 setting.
		return this.mod;
	}
	public void setFingerprintSetting(int mod){
		this.mod = mod;
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
	private int fileListLimit = 25; //input으로 받는 file list가 limit을 넘으면 잘라서 처리할 수 있도록 하여 메모리 문제 해결.
	public int getFileListLimit(){
		return this.fileListLimit;
	}
	
	private int docIDListLimit = 25; //docID list size limit to prevent long queries in similarity score retrieval.
	public int getDocIDListLimit(){
		return this.docIDListLimit;
	}
	
	private int docInfoListLimit = 25; //Document Info의 갯수가 limit을 넘으면 잘라서 처리할 수 있도록 하여 메모리 문제 해결.
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
	
		/* experiment DB */
	//table ID == 1
	public final String DB_TABLE_NAME_INDEX1 = "3-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION1 = "3-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE1 = "3-gram_hashcode_simscoretable";
	
	//table ID == 2
	public final String DB_TABLE_NAME_INDEX2 = "3-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION2 = "3-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE2 = "3-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 3
	public final String DB_TABLE_NAME_INDEX3 = "4-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION3 = "4-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE3 = "4-gram_hashcode_simscoretable";
	
	//table ID == 4
	public final String DB_TABLE_NAME_INDEX4 = "4-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION4 = "4-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE4 = "4-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 5
	public final String DB_TABLE_NAME_INDEX5 = "5-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION5 = "5-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE5 = "5-gram_hashcode_simscoretable";
	
	//table ID == 6
	public final String DB_TABLE_NAME_INDEX6 = "5-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION6 = "5-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE6 = "5-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 7
	public final String DB_TABLE_NAME_INDEX7 = "6-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION7 = "6-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE7 = "6-gram_hashcode_simscoretable";
	
	//table ID == 8
	public final String DB_TABLE_NAME_INDEX8 = "6-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION8 = "6-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE8 = "6-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 9
	public final String DB_TABLE_NAME_INDEX9 = "7-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION9 = "7-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE9 = "7-gram_hashcode_simscoretable";
	
	//table ID == 10
	public final String DB_TABLE_NAME_INDEX10 = "7-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION10 = "7-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE10 = "7-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 11
	public final String DB_TABLE_NAME_INDEX11 = "8-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION11 = "8-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE11 = "8-gram_hashcode_simscoretable";
	
	//table ID == 12
	public final String DB_TABLE_NAME_INDEX12 = "8-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION12 = "8-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE12 = "8-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 13
	public final String DB_TABLE_NAME_INDEX13 = "9-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION13 = "9-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE13 = "9-gram_hashcode_simscoretable";
	
	//table ID == 14
	public final String DB_TABLE_NAME_INDEX14 = "9-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION14 = "9-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE14 = "9-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 15
	public final String DB_TABLE_NAME_INDEX15 = "10-gram_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION15 = "10-gram_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE15 = "10-gram_hashcode_simscoretable";
	
	//table ID == 16
	public final String DB_TABLE_NAME_INDEX16 = "10-gram_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION16 = "10-gram_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE16 = "10-gram_hashcode_fingerprint_simscoretable";
	
	//table ID == 17
	public final String DB_TABLE_NAME_INDEX17 = "1-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION17 = "1-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE17 = "1-gram_noun_hashcode_simscoretable";
	
	//table ID == 18
	public final String DB_TABLE_NAME_INDEX18 = "1-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION18 = "1-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE18 = "1-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 19
	public final String DB_TABLE_NAME_INDEX19 = "2-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION19 = "2-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE19 = "2-gram_noun_hashcode_simscoretable";
	
	//table ID == 20
	public final String DB_TABLE_NAME_INDEX20 = "2-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION20 = "2-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE20 = "2-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 21
	public final String DB_TABLE_NAME_INDEX21 = "3-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION21 = "3-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE21 = "3-gram_noun_hashcode_simscoretable";
	
	//table ID == 22
	public final String DB_TABLE_NAME_INDEX22 = "3-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION22 = "3-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE22 = "3-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 23
	public final String DB_TABLE_NAME_INDEX23 = "4-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION23 = "4-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE23 = "4-gram_noun_hashcode_simscoretable";
	
	//table ID == 24
	public final String DB_TABLE_NAME_INDEX24 = "4-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION24 = "4-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE24 = "4-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 25
	public final String DB_TABLE_NAME_INDEX25 = "5-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION25 = "5-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE25 = "5-gram_noun_hashcode_simscoretable";
	
	//table ID == 26
	public final String DB_TABLE_NAME_INDEX26 = "5-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION26 = "5-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE26 = "5-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 27
	public final String DB_TABLE_NAME_INDEX27 = "6-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION27 = "6-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE27 = "6-gram_noun_hashcode_simscoretable";
	
	//table ID == 28
	public final String DB_TABLE_NAME_INDEX28 = "6-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION28 = "6-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE28 = "6-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 29
	public final String DB_TABLE_NAME_INDEX29 = "7-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION29 = "7-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE29 = "7-gram_noun_hashcode_simscoretable";
	
	//table ID == 30
	public final String DB_TABLE_NAME_INDEX30 = "7-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION30 = "7-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE30 = "7-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 31
	public final String DB_TABLE_NAME_INDEX31 = "8-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION31 = "8-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE31 = "8-gram_noun_hashcode_simscoretable";
	
	//table ID == 32
	public final String DB_TABLE_NAME_INDEX32 = "8-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION32 = "8-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE32 = "8-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 33
	public final String DB_TABLE_NAME_INDEX33 = "9-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION33 = "9-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE33 = "9-gram_noun_hashcode_simscoretable";
	
	//table ID == 34
	public final String DB_TABLE_NAME_INDEX34 = "9-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION34 = "9-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE34 = "9-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 35
	public final String DB_TABLE_NAME_INDEX35 = "10-gram_noun_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION35 = "10-gram_noun_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE35 = "10-gram_noun_hashcode_simscoretable";
	
	//table ID == 36
	public final String DB_TABLE_NAME_INDEX36 = "10-gram_noun_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION36 = "10-gram_noun_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE36 = "10-gram_noun_hashcode_fingerprint_simscoretable";
	
	//table ID == 37
	public final String DB_TABLE_NAME_INDEX37 = "3-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION37 = "3-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE37 = "3-gram_string_simscoretable";
	
	//table ID == 38
	public final String DB_TABLE_NAME_INDEX38 = "3-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION38 = "3-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE38 = "3-gram_string_fingerprint_simscoretable";
	
	//table ID == 39
	public final String DB_TABLE_NAME_INDEX39 = "4-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION39 = "4-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE39 = "4-gram_string_simscoretable";
	
	//table ID == 40
	public final String DB_TABLE_NAME_INDEX40 = "4-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION40 = "4-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE40 = "4-gram_string_fingerprint_simscoretable";
	
	//table ID == 41
	public final String DB_TABLE_NAME_INDEX41 = "5-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION41 = "5-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE41 = "5-gram_string_simscoretable";
	
	//table ID == 42
	public final String DB_TABLE_NAME_INDEX42 = "5-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION42 = "5-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE42 = "5-gram_string_fingerprint_simscoretable";
	
	//table ID == 43
	public final String DB_TABLE_NAME_INDEX43 = "6-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION43 = "6-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE43 = "6-gram_string_simscoretable";
	
	//table ID == 44
	public final String DB_TABLE_NAME_INDEX44 = "6-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION44 = "6-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE44 = "6-gram_string_fingerprint_simscoretable";
	
	//table ID == 45
	public final String DB_TABLE_NAME_INDEX45 = "7-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION45 = "7-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE45 = "7-gram_string_simscoretable";
	
	//table ID == 46
	public final String DB_TABLE_NAME_INDEX46 = "7-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION46 = "7-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE46 = "7-gram_string_fingerprint_simscoretable";
	
	//table ID == 47
	public final String DB_TABLE_NAME_INDEX47 = "8-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION47 = "8-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE47 = "8-gram_string_simscoretable";
	
	//table ID == 48
	public final String DB_TABLE_NAME_INDEX48 = "8-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION48 = "8-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE48 = "8-gram_string_fingerprint_simscoretable";
	
	//table ID == 49
	public final String DB_TABLE_NAME_INDEX49 = "9-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION49 = "9-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE49 = "9-gram_string_simscoretable";
	
	//table ID == 50
	public final String DB_TABLE_NAME_INDEX50 = "9-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION50 = "9-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE50 = "9-gram_string_fingerprint_simscoretable";
	
	//table ID == 51
	public final String DB_TABLE_NAME_INDEX51 = "10-gram_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION51 = "10-gram_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE51 = "10-gram_string_simscoretable";
	
	//table ID == 52
	public final String DB_TABLE_NAME_INDEX52 = "10-gram_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION52 = "10-gram_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE52 = "10-gram_string_fingerprint_simscoretable";
	
	//table ID == 53
	public final String DB_TABLE_NAME_INDEX53 = "1-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION53 = "1-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE53 = "1-gram_noun_string_simscoretable";
	
	//table ID == 54
	public final String DB_TABLE_NAME_INDEX54 = "1-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION54 = "1-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE54 = "1-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 55
	public final String DB_TABLE_NAME_INDEX55 = "2-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION55 = "2-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE55 = "2-gram_noun_string_simscoretable";
	
	//table ID == 56
	public final String DB_TABLE_NAME_INDEX56 = "2-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION56 = "2-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE56 = "2-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 57
	public final String DB_TABLE_NAME_INDEX57 = "3-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION57 = "3-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE57 = "3-gram_noun_string_simscoretable";
	
	//table ID == 58
	public final String DB_TABLE_NAME_INDEX58 = "3-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION58 = "3-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE58 = "3-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 59
	public final String DB_TABLE_NAME_INDEX59 = "4-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION59 = "4-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE59 = "4-gram_noun_string_simscoretable";
	
	//table ID == 60
	public final String DB_TABLE_NAME_INDEX60 = "4-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION60 = "4-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE60 = "4-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 61
	public final String DB_TABLE_NAME_INDEX61 = "5-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION61 = "5-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE61 = "5-gram_noun_string_simscoretable";
	
	//table ID == 62
	public final String DB_TABLE_NAME_INDEX62 = "5-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION62 = "5-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE62 = "5-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 63
	public final String DB_TABLE_NAME_INDEX63 = "6-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION63 = "6-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE63 = "6-gram_noun_string_simscoretable";
	
	//table ID == 64
	public final String DB_TABLE_NAME_INDEX64 = "6-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION64 = "6-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE64 = "6-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 65
	public final String DB_TABLE_NAME_INDEX65 = "7-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION65 = "7-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE65 = "7-gram_noun_string_simscoretable";
	
	//table ID == 66
	public final String DB_TABLE_NAME_INDEX66 = "7-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION66 = "7-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE66 = "7-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 67
	public final String DB_TABLE_NAME_INDEX67 = "8-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION67 = "8-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE67 = "8-gram_noun_string_simscoretable";
	
	//table ID == 68
	public final String DB_TABLE_NAME_INDEX68 = "8-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION68 = "8-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE68 = "8-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 69
	public final String DB_TABLE_NAME_INDEX69 = "9-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION69 = "9-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE69 = "9-gram_noun_string_simscoretable";
	
	//table ID == 70
	public final String DB_TABLE_NAME_INDEX70 = "9-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION70 = "9-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE70 = "9-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 71
	public final String DB_TABLE_NAME_INDEX71 = "10-gram_noun_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION71 = "10-gram_noun_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE71 = "10-gram_noun_string_simscoretable";
	
	//table ID == 72
	public final String DB_TABLE_NAME_INDEX72 = "10-gram_noun_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION72 = "10-gram_noun_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE72 = "10-gram_noun_string_fingerprint_simscoretable";
	
	//table ID == 73
	public final String DB_TABLE_NAME_INDEX73 = "sentence_hashcode_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION73 = "sentence_hashcode_charlocationtable";
	public final String DB_TABLE_NAME_SCORE73 = "sentence_hashcode_simscoretable";
	
	//table ID == 74
	public final String DB_TABLE_NAME_INDEX74 = "sentence_hashcode_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION74 = "sentence_hashcode_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE74 = "sentence_hashcode_fingerprint_simscoretable";
	
	//table ID == 75
	public final String DB_TABLE_NAME_INDEX75 = "sentence_string_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION75 = "sentence_string_charlocationtable";
	public final String DB_TABLE_NAME_SCORE75 = "sentence_string_simscoretable";
	
	//table ID == 76
	public final String DB_TABLE_NAME_INDEX76 = "sentence_string_fingerprint_invertedindextable";
	public final String DB_TABLE_NAME_LOCATION76 = "sentence_string_fingerprint_charlocationtable";
	public final String DB_TABLE_NAME_SCORE76 = "sentence_string_fingerprint_simscoretable";
	
		/* experiment DB */
	/* DB Connect Info */
	
	
}
