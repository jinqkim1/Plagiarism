package com.kdars.HotCheetos.DataImport;

import com.kdars.HotCheetos.Config.Configuration;

public interface ImportContent {
	public String extractTextPattern = Configuration.getInstance().getTextPattern();
	
	public String importContent(int src);
	public String importDocument(String src);
}
