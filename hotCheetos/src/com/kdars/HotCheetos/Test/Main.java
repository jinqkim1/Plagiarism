package com.kdars.HotCheetos.Test;

import java.util.ArrayList;
import java.util.HashMap;

import com.kdars.HotCheetos.Experiment.Experiment;
import com.kdars.HotCheetos.WorkFlow.Workflow;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("\nexperiment1");
		Experiment.getInstance().experiment1();
		System.out.println("\nexperiment2");
		Experiment.getInstance().experiment2();
		System.out.println("\nexperiment3");
		Experiment.getInstance().experiment3();
		System.out.println("\nexperiment4");
		Experiment.getInstance().experiment4();
	}
}
