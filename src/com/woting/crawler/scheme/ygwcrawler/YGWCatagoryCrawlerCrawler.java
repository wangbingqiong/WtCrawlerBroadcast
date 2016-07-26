package com.woting.crawler.scheme.ygwcrawler;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.woting.crawler.core.boradcast.persis.po.CatagoryPo;
import com.woting.crawler.core.boradcast.service.CatagoryService;
import com.woting.crawler.ext.SpringShell;

public class YGWCatagoryCrawlerCrawler {
	
	private CatagoryService cataService;
	
	public Map<String, Object> getCatagory2ChMap(String publisher) {
		cataService = (CatagoryService) SpringShell.getBean("catagoryService");
		List<CatagoryPo> catalist = cataService.getCatagoryList(publisher);
		Document doc ;
		try {
			for (CatagoryPo catagoryPo : catalist) {
				doc = Jsoup.connect(catagoryPo.getCatagoryURL()).ignoreContentType(true).timeout(10000).get();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
