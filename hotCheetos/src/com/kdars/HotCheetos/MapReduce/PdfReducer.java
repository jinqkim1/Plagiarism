package com.kdars.HotCheetos.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;

public class PdfReducer extends Reducer<Text, Text, Text, IntWritable>{
	
	@Override
	public void reduce(Text title, Iterable<Text> content, Context context){
		
		for (Text text : content){ //������ mapper������ �� key �� �ϳ��� value(pdf ����)�� �Ѱ��ֱ� ������ iterate�� �ѹ� �ۿ� ���ϰ� �� ��.
			DBManager.getInstance().insertRowToTextTable(title.toString(), text.toString());
		}
		
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		
		
	}
}
