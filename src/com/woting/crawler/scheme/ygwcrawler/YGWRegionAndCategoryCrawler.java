package com.woting.crawler.scheme.ygwcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

public class YGWRegionAndCategoryCrawler {

	public List<RegionPo> getYGWRegionAndCategory(String url){
		Document doc = null;
		Map<String, Object> map = new HashMap<String,Object>();
		List<RegionPo> regionlist = new ArrayList<RegionPo>();
		try {
			doc = Jsoup.connect(url).get();
			String str = doc.select("body").get(0).html();
			str = str.replace("&quot;", "\"");
			Map<String, Object> m = (Map<String, Object>) JsonUtils.jsonToObj(str, Map.class);
			List<Map<String, Object>> arealist = (List<Map<String, Object>>) m.get("area");
			for (Map<String, Object> m2 : arealist) {
				RegionPo reg = new RegionPo();
				String srcid = m2.get("key")+"";
				String regionname = m2.get("value")+"";
				String regionurl = "http://bk2.radio.cn/mms4/videoPlay/pcGetChannels.jspa?area="+srcid+"&type=0";
				reg.setId(SequenceUUID.getUUIDSubSegment(4));
				reg.setSrcId(srcid);
				reg.setRegionName(regionname);
				reg.setRegionURL(regionurl);
				reg.setPublisher("央广网FM");
				reg.setcTime(new Timestamp(System.currentTimeMillis()));
				regionlist.add(reg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regionlist;
	}
	
}
