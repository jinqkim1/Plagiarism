package com.kdars.HotCheetos.PairStructure;

import java.util.Comparator;

public class DocPair {
	public int docID1;
	public int docID2;
	public double similarity;
	
//	public int compare(Object arg0, Object arg1) {
//		return (((DocPair) arg0).similarity > ((DocPair) arg1).similarity)?1:-1;
//	}
	
	public static Comparator<DocPair> DocPairComparator = new Comparator<DocPair>() {

		public int compare(DocPair arg0, DocPair arg1) {

			return (((DocPair) arg0).similarity > ((DocPair) arg1).similarity) ? 1 : -1;
		}

	};
}
