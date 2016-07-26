package com.woting.crawler.scheme.qtcrawler;

import java.util.List;

import com.woting.crawler.core.boradcast.persis.po.CatagoryPo;
import com.woting.crawler.core.boradcast.service.CatagoryService;
import com.woting.crawler.ext.SpringShell;

public class QTCatagoryCrawler {
	private CatagoryService cataService;

	public List<CatagoryPo> getCatagoryList() {
		cataService = (CatagoryService) SpringShell.getBean("catagoryService");
		List<CatagoryPo> catalist = cataService.getCatagoryList();
		return catalist;
	}
}
