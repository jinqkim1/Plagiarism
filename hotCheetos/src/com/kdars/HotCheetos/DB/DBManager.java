package com.kdars.HotCheetos.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.DocumentStructure.SentenceInfo;
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
	
	public String convertIDtoName_Score(int scoreTableID){
		if (scoreTableID == 1) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE1;
		} else if (scoreTableID == 2) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE2;
		} else if (scoreTableID == 3) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE3;
		} else if (scoreTableID == 4) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE4;
		} else if (scoreTableID == 5) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE5;
		} else if (scoreTableID == 6) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE6;
		} else if (scoreTableID == 7) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE7;
		} else if (scoreTableID == 8) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE8;
		} else if (scoreTableID == 9) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE9;
		} else if (scoreTableID == 10) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE10;
		} else if (scoreTableID == 11) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE11;
		} else if (scoreTableID == 12) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE12;
		} else if (scoreTableID == 13) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE13;
		} else if (scoreTableID == 14) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE14;
		} else if (scoreTableID == 15) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE15;
		} else if (scoreTableID == 16) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE16;
		} else if (scoreTableID == 17) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE17;
		} else if (scoreTableID == 18) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE18;
		} else if (scoreTableID == 19) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE19;
		} else if (scoreTableID == 20) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE20;
		} else if (scoreTableID == 21) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE21;
		} else if (scoreTableID == 22) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE22;
		} else if (scoreTableID == 23) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE23;
		} else if (scoreTableID == 24) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE24;
		} else if (scoreTableID == 25) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE25;
		} else if (scoreTableID == 26) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE26;
		} else if (scoreTableID == 27) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE27;
		} else if (scoreTableID == 28) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE28;
		} else if (scoreTableID == 29) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE29;
		} else if (scoreTableID == 30) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE30;
		} else if (scoreTableID == 31) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE31;
		} else if (scoreTableID == 32) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE32;
		} else if (scoreTableID == 33) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE33;
		} else if (scoreTableID == 34) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE34;
		} else if (scoreTableID == 35) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE35;
		} else if (scoreTableID == 36) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE36;
		} else if (scoreTableID == 37) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE37;
		} else if (scoreTableID == 38) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE38;
		} else if (scoreTableID == 39) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE39;
		} else if (scoreTableID == 40) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE40;
		} else if (scoreTableID == 41) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE41;
		} else if (scoreTableID == 42) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE42;
		} else if (scoreTableID == 43) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE43;
		} else if (scoreTableID == 44) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE44;
		} else if (scoreTableID == 45) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE45;
		} else if (scoreTableID == 46) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE46;
		} else if (scoreTableID == 47) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE47;
		} else if (scoreTableID == 48) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE48;
		} else if (scoreTableID == 49) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE49;
		} else if (scoreTableID == 50) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE50;
		} else if (scoreTableID == 51) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE51;
		} else if (scoreTableID == 52) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE52;
		} else if (scoreTableID == 53) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE53;
		} else if (scoreTableID == 54) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE54;
		} else if (scoreTableID == 55) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE55;
		} else if (scoreTableID == 56) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE56;
		} else if (scoreTableID == 57) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE57;
		} else if (scoreTableID == 58) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE58;
		} else if (scoreTableID == 59) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE59;
		} else if (scoreTableID == 60) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE60;
		} else if (scoreTableID == 61) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE61;
		} else if (scoreTableID == 62) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE62;
		} else if (scoreTableID == 63) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE63;
		} else if (scoreTableID == 64) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE64;
		} else if (scoreTableID == 65) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE65;
		} else if (scoreTableID == 66) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE66;
		} else if (scoreTableID == 67) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE67;
		} else if (scoreTableID == 68) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE68;
		} else if (scoreTableID == 69) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE69;
		} else if (scoreTableID == 70) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE70;
		} else if (scoreTableID == 71) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE71;
		} else if (scoreTableID == 72) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE72;
		} else if (scoreTableID == 73) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE73;
		} else if (scoreTableID == 74) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE74;
		} else if (scoreTableID == 75) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE75;
		} else if (scoreTableID == 76) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE76;
		} else if (scoreTableID == 0) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE;
		} else if (scoreTableID == 77) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE77;
		} else if (scoreTableID == 78) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE78;
		} else if (scoreTableID == 79) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE79;
		} else if (scoreTableID == 80) {
			return Configurations.getInstance().DB_TABLE_NAME_SCORE80;
		}
		
		return null;
	}
	
	public String convertIDtoName_Location(int locationTableID){
		if (locationTableID == 1) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION1;
		} else if (locationTableID == 2) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION2;
		} else if (locationTableID == 3) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION3;
		} else if (locationTableID == 4) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION4;
		} else if (locationTableID == 5) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION5;
		} else if (locationTableID == 6) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION6;
		} else if (locationTableID == 7) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION7;
		} else if (locationTableID == 8) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION8;
		} else if (locationTableID == 9) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION9;
		} else if (locationTableID == 10) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION10;
		} else if (locationTableID == 11) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION11;
		} else if (locationTableID == 12) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION12;
		} else if (locationTableID == 13) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION13;
		} else if (locationTableID == 14) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION14;
		} else if (locationTableID == 15) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION15;
		} else if (locationTableID == 16) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION16;
		} else if (locationTableID == 17) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION17;
		} else if (locationTableID == 18) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION18;
		} else if (locationTableID == 19) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION19;
		} else if (locationTableID == 20) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION20;
		} else if (locationTableID == 21) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION21;
		} else if (locationTableID == 22) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION22;
		} else if (locationTableID == 23) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION23;
		} else if (locationTableID == 24) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION24;
		} else if (locationTableID == 25) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION25;
		} else if (locationTableID == 26) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION26;
		} else if (locationTableID == 27) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION27;
		} else if (locationTableID == 28) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION28;
		} else if (locationTableID == 29) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION29;
		} else if (locationTableID == 30) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION30;
		} else if (locationTableID == 31) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION31;
		} else if (locationTableID == 32) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION32;
		} else if (locationTableID == 33) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION33;
		} else if (locationTableID == 34) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION34;
		} else if (locationTableID == 35) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION35;
		} else if (locationTableID == 36) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION36;
		} else if (locationTableID == 37) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION37;
		} else if (locationTableID == 38) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION38;
		} else if (locationTableID == 39) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION39;
		} else if (locationTableID == 40) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION40;
		} else if (locationTableID == 41) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION41;
		} else if (locationTableID == 42) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION42;
		} else if (locationTableID == 43) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION43;
		} else if (locationTableID == 44) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION44;
		} else if (locationTableID == 45) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION45;
		} else if (locationTableID == 46) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION46;
		} else if (locationTableID == 47) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION47;
		} else if (locationTableID == 48) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION48;
		} else if (locationTableID == 49) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION49;
		} else if (locationTableID == 50) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION50;
		} else if (locationTableID == 51) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION51;
		} else if (locationTableID == 52) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION52;
		} else if (locationTableID == 53) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION53;
		} else if (locationTableID == 54) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION54;
		} else if (locationTableID == 55) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION55;
		} else if (locationTableID == 56) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION56;
		} else if (locationTableID == 57) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION57;
		} else if (locationTableID == 58) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION58;
		} else if (locationTableID == 59) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION59;
		} else if (locationTableID == 60) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION60;
		} else if (locationTableID == 61) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION61;
		} else if (locationTableID == 62) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION62;
		} else if (locationTableID == 63) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION63;
		} else if (locationTableID == 64) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION64;
		} else if (locationTableID == 65) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION65;
		} else if (locationTableID == 66) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION66;
		} else if (locationTableID == 67) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION67;
		} else if (locationTableID == 68) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION68;
		} else if (locationTableID == 69) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION69;
		} else if (locationTableID == 70) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION70;
		} else if (locationTableID == 71) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION71;
		} else if (locationTableID == 72) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION72;
		} else if (locationTableID == 73) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION73;
		} else if (locationTableID == 74) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION74;
		} else if (locationTableID == 75) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION75;
		} else if (locationTableID == 76) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION76;
		} else if (locationTableID == 0) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION;
		} else if (locationTableID == 77) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION77;
		} else if (locationTableID == 78) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION78;
		} else if (locationTableID == 79) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION79;
		} else if (locationTableID == 80) {
			return Configurations.getInstance().DB_TABLE_NAME_LOCATION80;
		}
		
		return null;
	}
	
	public String convertIDtoName_InvertedIndex(int invertedIndexTableID){
		if (invertedIndexTableID == 1) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX1;
		} else if (invertedIndexTableID == 2) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX2;
		} else if (invertedIndexTableID == 3) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX3;
		} else if (invertedIndexTableID == 4) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX4;
		} else if (invertedIndexTableID == 5) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX5;
		} else if (invertedIndexTableID == 6) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX6;
		} else if (invertedIndexTableID == 7) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX7;
		} else if (invertedIndexTableID == 8) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX8;
		} else if (invertedIndexTableID == 9) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX9;
		} else if (invertedIndexTableID == 10) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX10;
		} else if (invertedIndexTableID == 11) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX11;
		} else if (invertedIndexTableID == 12) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX12;
		} else if (invertedIndexTableID == 13) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX13;
		} else if (invertedIndexTableID == 14) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX14;
		} else if (invertedIndexTableID == 15) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX15;
		} else if (invertedIndexTableID == 16) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX16;
		} else if (invertedIndexTableID == 17) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX17;
		} else if (invertedIndexTableID == 18) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX18;
		} else if (invertedIndexTableID == 19) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX19;
		} else if (invertedIndexTableID == 20) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX20;
		} else if (invertedIndexTableID == 21) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX21;
		} else if (invertedIndexTableID == 22) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX22;
		} else if (invertedIndexTableID == 23) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX23;
		} else if (invertedIndexTableID == 24) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX24;
		} else if (invertedIndexTableID == 25) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX25;
		} else if (invertedIndexTableID == 26) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX26;
		} else if (invertedIndexTableID == 27) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX27;
		} else if (invertedIndexTableID == 28) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX28;
		} else if (invertedIndexTableID == 29) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX29;
		} else if (invertedIndexTableID == 30) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX30;
		} else if (invertedIndexTableID == 31) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX31;
		} else if (invertedIndexTableID == 32) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX32;
		} else if (invertedIndexTableID == 33) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX33;
		} else if (invertedIndexTableID == 34) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX34;
		} else if (invertedIndexTableID == 35) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX35;
		} else if (invertedIndexTableID == 36) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX36;
		} else if (invertedIndexTableID == 37) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX37;
		} else if (invertedIndexTableID == 38) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX38;
		} else if (invertedIndexTableID == 39) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX39;
		} else if (invertedIndexTableID == 40) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX40;
		} else if (invertedIndexTableID == 41) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX41;
		} else if (invertedIndexTableID == 42) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX42;
		} else if (invertedIndexTableID == 43) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX43;
		} else if (invertedIndexTableID == 44) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX44;
		} else if (invertedIndexTableID == 45) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX45;
		} else if (invertedIndexTableID == 46) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX46;
		} else if (invertedIndexTableID == 47) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX47;
		} else if (invertedIndexTableID == 48) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX48;
		} else if (invertedIndexTableID == 49) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX49;
		} else if (invertedIndexTableID == 50) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX50;
		} else if (invertedIndexTableID == 51) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX51;
		} else if (invertedIndexTableID == 52) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX52;
		} else if (invertedIndexTableID == 53) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX53;
		} else if (invertedIndexTableID == 54) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX54;
		} else if (invertedIndexTableID == 55) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX55;
		} else if (invertedIndexTableID == 56) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX56;
		} else if (invertedIndexTableID == 57) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX57;
		} else if (invertedIndexTableID == 58) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX58;
		} else if (invertedIndexTableID == 59) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX59;
		} else if (invertedIndexTableID == 60) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX60;
		} else if (invertedIndexTableID == 61) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX61;
		} else if (invertedIndexTableID == 62) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX62;
		} else if (invertedIndexTableID == 63) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX63;
		} else if (invertedIndexTableID == 64) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX64;
		} else if (invertedIndexTableID == 65) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX65;
		} else if (invertedIndexTableID == 66) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX66;
		} else if (invertedIndexTableID == 67) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX67;
		} else if (invertedIndexTableID == 68) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX68;
		} else if (invertedIndexTableID == 69) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX69;
		} else if (invertedIndexTableID == 70) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX70;
		} else if (invertedIndexTableID == 71) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX71;
		} else if (invertedIndexTableID == 72) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX72;
		} else if (invertedIndexTableID == 73) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX73;
		} else if (invertedIndexTableID == 74) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX74;
		} else if (invertedIndexTableID == 75) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX75;
		} else if (invertedIndexTableID == 76) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX76;
		} else if (invertedIndexTableID == 0) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX;
		} else if (invertedIndexTableID == 77) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX77;
		} else if (invertedIndexTableID == 78) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX78;
		} else if (invertedIndexTableID == 79) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX79;
		} else if (invertedIndexTableID == 80) {
			return Configurations.getInstance().DB_TABLE_NAME_INDEX80;
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	public Integer insertRowAndGetDocIDArrayPRISM(String title, String processedContent){
		if(DB.queryTextAsDocIDPRISM(title) == 0){  //0���� �ö�´ٴ� ���� � mapper�� ���� text db�� ������ �������� �ʾҴٴ� ��. ��� ���� ���� �ǽ�.
			if (DB.insertTextPRISM(title, processedContent)){
				return DB.queryTextAsDocIDPRISM(title);
			}
		}
		
		if(DB.deleteText_PRISM(title)){  //�̹� �� �ִ� �Ŷ�� �ش� ������ ���� �� insert �ǽ�.
			if (DB.insertTextPRISM(title, processedContent)){
				return DB.queryTextAsDocIDPRISM(title);
			}
			
		}
		
		return null;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	public ArrayList<Integer> getAllTextAsDocIDArrayPRISM(){
		return DB.queryAllTextAsDocIDListPRISM();
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	
	public boolean insertBulkToScoreTable(String csvContent, int scoreTableID){
		String scoreTableName = convertIDtoName_Score(scoreTableID);
		return DB.bulkInsertScore(csvContent, scoreTableName);
	}
	
	public boolean checkForScore_MapReduce(int docID, int scoreTableID){
		String scoreTableName = convertIDtoName_Score(scoreTableID);
		return DB.deleteDuplicatesInScoreTable(docID, scoreTableName);
	}
	
	public ArrayList<DocPair> getHighestPairs(ArrayList<Integer> docIDList, int scoreTableID){
		String scoreTableName = convertIDtoName_Score(scoreTableID);
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
		
		if(invertedIndexTableName.contains("string")){
			StringBuilder csvContent = new StringBuilder();
			String docIDString = String.valueOf(docInfo.docID);
			
			int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
			int bulkInsertLimitChecker = 0;
			
			Iterator it = docInfo.termFreq.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry)it.next();
				csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
				
				bulkInsertLimitChecker++;
				if(bulkInsertLimitChecker == bulkInsertLimit){
					if(!DB.bulkInsertHashWithString(csvContent.toString(), invertedIndexTableName)){
						System.out.println("Similarity score bulk insert failed.");
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
				}
				
				it.remove();
			}

			return DB.bulkInsertHashWithString(csvContent.toString(), invertedIndexTableName);
		}
		
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Iterator it = docInfo.termFreq.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
			
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent = new StringBuilder();
			}
			
			it.remove();
		}
		
		return DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName);
	}
	
	public boolean insertBulkToHashTable_MapReduce(int docID, MapWritable termFreqMap, int invertedIndexTableID){
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		
		if(DB.checkHash(docID, invertedIndexTableName) != 0){  //�̹� �ش� docid�� hash���� ������ ��쿡��, map reduce ���߿� ���� ������ �Ǵ�. ����� ó������ �ٽ� insert��.
			boolean deleteChecker = false;
			while(!deleteChecker){
				deleteChecker = DB.deleteHash(docID, invertedIndexTableName);
			}
		}
		
		if(invertedIndexTableName.contains("string")){
			StringBuilder csvContent = new StringBuilder();
			String docIDString = String.valueOf(docID);
			
			int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
			int bulkInsertLimitChecker = 0;
			
			Iterator it = termFreqMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry)it.next();
				csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
				
				bulkInsertLimitChecker++;
				if(bulkInsertLimitChecker == bulkInsertLimit){
					if(!DB.bulkInsertHashWithString(csvContent.toString(), invertedIndexTableName)){
						System.out.println("Similarity score bulk insert failed.");
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
				}
				
				it.remove();
			}

			return DB.bulkInsertHashWithString(csvContent.toString(), invertedIndexTableName);
		}
		
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docID);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Iterator it = termFreqMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
			
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent = new StringBuilder();
			}
			
			it.remove();
		}
		
		return DB.bulkInsertHash(csvContent.toString(), invertedIndexTableName);
	}
	
	public boolean insertBulkToSentenceTable(DocumentInfo docInfo, int invertedIndexTableID){
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		
		if(invertedIndexTableName.contains("string")){
			StringBuilder csvContent = new StringBuilder();
			String docIDString = String.valueOf(docInfo.docID);
			
			int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
			int bulkInsertLimitChecker = 0;
			
			Iterator it_sentence = docInfo.sentenceInfoMap.entrySet().iterator();
			while(it_sentence.hasNext()){
				Map.Entry pairs = (Map.Entry)it_sentence.next();
				String sentenceIDString = pairs.getKey().toString();
				
				SentenceInfo senInfo = (SentenceInfo) pairs.getValue();
				Iterator it = senInfo.termFreq.entrySet().iterator();
				
				while(it.hasNext()){
					Map.Entry pair = (Map.Entry)it.next();
					csvContent.append(docIDString + "," + sentenceIDString + "," + pair.getKey().toString() + "," + pair.getValue().toString() + "\n");
					
					bulkInsertLimitChecker++;
					if(bulkInsertLimitChecker == bulkInsertLimit){
						if(!DB.bulkInsertSentenceWithString(csvContent.toString(), invertedIndexTableName)){
							System.out.println("Similarity score bulk insert failed.");
						}
						bulkInsertLimitChecker = 0;
						csvContent = new StringBuilder();
					}
					
					it.remove();
				}
				
				it_sentence.remove();
			}
			
			return DB.bulkInsertSentenceWithString(csvContent.toString(), invertedIndexTableName);
		}
		


		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Iterator it_sentence = docInfo.sentenceInfoMap.entrySet().iterator();
		while(it_sentence.hasNext()){
			Map.Entry pairs = (Map.Entry)it_sentence.next();
			String sentenceIDString = pairs.getKey().toString();
			
			SentenceInfo senInfo = (SentenceInfo) pairs.getValue();
			Iterator it = senInfo.termFreq.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry)it.next();
				csvContent.append(docIDString + "," + sentenceIDString + "," + pair.getKey().toString() + "," + pair.getValue().toString() + "\n");
				
				bulkInsertLimitChecker++;
				if(bulkInsertLimitChecker == bulkInsertLimit){
					if(!DB.bulkInsertSentence(csvContent.toString(), invertedIndexTableName)){
						System.out.println("Similarity score bulk insert failed.");
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
				}
				
				it.remove();
			}
			
			it_sentence.remove();
		}
		
		return DB.bulkInsertSentence(csvContent.toString(), invertedIndexTableName);
	}
	
	public boolean insertBulkToSentenceTable_MapReduce(int docid, HashMap<Integer, HashMap<String, Integer>> sentenceMap, int invertedIndexTableID){
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		
		if(invertedIndexTableName.contains("string")){
			StringBuilder csvContent = new StringBuilder();
			String docIDString = String.valueOf(docid);
			
			int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
			int bulkInsertLimitChecker = 0;
			
			for (Map.Entry<Integer, HashMap<String, Integer>> entry : sentenceMap.entrySet()) {
				String senIDString = entry.getKey().toString();

				HashMap<String, Integer> termFreqMap = entry.getValue();
				for (Map.Entry<String, Integer> entry1 : termFreqMap.entrySet()){
					csvContent.append(docIDString + "," + senIDString + "," + entry1.getKey() + "," + entry1.getValue().toString() + "\n");
					
					bulkInsertLimitChecker++;
					if (bulkInsertLimitChecker == bulkInsertLimit) {
						if (!DB.bulkInsertSentenceWithString(
								csvContent.toString(), invertedIndexTableName)) {
							System.out
									.println("Similarity score bulk insert failed.");
						}
						bulkInsertLimitChecker = 0;
						csvContent = new StringBuilder();
					}
				}
			}
			
			return DB.bulkInsertSentenceWithString(csvContent.toString(), invertedIndexTableName);
		}


		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docid);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		for (Map.Entry<Integer, HashMap<String, Integer>> entry : sentenceMap.entrySet()) {
			String senIDString = entry.getKey().toString();

			HashMap<String, Integer> termFreqMap = entry.getValue();
			for (Map.Entry<String, Integer> entry1 : termFreqMap.entrySet()){
				csvContent.append(docIDString + "," + senIDString + "," + entry1.getKey() + "," + entry1.getValue().toString() + "\n");
				
				bulkInsertLimitChecker++;
				if (bulkInsertLimitChecker == bulkInsertLimit) {
					if (!DB.bulkInsertSentenceWithString(
							csvContent.toString(), invertedIndexTableName)) {
						System.out
								.println("Similarity score bulk insert failed.");
					}
					bulkInsertLimitChecker = 0;
					csvContent = new StringBuilder();
				}
			}
		}
		
		return DB.bulkInsertSentenceWithString(csvContent.toString(), invertedIndexTableName);
	}
	
	public boolean insertBulkToHashTableWithTableName(DocumentInfo docInfo, String tableName){
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Iterator it = docInfo.termFreq.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
			
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHashWithTableName(csvContent.toString(), tableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent = new StringBuilder();
			}
			
			it.remove();
		}
		
		return DB.bulkInsertHashWithTableName(csvContent.toString(), tableName);
	}
	
	public boolean insertBulkToHashTableWithStringTableName(DocumentInfo docInfo, String tableName){
		StringBuilder csvContent = new StringBuilder();
		String docIDString = String.valueOf(docInfo.docID);
		
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;
		
		Iterator it = docInfo.termFreq.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			csvContent.append(docIDString + "," + pairs.getKey().toString() + "," + pairs.getValue().toString() + "\n");
			
			bulkInsertLimitChecker++;
			if(bulkInsertLimitChecker == bulkInsertLimit){
				if(!DB.bulkInsertHashWithStringTableName(csvContent.toString(), tableName)){
					System.out.println("Similarity score bulk insert failed.");
				}
				bulkInsertLimitChecker = 0;
				csvContent = new StringBuilder();
			}
			
			it.remove();
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
		
		if(invertedIndexTableName.contains("string")){
			return DB.queryMultipleDocInfoArrayWithString(docIDs, invertedIndexTableName);
		}
		
		return DB.queryMultipleDocInfoArray(docIDs, invertedIndexTableName);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	public ArrayList<Integer> getCurrentDocIDsFromInvertedIndexTable(int invertedIndexTableID){
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		//DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('invertedIndexTableName	"+invertedIndexTableName+"')");
		
		return DB.queryCurrentDocIDsFromInvertedIndexTable(invertedIndexTableName);
	}
//	
//	public ArrayList<Integer> flagInputAndGetCurrentDocIDsFromInvertedIndexTable(int docID, int invertedIndexTableID){
//		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
//		
//		return DB.flagAndqueryCurrentDocIDsFromInvertedIndexTable(docID, invertedIndexTableName);
//	}
//	
//	public boolean flagCompleteDocuments(int invertedIndexTableID){
//		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
//		
//		return DB.switchFlagFromTwoToZero_invertedIndex(invertedIndexTableName);
//	}
	//////////////////////////////////////////////////////////////////////////////////////////////////// prism ���� ��!��! ��������!
	
	
	public ArrayList<DocumentInfo> getMultipleDocInfoArray_Sentence(ArrayList<Integer> docIDs, int invertedIndexTableID) {
		String invertedIndexTableName = convertIDtoName_InvertedIndex(invertedIndexTableID);
		
		if(invertedIndexTableName.contains("string")){
			return DB.queryMultipleDocInfoArrayWithString_Sentence(docIDs, invertedIndexTableName);
		}
		
		return DB.queryMultipleDocInfoArray_Sentence(docIDs, invertedIndexTableName);
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

	public void insertSQL(String sql) {
		DB.insertSQL(sql);
	}

	public boolean checkFile(String sql) {
		// TODO Auto-generated method stub
		return DB.checkFile(sql);
	}

	public void insertSQLMapperin(String sql) {
		DB.insertSQLMapper(sql);
	}

	public void completeStatus() {
		DB.completeStatus();
	}
	
}
