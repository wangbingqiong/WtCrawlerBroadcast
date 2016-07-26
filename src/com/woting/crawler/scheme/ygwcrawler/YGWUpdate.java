package com.woting.crawler.scheme.ygwcrawler;

import java.util.List;

import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.core.boradcast.service.RegionService;
import com.woting.crawler.ext.SpringShell;

public class YGWUpdate {
	private ChannelService chService;
	private RegionService regService;

	public void updatYGWInfo(List<RegionPo> regionlist, List<ChannelPo> chlist) {
		chService = (ChannelService) SpringShell.getBean("channelService");
		regService = (RegionService) SpringShell.getBean("regionService");
		try {
			regService.deleteRegion("央广网FM");
			regService.insertRegionList(regionlist);
			chService.deleteChannel("央广网FM");
			chService.insertChannelList(chlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
