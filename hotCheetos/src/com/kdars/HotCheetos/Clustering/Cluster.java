package com.kdars.HotCheetos.Clustering;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.DB.DBManager;

public class Cluster {
	
	public void getIntialCluster() {
		ArrayList<HashMap<Integer, Integer>> clusterList = new ArrayList<HashMap<Integer, Integer>>();

		ArrayList<ArrayList<Integer>> closeDocIDMap = DBManager.getInstance().getInitialdocIDsForCluster();

		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		ArrayList<Integer> comparedDocIDList = new ArrayList<Integer>();

	}
	
	
	
}
