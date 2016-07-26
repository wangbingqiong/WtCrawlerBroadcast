package com.woting.crawler.scheme.kgcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.util.JsonUtils;

public class KGCrawler {
	
	public boolean beginKGCrawler() { 
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int num = 0;
		new KGThread(0, 300, list, num).start();
		new KGThread(300, 600, list, num).start();
		new KGThread(600, 900, list, num).start();
		new KGThread(900, 1200, list, num).start();
		new KGThread(1200, 1500, list, num).start();
		new KGThread(1500, 1800, list, num).start();
		new KGThread(2100, 2400, list, num).start();
		new KGThread(2400, 2700, list, num).start();
		new KGThread(2700, 3000, list, num).start();
		
		while(true){
			try {
				Thread.sleep(10000);
			} catch (Exception e) {}
			System.out.println(JsonUtils.objToJson(list));
		}
	}
}
