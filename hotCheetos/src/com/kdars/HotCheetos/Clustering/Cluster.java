package com.kdars.HotCheetos.Clustering;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.DB.DBManager;

public class Cluster {
	
	public void getIntialCluster() {
		ArrayList<ArrayList<Integer>> clusterList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> cluster = new ArrayList<Integer>();
		
		ArrayList<ArrayList<Integer>> closeDocIDLists = DBManager.getInstance().getInitialdocIDsForCluster();

		ArrayList<Integer> docIDList = closeDocIDLists.get(0);
		ArrayList<Integer> comparedDocIDList = closeDocIDLists.get(1);
		
		int duplicateDocIDCheck = 0;
		
		while(!docIDList.isEmpty()){
			int docID = docIDList.get(0);
			int comparedDocID = comparedDocIDList.get(0);
			
			if (duplicateDocIDCheck == docID){
				cluster.add(comparedDocID);
				comparedDocIDList.remove(0);
				continue;
			}
			
			if (comparedDocIDList.contains(docID)){
				
			}
			
			
			duplicateDocIDCheck = docID;
		}
		
		
	}
	
	
	
}
