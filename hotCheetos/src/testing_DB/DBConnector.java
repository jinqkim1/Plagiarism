package testing_DB;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {
	private Connection sqlConnection;
	
	private String textTable = DBConfig.getInstance().DB_TABLE_NAME_TEXT;
	private String docID = "DocID";
	private String docTitle = "Title";
	private String docContent = "Text";
	
	private String invertedIndexTable = DBConfig.getInstance().DB_TABLE_NAME_INDEX;
	private String identifierForIndexTable = "Index";
	private String hashingDocID = "DocID";
	private String hashcode = "Hashcode";
	private String termFreq = "TermFrequency";
	
	private String scoreTable = DBConfig.getInstance().DB_TABLE_NAME_SCORE;
//	private String scoreTable = DBConfig.getInstance().DB_TABLE_NAME_SCORE_NGRAM;
//	private String scoreTable = DBConfig.getInstance().DB_TABLE_NAME_SCORE_NOUN;
	private String identifierForScoreTable = "Index";
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
	
	public boolean bulkInsertScore(String csvContent){
		try {
			Statement stmt = (com.mysql.jdbc.Statement)sqlConnection.createStatement();
			stmt.execute("SET UNIQUE_CHECKS=0; ");
			stmt.execute("ALTER TABLE " + scoreTable + " DISABLE KEYS");
			
			String query = "LOAD DATA LOCAL INFILE 'file.txt' " +
                    "INTO TABLE " + scoreTable + " (" + compare + ", " + beComparedWith + ", " + simScore + ");";
			
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
	
	public boolean bulkInsertHash() {
		return false;
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
//		sqlConnection;
		System.out.println("testing");
		java.sql.Connection sqlConnection = null;
		
		String jdbcUrl = DBConfig.getInstance().DB_JDBC_URL;
		String DBName = DBConfig.getInstance().DB_NAME;
		String userID = DBConfig.getInstance().DB_USER_ID;
		String userPass = DBConfig.getInstance().DB_USER_PASS;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
//			e.printStackTrace();
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