package com.woting.crawler.scheme.kgcrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.core.boradcast.service.RegionService;
import com.woting.crawler.ext.SpringShell;

public class KGRegionCrawler {
	
	private RegionService regService;
	
	public Map<String, Object> getKGRegionList(String publisher) {
		regService = (RegionService) SpringShell.getBean("regionService");
		Map<String, Object> map = new HashMap<String,Object>();
		List<RegionPo> reglist= regService.getRegionList(publisher);
		for (RegionPo regionPo : reglist) {
			String srcid = regionPo.getSrcId();
			String regionname = regionPo.getRegionName();
			map.put(srcid, regionname);
		}
		map.put("regionlist", reglist);
		return map;
	}
}
