package com.woting.crawler.scheme.ygwcrawler;

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

public class YGWCategoryCrawler {
	
	private CategoryService cateService;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCategory2ChMap(String publisher) {
		cateService = (CategoryService) SpringShell.getBean("categoryService");
		List<CategoryPo> catelist = cateService.getCategoryList(publisher);
		Map<String, Object> map = new HashMap<String,Object>();
		Document doc ;
		try {
			for (CategoryPo categoryPo : catelist) {
				doc = Jsoup.connect(categoryPo.getCategoryURL()).ignoreContentType(true).timeout(10000).get();
				String str = doc.select("body").get(0).html();
				str = StringEscapeUtils.unescapeHtml4(str);
				str = str.replace("(", "").replace(")", "");
				str = str.substring(4, str.length());
				List<Map<String, Object>> calist = (List<Map<String, Object>>) JsonUtils.jsonToObj(str, List.class);
				String cateid = categoryPo.getSrcId();
				String catename = categoryPo.getCategoryName();
				for (Map<String, Object> m : calist) {
					String chid = m.get("channelId")+"";
					map.put(chid, cateid);
					map.put("YGW"+cateid, catename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
