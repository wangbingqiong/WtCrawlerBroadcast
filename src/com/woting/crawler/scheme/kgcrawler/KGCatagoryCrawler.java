package com.woting.crawler.scheme.kgcrawler;

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

public class KGCatagoryCrawler {

	private CatagoryService cataService;

	public Map<String, Object> getCatagory2ChMap(String publisher) {
		cataService = (CatagoryService) SpringShell.getBean("catagoryService");
		List<CatagoryPo> catalist = cataService.getCatagoryList(publisher);
		Map<String, Object> catamap = new HashMap<String, Object>();
		Document doc;
		try {
			for (CatagoryPo catagoryPo : catalist) {
				doc = Jsoup.connect(catagoryPo.getCatagoryURL()).ignoreContentType(true).timeout(10000).get();
				String jsonstr = doc.getElementsByTag("body").get(0).html();
				jsonstr = StringEscapeUtils.unescapeHtml4(jsonstr);
				Map<String, Object> camap = (Map<String, Object>) JsonUtils.jsonToObj(jsonstr, Map.class);
				List<Map<String, Object>> calist = (List<Map<String, Object>>) camap.get("program_info_list");
				String cataid = catagoryPo.getSrcId();
				String cataname = catagoryPo.getCatagoryName();
				for (Map<String, Object> m : calist) {
					String chid = m.get("channel_key")+"";
					catamap.put(chid, cataid);
					catamap.put("KG"+cataid, cataname);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catamap;
	}
}
