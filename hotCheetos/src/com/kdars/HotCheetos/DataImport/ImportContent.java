package com.kdars.HotCheetos.DataImport;

import com.kdars.HotCheetos.Config.Configurations;

public interface ImportContent {
	public String extractTextPattern = Configurations.getInstance().getTextPattern();
	
	public String importContent(int src);
	public String importDocument(String src);
}
