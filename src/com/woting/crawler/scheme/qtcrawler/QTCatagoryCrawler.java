package com.woting.crawler.scheme.qtcrawler;

import java.util.ArrayList;
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

public class QTCatagoryCrawler {
	private CatagoryService cataService;

	public Map<String, Object> getCatagory2ChMap(String publisher) {
		cataService = (CatagoryService) SpringShell.getBean("catagoryService");
		List<CatagoryPo> catalist = cataService.getCatagoryList(publisher);
		Map<String, Object> chmap = new HashMap<String,Object>();
		Map<String, Object> catamap = new HashMap<String,Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Document doc ;
		try {
			for (CatagoryPo catagoryPo : catalist) {
				doc = Jsoup.connect(catagoryPo.getCatagoryURL()).ignoreContentType(true).timeout(10000).get();
				String jsonstr = doc.getElementsByTag("body").get(0).html();
				jsonstr = StringEscapeUtils.unescapeHtml4(jsonstr);
				Map<String, Object> m = (Map<String, Object>) JsonUtils.jsonToObj(jsonstr, Map.class);
				List<Map<String, Object>> datalist = (List<Map<String, Object>>) m.get("data");
				String cataid = catagoryPo.getId();
				String cataname = catagoryPo.getCatagoryName();
				catamap.put(cataid, cataname);
				for (Map<String, Object> map : datalist) {
					String chliveid = map.get("id")+"";
					chmap.put(chliveid, cataid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("chmap", chmap);
		map.put("catamap",catamap);
		System.out.println(JsonUtils.objToJson(map));
		return map;
	}
}
