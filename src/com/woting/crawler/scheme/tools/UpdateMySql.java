package com.woting.crawler.scheme.tools;

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

public class UpdateMySql {

	private ChannelService chService;
	private ProgrammeService proService;
	private RegionService  regService;
	
	public void updateSqlInfo(List<RegionPo> regionlist, List<ChannelPo> chlist, List<ProgrammePo> prolist, String publisher) {
		chService = (ChannelService) SpringShell.getBean("channelService");
		proService = (ProgrammeService) SpringShell.getBean("programmeService");
		regService = (RegionService) SpringShell.getBean("regionService");
		
		String chstr = JsonUtils.objToJson(chlist);
		WriteTXT.writeTXTByJsonstr(chstr, "E:\\电台抓取\\"+publisher+"电台抓取信息_"+System.currentTimeMillis()+".txt");
		if(prolist!=null&&prolist.size()>0) {
			String fsstr = JsonUtils.objToJson(prolist);
			WriteTXT.writeTXTByJsonstr(fsstr, "E:\\电台抓取\\"+publisher+"节目抓取信息_"+System.currentTimeMillis()+".txt");
		}
		try {
			if(!publisher.equals("酷狗FM") && regionlist!=null && regionlist.size()>0) {
				regService.deleteRegion(publisher);
			    regService.insertRegionList(regionlist);
			}
			
			if(chlist!=null && chlist.size()>0){
				chService.deleteChannel(publisher);
				chService.insertChannelList(chlist);
			}
			if(prolist!=null && prolist.size()>0){
				proService.deleteProgramme(publisher);
				proService.insertProgrammeList(prolist);
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}
