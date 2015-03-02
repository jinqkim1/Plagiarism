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
		
		int docID = 0;
		String content = null;
		
		int contentCount = 0; //어차피 mapper에서는 한 key 당 하나의 value(pdf 내용)을 넘겨주기 때문에 iterate은 한번 밖에 안하게 되는게 정상.
		for (Text text : contents){
			
			contentCount++;
			
			content = text.toString();
			
			//여기서 text table에 저장 후 docID 가져옴.
			docID = DBManager.getInstance().insertRowAndGetDocIDArray(title.toString(), text.toString());  //결국 하나 밖에 안 담음.
		}
		
		if (contentCount != 1){
			//content가 여러개 들어왔다?? 한 제목당??  이건 비정상.
			System.out.println("Reducer에서 고장났음");
		}
		
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		
		//여기서 inverted index table에 저장함.
		//일단 test를 위해 default로 3-gram hashcode fingerprint 진행하게 됨.
		DocumentInfo docInfo = test.parseDoc(content, docID, 73);  // 여기서 73은 table id로 일단 sentence-hashcode table에 저장하게 됨.
		
		for(String key : docInfo.termFreq.keySet()){
			Text keyText = new Text();
			keyText.set(key);
			
			IntWritable valueInt = new IntWritable();
			valueInt.set(docInfo.termFreq.get(key));
			
			context.write(keyText, valueInt);
		}
		
	}
}
