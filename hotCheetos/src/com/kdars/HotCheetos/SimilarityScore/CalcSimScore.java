package com.kdars.HotCheetos.SimilarityScore;

import java.util.HashMap;

public interface CalcSimScore {
	public double calcSim(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2);
}
