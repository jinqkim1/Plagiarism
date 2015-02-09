package com.kdars.HotCheetos.DB;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;

public class DBManager {
	private static DBManager thisClass = new DBManager();
	private DBConnector DB;
	
	public DBManager(){
		DB = new DBConnector();
	}
	
	public static DBManager getInstance(){
		return	thisClass;
	}
	
	private String convertIDtoName_Score(int scoreTableID){
		if (scoreTableID == 1){
			return Configuration.getInstance().DB_TABLE_NAME_SCORE;
		}else if( scoreTableID == 2){
			return Configuration.getInstance().DB_TABLE_NAME_SCORE1;
		}else if(scoreTableID == 3){
			return Configuration.getInstance().DB_TABLE_NAME_SCORE2;
		}
		
		return null;
	}
	
	private String convertIDtoName_Location(int locationTableID){
		if (locationTableID == 1){
			return Configuration.getInstance().DB_TABLE_NAME_LOCATION;
		}else if( locationTableID == 2){
			return Configuration.getInstance().DB_TABLE_NAME_LOCATION1;
		}else if(locationTableID == 3){
			return Configuration.getInstance().DB_TABLE_NAME_LOCATION2;
		}
		
		return null;
	}
	
	private String convertIDtoName_InvertedIndex(int invertedIndexTableID){
		if (invertedIndexTableID == 1){
			return Configuration.getInstance().DB_TABLE_NAME_INDEX;
		}else if( invertedIndexTableID == 2){
			return Configuration.getInstance().DB_TABLE_NAME_INDEX1;
		}else if(invertedIndexTableID == 3){
			return Configuration.getInstance().DB_TABLE_NAME_INDEX2;
		}
		
		return null;
	}
	
	public boolean insertRowToTextTable(String title, String text){
		return DB.insertText(title, text);
	}
	
	public Integer insertRowAndGetDocIDArray(String title, String processedContent){
		if (DB.insertText(title, processedContent)){
			return DB.queryTextAsDocID(title);
		}
		return null;
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
	
	public boolean insertBulkToScoreTable(String csvContent, int scoreTableID){
		String scoreTableName = convertIDtoName_Score(scoreTableID);
		return DB.bulkInsertScore(csvContent, scoreTableName);
	}
	
	public ArrayList<DocPair> getHighestPairs(ArrayList<Integer> docIDList, int scoreTableID){
		String scoreTableName = convertIDtoName_Score(scoreTableID);
//		int docListSizeLimit = Configuration.getInstance().getDocIDlistLimit();
//		ArrayList<ArrayList<DocPair>> pairLists = new ArrayList<ArrayList<DocPair>>();
//		
//		while(!docIDList.isEmpty()){  //docIDList size가 너무 크면 query가 너무 길어질 가능성이 있기 때문에 잘라서 query 실행하도록 함.
//			
//			if (docIDList.size() <= docListSizeLimit){
//				pairLists.add(DB.queryHighestPairs(docIDList, scoreTableName));
//				docIDList.clear();
//			}
//			
//			ArrayList<Integer> subList = (ArrayList<Integer>) docIDList.subList(0, docListSizeLimit - 1);
//			pairLists.add(DB.queryHighestPairs(subList, scoreTableName));
//			
//			docIDList = (ArrayList<Integer>) docIDList.subList(docListSizeLimit, docIDList.size() - 1);
//		}
		
		return DB.queryHighestPairs(docIDList, scoreTableName);
	}
	
	public boolean insertBulkToScoreTableWithTableName(String csvContent, String tableName){
		return DB.bulkInsertScoreWithTableName(csvContent, tableName);
	}
	
	public ArrayList<ArrayList<Integer>> getInitialdocIDsForCluster(){
		return DB.queryHighScoresForCluster();
	}
	
	public boolean insertBulkToHashTable(DocumentInfo docInfo, int invertedIndexTableID){
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configuration.getInstance().getbulkLimit();
		int bulkInsertLimitChecker = 0;
		for (String termHash : docInfo.termFreq.keySet()){
			csvContent.append(docIDString + "," + termHash + "," + String.valueOf(docInfo.termFreq.get(termHash)) + "\n");
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent.delete(0,csvContent.length());
			}
		}
		
		return DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName);
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

	public ArrayList<DocumentInfo> getMultipleDocInfoArray(ArrayList<Integer> docIDs, int invertedIndexTableID) {
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		return DB.queryMultipleDocInfoArray(docIDs, invertedIndexTableName);
	}

	public DocumentInfo getDocInfoArray(int docid, int invertedIndexTableID) {
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		return DB.queryDocInfoArray(docid, invertedIndexTableName);
	}
	
	public ArrayList<DocumentInfo> getDocInfoArrayWithTableName(ArrayList<Integer> docIDs, String tableName) {
		return DB.queryDocInfoArrayWithTableName(docIDs, tableName);
	}
	
	public ArrayList<DocumentInfo> getDocInfoArrayWithStringTableName(ArrayList<Integer> docIDs, String tableName) {
		return DB.queryDocInfoArrayWithStringTableName(docIDs, tableName);
	}
	
}
