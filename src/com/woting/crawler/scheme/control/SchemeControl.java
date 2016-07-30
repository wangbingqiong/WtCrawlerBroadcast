package com.woting.crawler.scheme.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.jsonconf.JsonConfig;
import com.spiritdata.framework.util.JsonUtils;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.cm.core.broadcast.service.BroadcastService;
import com.woting.cm.core.dict.persis.po.DictMasterPo;
import com.woting.cm.core.dict.persis.po.DictRefResPo;
import com.woting.cm.core.dict.service.DictService;
import com.woting.crawler.CrawlerConstants;
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
	private DictService dictService;
	private BroadcastService bcService;
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> progress_1(){
		JsonConfig jsonConfig;
		try {
			jsonConfig = new JsonConfig(SystemCache.getCache(CrawlerConstants.APP_PATH).getContent()+"conf/DictRelevance.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> resultlist;
//		//开始抓取，抓取方式1
//		resultlist = crawlerProgress_1();
//		//开始从中间库到资源库转换  方式1
//		bclfService = (BcLiveFlowService) SpringShell.getBean("bcLiveFlowService");
//        chService = (ChannelService) SpringShell.getBean("channelService");
//        Map<String, Object> m = bclfService.getBCLiveFlowMap();
//        Map<String, Object> m1 = (Map<String, Object>) m.get("chid_flowuri");
//        List<ChannelPo> chlist = chService.getChannelList();
        CompareInfo compareInfo = new CompareInfo();
//        resultlist.add(compareInfo.contrastBCLiveFlowBySrcChannelId(chlist, m1));
//        //资源库行政地区处理
        dictService = (DictService) SpringShell.getBean("dictService");
//        List<DictMasterPo> dictmlist = dictService.getDictMById("2");
//        DictMasterPo dictm = new DictMasterPo();
//        if(dictmlist!=null&&dictmlist.size()>0)
//        	dictm = dictmlist.get(0);
//        if (dictm!=null) {
//			
//		}
		int num = dictService.deleteResDictNotInBroadcast("2");
		bcService = (BroadcastService) SpringShell.getBean("broadcastService");
		List<BroadcastPo> bclist = bcService.getBroadcastNotInResDict("2");
//		System.out.println(JsonUtils.objToJson(bclist));
		List<DictRefResPo> list = compareInfo.contrastResDictRegion(bclist);
//		System.out.println(JsonUtils.objToJson(reslist));
		Map<String, Object> resmap = new HashMap<String,Object>();
		resmap.put("list", list);
		dictService.insertResDict(resmap);
		return null;
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
