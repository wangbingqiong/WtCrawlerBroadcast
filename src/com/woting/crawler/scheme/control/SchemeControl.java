package com.woting.crawler.scheme.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.util.JsonUtils;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.ext.SpringShell;
import com.woting.crawler.scheme.etl.CompareInfo;
import com.woting.crawler.scheme.kgcrawler.KGCrawler;
import com.woting.crawler.scheme.qtcrawler.QTCrawler;
import com.woting.crawler.scheme.ygwcrawler.YGWCrawler;

public class SchemeControl {
	
	private BcLiveFlowService bclfService ;
	private ChannelService chService;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> progress_1(){
		List<Map<String, Object>> resultlist;
		//开始抓取，抓取方式1
		resultlist = crawlerProgress_1();
		//开始从中间库到资源库转换  方式1
		bclfService = (BcLiveFlowService) SpringShell.getBean("bcLiveFlowService");
        chService = (ChannelService) SpringShell.getBean("channelService");
        Map<String, Object> m = bclfService.getBCLiveFlowMap();
        Map<String, Object> m1 = (Map<String, Object>) m.get("chid_flowuri");
        List<ChannelPo> chlist = chService.getChannelList();
        CompareInfo compareInfo = new CompareInfo();
        resultlist.add(compareInfo.contrastBCLiveFlowBySrcChannelId(chlist, m1));
		return resultlist;
	}
	
	public List<Map<String, Object>> crawlerProgress_1(){
		 List<Map<String, Object>> crawlerlist = new ArrayList<Map<String,Object>>();
	        new KGCrawler("酷狗FM", crawlerlist).start();
	        new QTCrawler("蜻蜓FM", crawlerlist).start();
	        new YGWCrawler("央广网FM", crawlerlist).start();
	        while(true){
	        	try {
					Thread.sleep(10000);
				} catch (Exception e) {}
	        	System.out.println("##"+crawlerlist.size());
	        	if(crawlerlist!=null && crawlerlist.size()==3){
	        		break;
	        	}
	        }
		return crawlerlist;
	}
}
