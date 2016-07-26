package com.woting.crawler.scheme.qtcrawler;

import java.util.List;

import com.spiritdata.framework.util.JsonUtils;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.core.boradcast.service.ProgrammeService;
import com.woting.crawler.core.boradcast.service.RegionService;
import com.woting.crawler.ext.SpringShell;
import com.woting.crawler.utils.WriteTXT;

public abstract class QTUpdate {

	private ChannelService chService;
	private ProgrammeService proService;
	private RegionService  regService;
	
	public void updatQTInfo(List<RegionPo> regionlist, List<ChannelPo> chlist, List<ProgrammePo> fslist) {
		chService = (ChannelService) SpringShell.getBean("channelService");
		proService = (ProgrammeService) SpringShell.getBean("programmeService");
		regService = (RegionService) SpringShell.getBean("regionService");
		String fsstr = JsonUtils.objToJson(fslist);
		String chstr = JsonUtils.objToJson(chlist);
		WriteTXT.writeTXTByJsonstr(fsstr, "E:\\蜻蜓电台抓取\\蜻蜓节目抓取信息_"+System.currentTimeMillis()+".txt");
		WriteTXT.writeTXTByJsonstr(chstr, "E:\\蜻蜓电台抓取\\蜻蜓电台抓取信息_"+System.currentTimeMillis()+".txt");
		try {
			regService.deleteRegion("蜻蜓FM");
			regService.insertRegionList(regionlist);
			chService.deleteChannel("蜻蜓FM");
			chService.insertChannelList(chlist);
			proService.deleteProgramme("蜻蜓FM");
			proService.insertProgrammeList(fslist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
