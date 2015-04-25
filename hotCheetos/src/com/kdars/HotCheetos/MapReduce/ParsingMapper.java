package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.Parsing.NGram_hashcode_mapReduce;

public class ParsingMapper extends Mapper<LongWritable, Text, LongWritable, MapWritable>{

	@Override
	public void map(LongWritable docID, Text processedContent, Context context) throws IOException, InterruptedException {
		
		//Parsing ���� �� .parsdoc���� invertedindextable�� ����.
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		
		//���⼭ inverted index table�� ������.
		//�ϴ� test�� ���� default�� 3-gram hashcode fingerprint �����ϰ� ��.
		
		
		long time  = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
	
		
		DBManager.getInstance().insertSQL("insert into `plagiarismdb`.`workflow` (`type`) value ('"+docID.toString()+"')");
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `start`='"+str+"' where `type`='"+docID.toString()+"'");
		MapWritable termFreqMap = test.parseDoc(processedContent.toString(), (int) docID.get(), 77);  // ���⼭ 0�� table id�� �ϴ� default table��  'invertedindextable'�� �����ϰ� ��.
		
		
		time  = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		str = dayTime.format(new Date(time));
		DBManager.getInstance().insertSQL("update `plagiarismdb`.`workflow` set `end`='"+str+"' where `type`='"+docID.toString()+"'");
		
		
		
		context.write(docID, termFreqMap);
	}
	
}
