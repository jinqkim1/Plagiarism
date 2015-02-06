package testing_DB;

public class DBConfig {
	
	private static  DBConfig settings = new DBConfig();
	public static DBConfig getInstance(){
		return	settings;
	}
	
	/* DB Connect Info*/
	public final String DB_JDBC_URL = "jdbc:mysql://192.168.1.4:3306/GraphDB";
	public final String DB_NAME = "plagiarismdb"; 
	public final String DB_USER_ID = "root";
	public final String DB_USER_PASS = "jinqkim69";
	public final String DB_TABLE_NAME_TEXT = "texttable";
	public final String DB_TABLE_NAME_INDEX = "invertedindextable";
	public final String DB_TABLE_NAME_SCORE = "simscoretable";
	public final String DB_TABLE_NAME_SCORE_NGRAM = "simscoretable_ngram";
	public final String DB_TABLE_NAME_SCORE_NOUN = "simscoretable_nouns";
	/* DB Connect Info*/
	
}
