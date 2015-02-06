package com.kdars.HotCheetos.Config;

public class Configuration {
	
	private static  Configuration settings = new Configuration();
	public static Configuration getInstance(){
		return	settings;
	}
	
	/* N-gram setting */
	private int windowSize = 3;
	public int getNgramSetting(){  //N-gram�� size�� windowSize�� setting.
		return this.windowSize;
	}
	/* N-gram setting */
	
	/* Finger-printing setting */
	private int mod = 4;
	public int getFingerprintSetting(){  //mod�� ����� hashcode �����ϴ� ������ ���� setting.
		return this.mod;
	}
	
	private int substringSize = 15;
	public int getSubstringSetting(){  //substringSize�� ������ŭ�� ĳ���ͷ� substring �����ϴ� ������ ���� setting.
		return this.substringSize;
	}
	/* Finger-printing setting */
	
	/* DB Connect Info */
	public final String DB_JDBC_URL = "jdbc:mysql://192.168.1.4:3306/GraphDB";
	public final String DB_NAME = "plagiarismdb"; 
	public final String DB_USER_ID = "root";
	public final String DB_USER_PASS = "jinqkim69";
	public final String DB_TABLE_NAME_TEXT = "texttable";
	public final String DB_TABLE_NAME_INDEX = "invertedindextable";
	public final String DB_TABLE_NAME_SCORE = "simscoretable";
	public final String DB_TABLE_NAME_SCORE_NGRAM = "simscoretable_ngram";
	public final String DB_TABLE_NAME_SCORE_NOUN = "simscoretable_nouns";
	/* DB Connect Info */
	
}
