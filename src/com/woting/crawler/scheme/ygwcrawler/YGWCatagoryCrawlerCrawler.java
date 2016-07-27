package com.woting.crawler.scheme.ygwcrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.spiritdata.framework.util.JsonUtils;
import com.woting.crawler.core.boradcast.persis.po.CatagoryPo;
import com.woting.crawler.core.boradcast.service.CatagoryService;
import com.woting.crawler.ext.SpringShell;

public class YGWCatagoryCrawlerCrawler {
	
	private CatagoryService cataService;
	
	public Map<String, Object> getCatagory2ChMap(String publisher) {
		cataService = (CatagoryService) SpringShell.getBean("catagoryService");
		List<CatagoryPo> catalist = cataService.getCatagoryList(publisher);
		Map<String, Object> map = new HashMap<String,Object>();
		Document doc ;
		try {
			for (CatagoryPo catagoryPo : catalist) {
				doc = Jsoup.connect(catagoryPo.getCatagoryURL()).ignoreContentType(true).timeout(10000).get();
				String str = doc.select("body").get(0).html();
				str = StringEscapeUtils.unescapeHtml4(str);
				str = str.replace("(", "").replace(")", "");
				str = str.substring(4, str.length());
				List<Map<String, Object>> calist = (List<Map<String, Object>>) JsonUtils.jsonToObj(str, List.class);
				String cataid = catagoryPo.getSrcId();
				String cataname = catagoryPo.getCatagoryName();
				for (Map<String, Object> m : calist) {
					String chid = m.get("channelId")+"";
					map.put(chid, cataid);
					map.put("YGW"+cataid, cataname);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
