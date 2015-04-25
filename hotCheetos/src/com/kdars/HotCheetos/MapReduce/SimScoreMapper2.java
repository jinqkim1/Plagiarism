package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.Config.Configurations;
import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;

public class SimScoreMapper2 extends Mapper<LongWritable, MapWritable, IntWritable, MapWritable>{
	
	@Override
	public void map(LongWritable docID, MapWritable termFreqMap, Context context) throws IOException, InterruptedException {
		

		long time  = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		
		
		//DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+docID.toString()+"')");
		//DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+docID.toString()+"'");
			
		int docInfoMemoryLimit = Configurations.getInstance().getDocInfoListLimit();
		int tableID = Configurations.getInstance().getTableID();
		
//		DBManager.getInstance().checkForScore_MapReduce((int)docID.get(), tableID);  //���� mapReduce �ϴ� ���߿� �׾�ٸ� �� ������ ����Ǿ�� ������ DB���� ����� �ٽ� ����.
		
		ArrayList<Integer> corpusDocIDList = DBManager.getInstance().getCurrentDocIDsFromInvertedIndexTable(tableID);
		
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+tableID+"	"+corpusDocIDList.size()+"')");
		
		
		while(!corpusDocIDList.isEmpty()){  //���࿡ input documents�� ���� corpus document�� DB�� ��ٸ� while���� Ÿ�� ����.

			DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('1')");
			
			if(corpusDocIDList.size() <= docInfoMemoryLimit){
				//DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('2')");
				ArrayList<DocumentInfo> corpusDocInfoList = DBManager.getInstance().getMultipleDocInfoArray(corpusDocIDList, tableID);
				if (!simScore_Calculation_OneVSCorpus(docID, termFreqMap, corpusDocInfoList, tableID, tableID)){
					//DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('3')");
					System.out.println("simScore_Calculation_OneVSCorpus FAILLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
				}
				corpusDocIDList.clear();
				continue;
			}
			
			ArrayList<Integer> segmentedDocIDList = new ArrayList<Integer>(corpusDocIDList.subList(0, docInfoMemoryLimit - 1));
			ArrayList<DocumentInfo> corpusDocInfoList = DBManager.getInstance().getMultipleDocInfoArray(segmentedDocIDList, tableID);
			if (!simScore_Calculation_OneVSCorpus(docID, termFreqMap, corpusDocInfoList, tableID, tableID)){
				System.out.println("simScore_Calculation_OneVSCorpus FAILLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			}
			corpusDocIDList = new ArrayList<Integer>(corpusDocIDList.subList(docInfoMemoryLimit, corpusDocIDList.size() - 1));
		
		}
		
		
//		DBManager.getInstance().insertBulkToHashTable_MapReduce((int) docID.get(), termFreqMap, tableID);
		
		
		
		time  = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+docID.toString()+"'");
		
		return;
	}
	
	private boolean simScore_Calculation_OneVSCorpus(LongWritable docID, MapWritable termFreqMap, ArrayList<DocumentInfo> corpusDocInfoList, int scoreTableID, int invertedIndexTableID){
		StringBuilder csvContent = new StringBuilder();
		int bulkInsertLimit = Configurations.getInstance().getbulkScoreLimit();
		int bulkInsertLimitChecker = 0;

		int docid1 = (int) docID.get();
		for (DocumentInfo docInfo2 : corpusDocInfoList){
			int docid2 = docInfo2.docID;
			double simscore = calcSim(termFreqMap, docInfo2.termFreq);
			csvContent.append(String.valueOf(docid1)+","+String.valueOf(docid2)+","+String.valueOf(simscore)+"\n");
			
			bulkInsertLimitChecker++;
			if (bulkInsertLimitChecker == bulkInsertLimit){
				if(!DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID)){
					return false;
				}
				bulkInsertLimitChecker = 0;
				csvContent = new StringBuilder();
			}
		}
		
		
		return DBManager.getInstance().insertBulkToScoreTable(csvContent.toString(), scoreTableID);
	}
	
	private double calcSim(MapWritable termFreqMap, HashMap<String, Integer> doc2){
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		termFreqMap = new MapWritable(termFreqMap);
		doc2 = new HashMap<String, Integer>(doc2);
		Iterator iter1 = termFreqMap.entrySet().iterator();
		while(iter1.hasNext()){
			Map.Entry pair1 = (Map.Entry)iter1.next();
			String key = pair1.getKey().toString();
			double value1 = Double.valueOf(pair1.getValue().toString());
			
			if(doc2.containsKey(key)){
				double value2 = (double)doc2.get(key);
				multiply += (value1 * value2);
				norm2 += (value2 * value2);
				doc2.remove(key);
				
			}
			norm1 += (value1 * value1);
			iter1.remove();
		}
		
		Iterator iter2 = doc2.entrySet().iterator();
		while(iter2.hasNext()){
			Map.Entry pair2 = (Map.Entry)iter2.next();
			double value2 = Double.valueOf(pair2.getValue().toString());
			norm2 += (value2 * value2);
			iter2.remove();
		}
		
		double result =  multiply / Math.sqrt(norm1 * norm2);
		if(Double.isNaN(result)){
			result=0;
		}
		
		return result;
	}
	
}
