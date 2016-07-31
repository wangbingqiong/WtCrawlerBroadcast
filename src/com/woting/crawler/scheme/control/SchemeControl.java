package com.woting.crawler.scheme.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.cm.core.broadcast.service.BroadcastService;
import com.woting.cm.core.dict.persis.po.DictRefResPo;
import com.woting.cm.core.dict.service.DictService;
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
		long begiontime = System.currentTimeMillis();
		List<Map<String, Object>> resultlist = new ArrayList<>();
		Map<String, Object> map = new HashMap<String,Object>();
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
        
       //插入资源关系库电台行政区划数据
        dictService = (DictService) SpringShell.getBean("dictService");
		int num = 0;
		dictService.deleteResDictNotInBroadcast("2");
		bcService = (BroadcastService) SpringShell.getBean("broadcastService");
		List<BroadcastPo> bclist = bcService.getBroadcastNotInResDict("2");
		List<DictRefResPo> list = compareInfo.contrastResDictRegion(bclist,"2");
		Map<String, Object> resmap = new HashMap<String,Object>();
		
		if(list!=null & list.size()>0){
			resmap.put("list", list);
			num = dictService.insertResDict(resmap);
		}
		map.put("insertDictResRegion", num);
		resmap.clear();
		
		//插入资源关系库电台内容分类数据
		num = 0;
		dictService.deleteResDictNotInBroadcast("1");
		bclfService = (BcLiveFlowService) SpringShell.getBean("bcLiveFlowService");
		List<BCLiveFlowPo> bclflist = bclfService.getBroadcastNotInResDictIsMain("1");
		List<DictRefResPo> reslist = compareInfo.contrastResDictCategory(bclflist, "1");
		if(reslist!=null & reslist.size()>0){
			resmap.put("list", reslist);
		    num = dictService.insertResDict(resmap);
		}
		map.put("insertDictResCategory", num);
		long endtime = System.currentTimeMillis();
		map.put("AllDuration", endtime-begiontime);
		resultlist.add(map);
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
