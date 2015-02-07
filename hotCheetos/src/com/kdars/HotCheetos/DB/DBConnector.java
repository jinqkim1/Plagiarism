package com.kdars.HotCheetos.DB;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.kdars.HotCheetos.Config.Configuration;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {
	private Connection sqlConnection;
	
	private String textTable = Configuration.getInstance().DB_TABLE_NAME_TEXT;
	private String docID = "DocID";
	private String docTitle = "Title";
	private String docContent = "Text";
	
	private String deletelist = Configuration.getInstance().DB_TABLE_NAME_STOPWORD;
	private String identifierForStopwordTable = "Index";
	private String stopWords = "Stopword";
	
	private String invertedIndexTable = Configuration.getInstance().DB_TABLE_NAME_INDEX;
	private String identifierForIndexTable = "Index";
	private String hashingDocID = "DocID";
	private String hashcode = "Hashcode";
	private String termFreq = "TermFrequency";
	
	private String locationTable = Configuration.getInstance().DB_TABLE_NAME_LOCATION;
	private String identifierForLocationTable = "Index";
	private String locationDocID = "DocID";
	private String hashcodeForLocation = "Hashcode";
	private String locationWithinDoc = "LocationWithinDoc";
	
	private String scoreTable = Configuration.getInstance().DB_TABLE_NAME_SCORE;
	private String identifierForScoreTable = "Index";
	private String compare = "DocID";
	private String beComparedWith = "ComparedDocID";
	private String simScore = "SimilarityScore";
	
	public DBConnector(){
		//TODO: Connector 생성되면 connect 시도해서 성공하면 ok, 실패하면 표시.

		if ((sqlConnection = connect()) == null){
			System.exit(2);
		}
		
	}
	
	public boolean insertText(String title, String text){
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			String escapedTitle = escape(title);
			String escapedText = escape(text);
			stmt.executeUpdate("insert into "+ textTable + " (" + docTitle + ", " + docContent + ") values (\"" + escapedTitle + "\", \"" + escapedText + "\");");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public HashMap<Integer,String> queryAllText() {
		HashMap<Integer,String> textMap = new HashMap<Integer,String>();
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + docID + "," + docContent + " from " + textTable + ";");

			while (resultSet.next()) {
				textMap.put(resultSet.getInt(1), resultSet.getString(2));
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
			resultSet = stmt.executeQuery("select " + docID + "," + docContent + " from " + textTable + ";");

			while (resultSet.next()) {
				DocumentInfo temp = new DocumentInfo();
				temp.docID=resultSet.getInt(1);
				temp.contents=resultSet.getString(2);
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
			resultSet = stmt.executeQuery("select " + docID + " from " + textTable + ";");

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
	
	public String queryText(int documentID){
		String text = null;
		ResultSet resultSet = null;
		try {
			java.sql.Statement stmt = sqlConnection.createStatement();
			resultSet = stmt.executeQuery("select " + docContent + " from "+ textTable + " where " + docID + " = '" + String.valueOf(documentID) +"';");
			
			while(resultSet.next()){
				text = resultSet.getString(1);
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
	
	public boolean bulkInsertScore(String csvContent){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE " + scoreTable + " DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE " + scoreTable + " FIELDS TERMINATED BY ',' (" + compare + ", " + beComparedWith + ", " + simScore + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE " + scoreTable + " ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bulkInsertHash(String csvContent) {
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE " + invertedIndexTable + " DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE " + invertedIndexTable + " FIELDS TERMINATED BY ',' (" + hashingDocID + ", " + hashcode + ", " + termFreq + ");";
			
			InputStream content = IOUtils.toInputStream(csvContent);
			
			stmt.setLocalInfileInputStream(content);
			
			stmt.execute(query);
			
			stmt.execute("ALTER TABLE " + invertedIndexTable + " ENABLE KEYS");
			stmt.execute("SET UNIQUE_CHECKS=1; ");
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
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
		
		String jdbcUrl = Configuration.getInstance().DB_JDBC_URL;
		String DBName = Configuration.getInstance().DB_NAME;
		String userID = Configuration.getInstance().DB_USER_ID;
		String userPass = Configuration.getInstance().DB_USER_PASS;
		
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
