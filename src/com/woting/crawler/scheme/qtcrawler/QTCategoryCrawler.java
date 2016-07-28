package com.woting.crawler.scheme.qtcrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.spiritdata.framework.util.JsonUtils;
import com.woting.crawler.core.boradcast.persis.po.CategoryPo;
import com.woting.crawler.core.boradcast.service.CategoryService;
import com.woting.crawler.ext.SpringShell;

public class QTCategoryCrawler {
	private CategoryService cateService;

	public Map<String, Object> getCategory2ChMap(String publisher) {
		cateService = (CategoryService) SpringShell.getBean("categoryService");
		List<CategoryPo> catalist = cateService.getCategoryList(publisher);
		Map<String, Object> chmap = new HashMap<String,Object>();
		Map<String, Object> catemap = new HashMap<String,Object>();
		Document doc ;
		try {
			for (CategoryPo categoryPo : catalist) {
				doc = Jsoup.connect(categoryPo.getCategoryURL()).ignoreContentType(true).timeout(10000).get();
				String jsonstr = doc.getElementsByTag("body").get(0).html();
				jsonstr = StringEscapeUtils.unescapeHtml4(jsonstr);
				Map<String, Object> m = (Map<String, Object>) JsonUtils.jsonToObj(jsonstr, Map.class);
				List<Map<String, Object>> datalist = (List<Map<String, Object>>) m.get("data");
				String cateid = categoryPo.getId();
				String catename = categoryPo.getCategoryName();
				catemap.put(cateid, catename);
				for (Map<String, Object> map : datalist) {
					String chliveid = map.get("id")+"";
					chmap.put(chliveid, cateid);
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		Map<String, Object> map = new HashMap<>();
		map.put("chmap", chmap);
		map.put("catamap",catemap);
		return map;
	}
}
