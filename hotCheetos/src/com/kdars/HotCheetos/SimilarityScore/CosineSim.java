package com.kdars.HotCheetos.SimilarityScore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSim extends CalcSimScore {

	@Override
	public double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2) {
		
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		Iterator iter1 = doc1.entrySet().iterator();
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
		}
		
		double result =  multiply / Math.sqrt(norm1 * norm2);
		if(Double.isNaN(result)){
			result=0;
		}
		
		return result;
	}

}
