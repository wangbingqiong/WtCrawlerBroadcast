package com.woting.crawler.scheme.etl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.jsonconf.JsonConfig;
import com.spiritdata.framework.util.JsonUtils;
import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.cm.core.broadcast.service.BroadcastService;
import com.woting.cm.core.dict.persis.po.DictMasterPo;
import com.woting.cm.core.dict.persis.po.DictRefResPo;
import com.woting.cm.core.dict.service.DictService;
import com.woting.crawler.CrawlerConstants;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.ext.SpringShell;
import com.woting.crawler.scheme.tools.CrawlerInfo;

public class CompareInfo {

	@SuppressWarnings("unchecked")
	public Map<String, Object> contrastBCLiveFlowBySrcChannelId(List<ChannelPo> chlist, Map<String, Object> chid_flowuri){
		long begintime = System.currentTimeMillis();
		List<BCLiveFlowPo> updatebclflist = new ArrayList<BCLiveFlowPo>();
		List<ChannelPo> newchlist = new ArrayList<ChannelPo>();
		for (ChannelPo ch : chlist) {
			String oldflowuri = chid_flowuri.get(ch.getPublisher()+"::"+ch.getChId())+"";
			if(!oldflowuri.equals("null") ){
				if (!ch.getFlowURI().equals(oldflowuri)) {
					BCLiveFlowPo bclf = new BCLiveFlowPo();
				    bclf.setBcSource(ch.getPublisher());
				    bclf.setFlowURI(ch.getFlowURI());
				    bclf.setBcSrcChannelId(ch.getChId());
				    bclf.setcTime(new Timestamp(System.currentTimeMillis()));
				    updatebclflist.add(bclf);
				}
			}else{
				newchlist.add(ch);
			}
		}
		if(newchlist.size()>0){
			Map<String, Object> m = DataTransform.getBcByCh(newchlist);
		    List<BroadcastPo> bclist = (List<BroadcastPo>) m.get("broadcast");
		    List<BCLiveFlowPo> bclflist = (List<BCLiveFlowPo>) m.get("bcliveflow");
		    BroadcastService bcService = (BroadcastService) SpringShell.getBean("broadcastService");
		    BcLiveFlowService bclfService = (BcLiveFlowService) SpringShell.getBean("bcLiveFlowService");
		    bcService.insertBroadcastList(bclist);
		    bclfService.insertBCLiveFlowList(bclflist);
		}
		long endtime = System.currentTimeMillis();
		return CrawlerInfo.getMakeSqlInfo(endtime-begintime, updatebclflist, newchlist, Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public List<Map<String, Object>> contrastBCProgrammeByBcId(List<ProgrammePo> prolist, Map<String, Object> bcPmap, Map<String, Object> chid_bcid){
		List<Map<String, Object>> updatelist = new ArrayList<Map<String,Object>>();
		Map<String, Object> m = new HashMap<String,Object>();
		for (ProgrammePo P : prolist) {
			String Pstr = P.getTitle()+P.getBegintime()+P.getEndtime();
			String bcid = chid_bcid.get(P.getChId())+"";
			if(bcid!=null&&bcid.equals("null")){
				if(!Pstr.equals(bcPmap.get(bcid+P.getWeekDay()+P.getSort()))){
					m.put("bcid", bcid);
					m.put("chid", P.getChId());
					updatelist.add(m);
					m.clear();
				}
			}
		}
		return updatelist;
	}
	
	public List<DictRefResPo> contrastResDictRegion(List<BroadcastPo> bclist, String mid){
		DictService dictService = (DictService) SpringShell.getBean("dictService");
		DictMasterPo dictm = dictService.getDictMById(mid).get(0);
		Map<String, Object> dictd = dictService.getDictDMapByMid(mid);
		List<DictRefResPo> reslist = DataTransform.getResDictRegionByBcAndDictM(bclist, dictm, dictd);
		return reslist;
	}
	
	public List<DictRefResPo> contrastResDictCategory(List<BCLiveFlowPo> bclflist, String mid){
		DictService dictService = (DictService) SpringShell.getBean("dictService");
		ChannelService chService = (ChannelService) SpringShell.getBean("channelService");
		DictMasterPo dictm = dictService.getDictMById("1").get(0);
		Map<String, Object> dictd = dictService.getDictDMapByMid("1");
		Map<String, Object> chid_cateid = chService.getChIdAndCateidMap();
		JsonConfig jsonConfig = null;
		try {
			jsonConfig = new JsonConfig(SystemCache.getCache(CrawlerConstants.APP_PATH).getContent()+"conf/DictRelevance.txt");
		} catch (Exception e) {e.printStackTrace();}
		List<DictRefResPo> reslist = DataTransform.getResDictCategory(bclflist, dictm, dictd, chid_cateid, jsonConfig);
		List<DictRefResPo> dictlist = new ArrayList<DictRefResPo>();
		String ids = "";
		for (DictRefResPo dictRefResPo : reslist) {
			if (ids.contains(dictRefResPo.getResId())) {
				dictlist.add(dictRefResPo);
			}else {
				ids += dictRefResPo.getResId();
			}
		}
		reslist.removeAll(dictlist);
		System.out.println(JsonUtils.objToJson(reslist));
		return reslist;
	}
}
