package com.woting.crawler.scheme.kgcrawler;

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

public class KGCategoryCrawler {

	private CategoryService cateService;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCategory2ChMap(String publisher) {
		cateService = (CategoryService) SpringShell.getBean("categoryService");
		List<CategoryPo> catelist = cateService.getCategoryList(publisher);
		Map<String, Object> catemap = new HashMap<String, Object>();
		Document doc;
		try {
			for (CategoryPo categoryPo : catelist) {
				doc = Jsoup.connect(categoryPo.getCategoryURL()).ignoreContentType(true).timeout(10000).get();
				String jsonstr = doc.getElementsByTag("body").get(0).html();
				jsonstr = StringEscapeUtils.unescapeHtml4(jsonstr);
				Map<String, Object> camap = (Map<String, Object>) JsonUtils.jsonToObj(jsonstr, Map.class);
				List<Map<String, Object>> calist = (List<Map<String, Object>>) camap.get("program_info_list");
				String cateid = categoryPo.getSrcId();
				String catename = categoryPo.getCategoryName();
				for (Map<String, Object> m : calist) {
					String chid = m.get("channel_key")+"";
					catemap.put(chid, cateid);
					catemap.put("KG"+cateid, catename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catemap;
	}
}
