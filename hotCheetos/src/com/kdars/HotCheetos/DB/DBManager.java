package com.kdars.HotCheetos.DB;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class DBManager {
	private static DBManager thisClass = new DBManager();
	private DBConnector DB;
	
	public DBManager(){
		DB = new DBConnector();
	}
	
	public static DBManager getInstance(){
		return	thisClass;
	}
	
	public boolean insertRowToTextTable(String title, String text){
		return DB.insertText(title, text);
	}
	
	public String getText(int documentID){
		return DB.queryText(documentID);
	}
	
	public HashMap<Integer,String> getAllText(){
		return DB.queryAllText();
	}
	
	public ArrayList<DocumentInfo> getAllTextAsDocumentInforList() {
		return DB.queryAllTextAsDocumentInfoList();
	}
	
	public ArrayList<Integer> getAllTextAsDocIDArray(){
		return DB.queryAllTextAsDocIDList();
	}
	
	public boolean insertBulkToScoreTable(String csvContent){
		return DB.bulkInsertScore(csvContent);
	}
	
	public boolean insertBulkToScoreTableWithTableName(String csvContent, String tableName){
		return DB.bulkInsertScoreWithTableName(csvContent, tableName);
	}
	
	public ArrayList<ArrayList<Integer>> getInitialdocIDsForCluster(){
		return DB.queryHighScoresForCluster();
	}
	
	public boolean insertBulkToHashTable(DocumentInfo docInfo){
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		int bulkInsertLimitChecker = 0;
		for (String termHash : docInfo.termFreq.keySet()){
			csvContent.append(docIDString + "," + termHash + "," + String.valueOf(docInfo.termFreq.get(termHash)) + "\n");
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHash(csvContent.toString())){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent.delete(0,csvContent.length());
			}
		}
		
		return DB.bulkInsertHash(csvContent.toString());
	}
	
	public boolean insertBulkToHashTableWithTableName(DocumentInfo docInfo, String tableName){
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		int bulkInsertLimitChecker = 0;
		for (String termHash : docInfo.termFreq.keySet()){
			csvContent.append(docIDString + "," + termHash + "," + String.valueOf(docInfo.termFreq.get(termHash)) + "\n");
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHashWithTableName(csvContent.toString(), tableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent.delete(0,csvContent.length());
			}
		}
		
		return DB.bulkInsertHashWithTableName(csvContent.toString(), tableName);
	}
	
	public boolean insertBulkToHashTableWithStringTableName(DocumentInfo docInfo, String tableName){
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		int bulkInsertLimitChecker = 0;
		for (String termHash : docInfo.termFreq.keySet()){
			csvContent.append(docIDString + "," + termHash + "," + String.valueOf(docInfo.termFreq.get(termHash)) + "\n");
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHashWithStringTableName(csvContent.toString(), tableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent.delete(0,csvContent.length());
			}
		}
		
		return DB.bulkInsertHashWithStringTableName(csvContent.toString(), tableName);
	}
	
	public boolean insertBulkToLocationTable(String csvContent){
		return DB.bulkInsertLocation(csvContent);
	}
	
	public ArrayList<String> getStopwords(){
		return DB.queryStopwords();
	}

	public ArrayList<DocumentInfo> getDocInfoArray(ArrayList<Integer> docIDs) {
		return DB.queryDocInfoArray(docIDs);
	}
	
	public ArrayList<DocumentInfo> getDocInfoArrayWithTableName(ArrayList<Integer> docIDs, String tableName) {
		return DB.queryDocInfoArrayWithTableName(docIDs, tableName);
	}
	
	public ArrayList<DocumentInfo> getDocInfoArrayWithStringTableName(ArrayList<Integer> docIDs, String tableName) {
		return DB.queryDocInfoArrayWithStringTableName(docIDs, tableName);
	}
	
}
