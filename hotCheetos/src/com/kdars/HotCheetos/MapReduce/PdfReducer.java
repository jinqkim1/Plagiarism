package com.kdars.HotCheetos.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.Parsing.Parse1_nGram_hashcode;

public class PdfReducer extends Reducer<Text, Text, Text, IntWritable>{
	
	@Override
	public void reduce(Text title, Iterable<Text> content, Context context){
		
		for (Text text : content){ //어차피 mapper에서는 한 key 당 하나의 value(pdf 내용)을 넘겨주기 때문에 iterate은 한번 밖에 안하게 될 것.
			DBManager.getInstance().insertRowToTextTable(title.toString(), text.toString());
		}
		
		Parse1_nGram_hashcode test = new Parse1_nGram_hashcode();
		
		
	}
}
