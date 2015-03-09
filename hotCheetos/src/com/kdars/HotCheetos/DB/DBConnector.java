package com.kdars.HotCheetos.DB;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.DocumentStructure.SentenceInfo;
import com.kdars.HotCheetos.PairStructure.DocPair;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {
	private Connection sqlConnection;
	
	private String textTable = Configurations.getInstance().DB_TABLE_NAME_TEXT;
	private String docID = "DocID";
	private String docTitle = "Title";
	private String docContent = "Text";
	
	private String deletelist = Configurations.getInstance().DB_TABLE_NAME_STOPWORD;
	private String identifierForStopwordTable = "Index";
	private String stopWords = "Stopword";
	
	private String invertedIndexTable = Configurations.getInstance().DB_TABLE_NAME_INDEX;
	private String identifierForIndexTable = "Index";
	private String flagForMapReduce = "MapReduceFlag";  //flag description => 0=기존에 저장되있던 document들, 1=input document들, 2= simscore 계산 진행한 document들. 
	private String hashingDocID = "DocID";
	private String sentenceID = "SentenceID";
	private String hashcode = "Hashcode";
	private String term = "Term";
	private String termFreq = "TermFrequency";
	
	private String locationTable = Configurations.getInstance().DB_TABLE_NAME_LOCATION;
	private String identifierForLocationTable = "Index";
	private String locationDocID = "DocID";
	private String termForLocation = "Term";
	private String hashcodeForLocation = "Hashcode";
	private String locationWithinDoc = "LocationWithinDoc";
	
	private String scoreTable = Configurations.getInstance().DB_TABLE_NAME_SCORE;
	private String identifierForScoreTable = "Index";
	private String scoreFlag = "CalculationDuplicateFlag";
	private String compare = "DocID";
	private String beComparedWith = "ComparedDocID";
	private String simScore = "SimilarityScore";
	
	public DBConnector(){
		//TODO: Connector �����Ǹ� connect �õ��ؼ� �����ϸ� ok, �����ϸ� ǥ��.

		if ((sqlConnection = connect()) == null){
			System.exit(2);
		}
		
	}
	
	public boolean insertText(String title, String text){
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES " + textTable + " WRITE;");
			
			String escapedTitle = escape(title);
			String escapedText = escape(text);
			stmt.executeUpdate("insert into "+ textTable + " (" + docTitle + ", " + docContent + ") values (\"" + escapedTitle + "\", \"" + escapedText + "\");");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public boolean insertTextPRISM(String title, String text){
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES texttable_prism WRITE;");
			
			String escapedTitle = escape(title);
			String escapedText = escape(text);
			
			stmt.executeUpdate("insert into texttable_prism (" + docTitle + ", " + docContent + ") values (\"" + escapedTitle + "\", \"" + escapedText + "\");");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	public HashMap<Integer,String> queryAllText() {
		HashMap<Integer,String> textMap = new HashMap<Integer,String>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + this.docID + "," + docContent + " from " + textTable + ";");

			while (resultSet.next()) {
				textMap.put(resultSet.getInt(1), unescape(resultSet.getString(2)));
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return textMap;
	}
	
	public ArrayList<DocumentInfo> queryAllTextAsDocumentInfoList() {
		ArrayList<DocumentInfo> textMap = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + this.docID + "," + docContent + " from " + textTable + ";");

			while (resultSet.next()) {
				DocumentInfo temp = new DocumentInfo();
				temp.docID=resultSet.getInt(1);
				temp.contents=unescape(resultSet.getString(2));
				textMap.add(temp);
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return textMap;
	}
	
	public ArrayList<Integer> queryAllTextAsDocIDList() {
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + this.docID + " from " + textTable + ";");

			while (resultSet.next()) {
				docIDList.add(resultSet.getInt(1));
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return docIDList;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public ArrayList<Integer> queryAllTextAsDocIDListPRISM() {
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + this.docID + " from texttable_prism;");

			while (resultSet.next()) {
				docIDList.add(resultSet.getInt(1));
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return docIDList;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	public Integer queryTextAsDocID(String title) {
		int docID = 0;
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			String escapedTitle = escape(title);
			resultSet = stmt.executeQuery("select " + this.docID + " from " + textTable + " where " + docTitle + " = \"" + escapedTitle + "\";");

			while (resultSet.next()) {
				docID = resultSet.getInt(1);
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return docID;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public Integer queryTextAsDocIDPRISM(String title) {
		int docID = 0;
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			String escapedTitle = escape(title);
			resultSet = stmt.executeQuery("select " + this.docID + " from texttable_prism where " + docTitle + " = \"" + escapedTitle + "\";");

			while (resultSet.next()) {
				docID = resultSet.getInt(1);
			}

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return docID;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public boolean deleteText_PRISM(String title) {
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES texttable_prism WRITE;");
			
			String escapedTitle = escape(title);
			stmt.execute("delete from texttable_prism where " + docTitle + " = \"" + escapedTitle + "\";");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	public String queryText(int documentID){
		String text = null;
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + docContent + " from "+ textTable + " where " + docID + " = '" + String.valueOf(documentID) +"';");
			
			while(resultSet.next()){
				text = unescape(resultSet.getString(1));
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return text;
	}
	
	public ArrayList<String> queryStopwords(){
		ArrayList<String> stopwordList = new ArrayList<String>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select * from "+ deletelist + ";");
			
			while(resultSet.next()){
				stopwordList.add(resultSet.getString(2));
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return stopwordList;
	}
	
	public boolean deleteDuplicatesInScoreTable(int docID, String scoreTableName){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + scoreTableName + "` WRITE;");
			
			stmt.execute("delete from `" + scoreTableName + "` where " + compare + " = " + String.valueOf(docID) + ";");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteDuplicatesInScoreTable_WithFlag(int docID, String scoreTableName){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + scoreTableName + "` WRITE;");
			
			stmt.execute("delete from `" + scoreTableName + "` where " + compare + " = " + String.valueOf(docID) + " AND " + scoreFlag + " = 1;");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean switchFlagFromOneToZero_Score(int docID, String scoreTableName){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + scoreTableName + "` WRITE;");
			
			stmt.execute("update `" + scoreTableName + "` set " + scoreFlag + " = 0 where " + compare + " = " + String.valueOf(docID) + " AND " + scoreFlag + " = 1;");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertScore(String csvContent, String scoreTableName){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + scoreTableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + scoreTableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + scoreTableName + "` FIELDS TERMINATED BY ',' (" + scoreFlag + ", " + compare + ", " + beComparedWith + ", " + simScore + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + scoreTableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertScoreWithTableName(String csvContent, String tableName){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + tableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + tableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + tableName + "` FIELDS TERMINATED BY ',' (" + scoreFlag + ", " + compare + ", " + beComparedWith + ", " + simScore + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + tableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<DocPair> queryHighestPairs(ArrayList<Integer> docIDList, String scoreTableName){
		int docIDSizeLimit = Configurations.getInstance().getDocIDListLimit();
		ArrayList<DocPair> pairList = new ArrayList<DocPair>();
		
		ResultSet resultSet = null;
		
		try {
			double simScoreThreshold = Configurations.getInstance().getSimScoreThreshold();
			java.sql.Statement stmt = sqlConnection.createStatement();
			while (!docIDList.isEmpty()){
				if (docIDList.size() <= docIDSizeLimit){
					StringBuilder queryMaker = new StringBuilder();
					queryMaker.append("select * from `" + scoreTableName + "` where (");
					for (int docid : docIDList){
						queryMaker.append(compare + " = " + docid + " or " + beComparedWith + " = " + docid + " or ");
					}
					queryMaker.replace(queryMaker.length()-4, queryMaker.length(), ") and " + simScore + " >= '" + String.valueOf(simScoreThreshold) + "' order by " + simScore + " desc;");
					
					resultSet = stmt.executeQuery(queryMaker.toString());
					
					while (resultSet.next()){
						DocPair pair = new DocPair();
						pair.docID1 = resultSet.getInt(2);
						pair.docID2 = resultSet.getInt(3);
						pair.similarity = resultSet.getInt(4);
						pairList.add(pair);
					}
					
					docIDList.clear();
					continue;
				}
				
				ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(docIDList.subList(0, docIDSizeLimit - 1));
				StringBuilder queryMaker = new StringBuilder();
				queryMaker.append("select * from `" + scoreTableName + "` where (");
				for (int docid : segmentedDocIDList){
					queryMaker.append(compare + " = " + docid + " or " + beComparedWith + " = " + docid + " or ");
				}
				queryMaker.replace(queryMaker.length()-4, queryMaker.length(), ") and " + simScore + " >= '" + String.valueOf(simScoreThreshold) + "' order by " + simScore + " desc;");
				
				resultSet = stmt.executeQuery(queryMaker.toString());
				
				while (resultSet.next()){
					DocPair pair = new DocPair();
					pair.docID1 = resultSet.getInt(2);
					pair.docID2 = resultSet.getInt(3);
					pair.similarity = resultSet.getInt(4);
					pairList.add(pair);
				}
				
				docIDList = new ArrayList<Integer>(docIDList.subList(docIDSizeLimit, docIDList.size() - 1));
			}
			
			Collections.sort(pairList, DocPair.DocPairComparator);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return pairList;
	}
	
	public ArrayList<ArrayList<Integer>> queryHighScoresForCluster(){
		double simScoreThreshold = Configurations.getInstance().getSimScoreThreshold();
		ArrayList<ArrayList<Integer>> docIDLists = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ArrayList<Integer> comparedDocIDList = new ArrayList<Integer>();
		
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + compare + " , " + beComparedWith + " from "+ scoreTable + " where " + simScore + " >= '" + String.valueOf(simScoreThreshold) + "' order by " + compare + " asc;");
			
			while (resultSet.next()){
				docIDList.add(resultSet.getInt(1));
				comparedDocIDList.add(resultSet.getInt(2));
			}
			
			docIDLists.add(docIDList);
			docIDLists.add(comparedDocIDList);
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return docIDLists;
	}
	
	public Integer checkHash(int docID, String invertedIndexTableName) {
		int queriedDocID = 0;
		
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + this.docID + " from `" + invertedIndexTableName + "` where " + this.docID + " = " + String.valueOf(docID) + ";");
			
			while (resultSet.next()){
				queriedDocID = resultSet.getInt(1);
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return queriedDocID;
	}
	
	public boolean deleteHash(int docID, String invertedIndexTableName) {
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.execute("delete from `" + invertedIndexTableName + "` where " + this.docID + " = " + String.valueOf(docID) + ";");
			
			stmt.execute("UNLOCK TABLES");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertHash(String csvContent, String invertedIndexTableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + invertedIndexTableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + ", " + hashcode + ", " + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertHashWithString(String csvContent, String invertedIndexTableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + invertedIndexTableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + "," + term + "," + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertSentence(String csvContent, String invertedIndexTableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + invertedIndexTableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + ", " + sentenceID + "," + hashcode + ", " + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertSentenceWithString(String csvContent, String invertedIndexTableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + invertedIndexTableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + "," + sentenceID + "," + term + "," + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + invertedIndexTableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertHashWithTableName(String csvContent, String tableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + tableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + tableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + tableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + ", " + hashcode + ", " + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + tableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertHashWithStringTableName(String csvContent, String tableName) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + tableName + "` WRITE;");
			
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE `" + tableName + "` DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE `" + tableName + "` FIELDS TERMINATED BY ',' (" + flagForMapReduce + "," + hashingDocID + "," + term + "," + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE `" + tableName + "` ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.execute("UNLOCK TABLES;");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<DocumentInfo> queryMultipleDocInfoArray(ArrayList<Integer> docIDs, String invertedIndexTableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				resultSet = stmt.executeQuery("select " + hashcode + "," + termFreq + " from `" + invertedIndexTableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
				while(resultSet.next()){
					docInfo.termFreq.put(String.valueOf(resultSet.getInt(1)), resultSet.getInt(2));
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	public ArrayList<Integer> queryCurrentDocIDsFromInvertedIndexTable(String invertedIndexTableName){
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			resultSet = stmt.executeQuery("select " + this.docID + " from `" + invertedIndexTableName + "` where " + flagForMapReduce + " = 0;");
			
			while(resultSet.next()){
				docIDList.add(resultSet.getInt(1));
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return docIDList;
	}
	
	public ArrayList<Integer> flagAndqueryCurrentDocIDsFromInvertedIndexTable(int docID, String invertedIndexTableName){
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.executeUpdate("UPDATE `" + invertedIndexTableName + "` SET " + flagForMapReduce + " = 2 where " + this.docID + " = " + String.valueOf(docID) + ";");
			
			resultSet = stmt.executeQuery("select " + this.docID + " from `" + invertedIndexTableName + "` where " + flagForMapReduce + " = 1;");
			
			while(resultSet.next()){
				docIDList.add(resultSet.getInt(1));
			}
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return docIDList;
	}
	
	public boolean switchFlagFromTwoToZero_invertedIndex(String invertedIndexTableName){
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			stmt.execute("LOCK TABLES `" + invertedIndexTableName + "` WRITE;");
			
			stmt.executeUpdate("UPDATE `" + invertedIndexTableName + "` SET " + flagForMapReduce + " = 0 where " + flagForMapReduce + " != 0;");
			
			stmt.execute("UNLOCK TABLES;");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism 위해 잠!깐! 만들어놨음!
	
	
	public ArrayList<DocumentInfo> queryMultipleDocInfoArrayWithString(ArrayList<Integer> docIDs, String invertedIndexTableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				resultSet = stmt.executeQuery("select " + term + "," + termFreq + " from `" + invertedIndexTableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
				while(resultSet.next()){
					docInfo.termFreq.put(resultSet.getString(1), resultSet.getInt(2));
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	public ArrayList<DocumentInfo> queryMultipleDocInfoArray_Sentence(ArrayList<Integer> docIDs, String invertedIndexTableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				resultSet = stmt.executeQuery("select " + sentenceID + "," + hashcode + "," + termFreq + " from `" + invertedIndexTableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
				
				while(resultSet.next()){
					int senID = resultSet.getInt(1);
					if(docInfo.sentenceInfoMap.containsKey(senID)){
						docInfo.sentenceInfoMap.get(senID).termFreq.put(resultSet.getString(2), resultSet.getInt(3));
						continue;
					}
					SentenceInfo senInfo = new SentenceInfo();
					senInfo.sentenceID = senID;
					senInfo.termFreq.put(resultSet.getString(2), resultSet.getInt(3));
					docInfo.sentenceInfoMap.put(senID, senInfo);
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	public ArrayList<DocumentInfo> queryMultipleDocInfoArrayWithString_Sentence(ArrayList<Integer> docIDs, String invertedIndexTableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				resultSet = stmt.executeQuery("select " + sentenceID + "," + term + "," + termFreq + " from `" + invertedIndexTableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
				
				while(resultSet.next()){
					int senID = resultSet.getInt(1);
					if(docInfo.sentenceInfoMap.containsKey(senID)){
						docInfo.sentenceInfoMap.get(senID).termFreq.put(resultSet.getString(2), resultSet.getInt(3));
					}
					SentenceInfo senInfo = new SentenceInfo();
					senInfo.sentenceID = senID;
					senInfo.termFreq.put(resultSet.getString(2), resultSet.getInt(3));
					docInfo.sentenceInfoMap.put(senID, senInfo);
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	public DocumentInfo queryDocInfoArray(int docid, String invertedIndexTableName) {
		DocumentInfo docInfo = new DocumentInfo();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			docInfo.docID = docid;
			resultSet = stmt.executeQuery("select " + hashcode + "," + termFreq + " from `" + invertedIndexTableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
			while(resultSet.next()){
				docInfo.termFreq.put(String.valueOf(resultSet.getInt(1)), resultSet.getInt(2));
			}
		
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return docInfo;
	}
	
	public ArrayList<DocumentInfo> queryDocInfoArrayWithTableName(ArrayList<Integer> docIDs, String tableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				String sql = "select " + hashcode + "," + termFreq + " from `" + tableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';";
				resultSet = stmt.executeQuery(sql);
				while(resultSet.next()){
					docInfo.termFreq.put(String.valueOf(resultSet.getInt(1)), resultSet.getInt(2));
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	public ArrayList<DocumentInfo> queryDocInfoArrayWithStringTableName(ArrayList<Integer> docIDs, String tableName) {
		ArrayList<DocumentInfo> docInfoArray = new ArrayList<DocumentInfo>();
		ResultSet resultSet = null;
		
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			
			for(int docid : docIDs){
				DocumentInfo docInfo = new DocumentInfo();
				docInfo.docID = docid;
				resultSet = stmt.executeQuery("select " + term + "," + termFreq + " from `" + tableName + "` where " + hashingDocID + " = '" + String.valueOf(docid) + "';");
				while(resultSet.next()){
					docInfo.termFreq.put(resultSet.getString(1), resultSet.getInt(2));
				}
				docInfoArray.add(docInfo);
				resultSet = null;
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return docInfoArray;
	}
	
	public boolean bulkInsertLocation(String csvContent) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE " + locationTable + " DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE " + locationTable + " FIELDS TERMINATED BY ',' (" + locationDocID + ", " + hashcodeForLocation + ", " + locationWithinDoc + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE " + locationTable + " ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private String escape(String text) {
		String result = StringEscapeUtils.escapeHtml4(text);
		return result;
	}
	
	private String unescape(String text) {
		String result = StringEscapeUtils.unescapeHtml4(text);
		return result;
	}
	
	private Connection connect(){
		java.sql.Connection sqlConnection = null;
		
		String jdbcUrl = Configurations.getInstance().DB_JDBC_URL;
		String DBName = Configurations.getInstance().DB_NAME;
		String userID = Configurations.getInstance().DB_USER_ID;
		String userPass = Configurations.getInstance().DB_USER_PASS;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			System.err.println("JDBC is not found.");
			return null;
		}
		
		try{
			sqlConnection = DriverManager.getConnection(jdbcUrl, userID, userPass);
			
			java.sql.Statement stmt = sqlConnection.createStatement();
			stmt.execute("use "+DBName);
			stmt.close();
			
		}catch(SQLException e){
			e.printStackTrace();
			System.err.println("DB Connection Error.");
			disconnect(sqlConnection);
			return null;
		}
		
		return (Connection) sqlConnection;
	}
	
	private boolean disconnect(java.sql.Connection sqlConnection){
//		sqlConnection;
		try {
			sqlConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("DB Disconnection Error.");
			return	false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("DB Disconnection Error.");
			return	false;
		}
		
		return true;
	}
	
}
