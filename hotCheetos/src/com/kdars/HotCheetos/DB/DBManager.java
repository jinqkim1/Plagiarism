package com.kdars.HotCheetos.DB;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public ArrayList<Integer> getAlldocIDs(){
		return DB.queryAlldocIDs();
	}
	
	public boolean insertBulkToScoreTable(String csvContent){
		return DB.bulkInsertScore(csvContent);
	}
	
	public boolean insertBulkToHashTable(String csvContent){
		return DB.bulkInsertHash(csvContent);
	}
	
	public boolean insertBulkToLocationTable(String csvContent){
		return DB.bulkInsertLocation(csvContent);
	}
	
	public ArrayList<String> getStopwords(){
		return DB.queryStopwords();
	}
	
}
