package com.kdars.HotCheetos.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import com.kdars.HotCheetos.Parsing.NGram_hashcode_mapReduce;

public class ParsingMapper extends Mapper<LongWritable, Text, LongWritable, MapWritable>{

	@Override
	public void map(LongWritable docID, Text processedContent, Context context) throws IOException, InterruptedException {
		
		//Parsing ���� �� .parsdoc���� invertedindextable�� ����.
		NGram_hashcode_mapReduce test = new NGram_hashcode_mapReduce();
		
		//���⼭ inverted index table�� ������.
		//�ϴ� test�� ���� default�� 3-gram hashcode fingerprint �����ϰ� ��.
		MapWritable termFreqMap = test.parseDoc(processedContent.toString(), (int) docID.get(), 77);  // ���⼭ 0�� table id�� �ϴ� default table��  'invertedindextable'�� �����ϰ� ��.
		
		context.write(docID, termFreqMap);
	}
	
}
