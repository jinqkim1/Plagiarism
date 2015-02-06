package testing_DB;

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
	
	public boolean insertBulkToScoreTable(String csvContent){
		return DB.bulkInsertScore(csvContent);
	}

	public boolean insertBulkToHashTable() {
		return DB.bulkInsertHash();
	}
	
}
