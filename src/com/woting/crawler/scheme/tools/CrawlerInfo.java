package com.woting.crawler.scheme.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

public abstract class CrawlerInfo {

	public static Map<String, Object> getCrawlerInfo(long duration, List<RegionPo> reglist, List<ChannelPo> chlist, List<ProgrammePo> prolist, String classname) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("CrawlerName", classname);
		map.put("duration", duration);
		map.put("Region.size", reglist!=null?reglist.size():0);
		map.put("Channel.size", chlist!=null?chlist.size():0);
		map.put("Programme.size", prolist!=null?prolist.size():0);
		map.put("cTime", System.currentTimeMillis());
		return map;
	}
}
