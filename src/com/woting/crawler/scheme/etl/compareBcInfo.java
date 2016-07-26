package com.woting.crawler.scheme.etl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.persis.po.BCProgrammePo;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.cm.core.broadcast.service.BcProgrammeService;
import com.woting.cm.core.broadcast.service.BroadcastService;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.core.boradcast.service.ProgrammeService;
import com.woting.crawler.ext.SpringShell;
import com.woting.crawler.scheme.qtcrawler.QTCrawler;

public class compareBcInfo {
	
	private BroadcastService bcService;
	private BcLiveFlowService bclfService;
	private BcProgrammeService bcProService;
	private ChannelService chService;
	private ProgrammeService proService;
	
	public void compareCrawlerBcAndSqlBc(){
		bcService = (BroadcastService) SpringShell.getBean("broadcastService");
		bclfService = (BcLiveFlowService) SpringShell.getBean("bcLiveFlowService");
		bcProService = (BcProgrammeService) SpringShell.getBean("bcProgrammeService");
		chService = (ChannelService) SpringShell.getBean("channelService");
		proService = (ProgrammeService) SpringShell.getBean("programmeService");
		List<BroadcastPo> bclist = bcService.getBroadcastList(); //获得服务器数据库电台列表
		List<ChannelPo> chlist = chService.getChannelList("蜻蜓FM"); //获得中间数据库电台列表
		List<ProgrammePo> prolist = proService.getProgrammeList("蜻蜓FM"); //获得中间数据库节目列表
		
		List<BroadcastPo> samelist = new ArrayList<BroadcastPo>(); //比较后相同的电台列表
		List<BroadcastPo> nosamelist = new ArrayList<BroadcastPo>(); //比较后不相同的电台列表
		List<ChannelPo> samechlist = new ArrayList<ChannelPo>(); 
		List<BCLiveFlowPo> upbclflist = new ArrayList<BCLiveFlowPo>();
		for (ChannelPo ch : chlist) {
			String chtitle = ch.getChTitle();
			for (BroadcastPo bc : bclist) {
				String bctitle = bc.getBcTitle();
				if (chtitle.equals(bctitle)) {
					samechlist.add(ch);
					samelist.add(DataTransform.updateBcByCh(bc, ch));
					upbclflist.add(DataTransform.getBclfByBcAndCh(bc, ch));
				}
			}
		}
		chlist.removeAll(samechlist);
		System.out.println("数据整理完毕，开始更新数据库");
		//更新资源库数据
		bcService.updateBroadcastList(samelist);
		bclfService.updateBCLiveFlowList(upbclflist);
		
		//插入资源库新资源
		Map<String, Object> map = DataTransform.getBcByCh(chlist);
		nosamelist = (List<BroadcastPo>) map.get("broadcast");
		upbclflist = (List<BCLiveFlowPo>) map.get("bcliveflow");
		System.out.println(nosamelist.size());
		bcService.insertBroadcastList(nosamelist);
		bclfService.insertBCLiveFlowList(upbclflist);
		
		Map<String, Object> bclfmap = bclfService.getBCLFMap("蜻蜓FM"); //得到map key=bcSrcChannel : value=bcId
		List<BCProgrammePo> bcplist = DataTransform.getBcpByBclfAndFes(bclfmap, prolist);
		bcProService.insertBCProgrammeList(bcplist);
	}
}
