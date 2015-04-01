package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.kdars.HotCheetos.DB.DBManager;
import com.kdars.HotCheetos.DocumentStructure.DocumentInfo;
import com.kdars.HotCheetos.Parsing.NGram_hashcode_mapReduce;

public class PdfReducer extends Reducer<Text, Text, Text, IntWritable>{
	
	@Override
	public void reduce(Text title, Iterable<Text> contents, Context context) throws IOException, InterruptedException {
		/*
		int docID = 0;
		String content = null;
		
		int contentCount = 0; //������ mapper������ �� key �� �ϳ��� value(pdf ����)�� �Ѱ��ֱ� ������ iterate�� �ѹ� �ۿ� ���ϰ� �Ǵ°� ����.
		for (Text text : contents){
			
			contentCount++;
			
			content = text.toString();
			
			//���⼭ text table�� ���� �� docID ������.
//			docID = DBManager.getInstance().insertRowAndGetDocIDArray(title.toString(), text.toString());  //�ᱹ �ϳ� �ۿ� �� ����.
			
			//prism test �� �ٽ� ���� �ʿ�.
			docID = DBManager.getInstance().insertRowAndGetDocIDArrayPRISM(title.toString(), text.toString());
		}
		
		if (contentCount != 1){
			//content�� ������ ���Դ�?? �� �����??  �̰� ������.
			System.out.println("Reducer���� ���峵��");
		}
		
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		*/
		//���⼭ inverted index table�� ������.
		//�ϴ� test�� ���� default�� 3-gram hashcode fingerprint �����ϰ� ��.
//		DocumentInfo docInfo = test.parseDoc(content, docID, 77);  // ���⼭ 0�� table id�� �ϴ� default table��  'invertedindextable'�� �����ϰ� ��.
		
//		for(String key : docInfo.termFreq.keySet()){
//			Text keyText = new Text();
//			keyText.set(key);
//			
//			IntWritable valueInt = new IntWritable();
//			valueInt.set(docInfo.termFreq.get(key));
//			
//			context.write(keyText, valueInt);
//		}
		
		Text keyText = new Text();
		keyText.set("1");
		IntWritable valueInt = new IntWritable();
		valueInt.set(1);
		context.write(keyText, valueInt);
		
	}
}
