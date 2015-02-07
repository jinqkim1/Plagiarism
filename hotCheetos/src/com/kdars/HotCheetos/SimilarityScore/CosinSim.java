package com.kdars.HotCheetos.SimilarityScore;

import java.util.HashMap;
import java.util.Iterator;

public class CosinSim implements CalcSimScore {

	private static  CosinSim cosinSim = new CosinSim();
	public static CosinSim getInstance(){
		return	cosinSim;
	}
	@Override
	public double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2) {
		
		double multiply = 0.0d;
		double norm1 = 0.0d;
		double norm2 = 0.0d;
		
		Iterator<String> iter1 = doc1.keySet().iterator();
		
		while(iter1.hasNext()){
			String key = iter1.next();
			if(doc2.containsKey(key)){
				multiply += ((double)doc1.get(key)) * ((double)doc2.get(key));
				norm2 += (((double)doc2.get(key)) * ((double)doc2.get(key)));
				doc2.remove(key);
			}
			norm1 += (((double)doc1.get(key)) * ((double)doc1.get(key)));
		}
		
		Iterator<String> iter2 = doc2.keySet().iterator();
		
		while(iter2.hasNext()){
			String key = iter2.next();
			norm2 += (((double)doc2.get(key)) * ((double)doc2.get(key)));
		}
		
		double result =  multiply / Math.sqrt(norm1 * norm2);
		if(Double.isNaN(result)){
			result=0;
		}
		
		return result;
	}

}
