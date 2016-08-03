package com.woting.crawler.scheme.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

public abstract class CrawlerInfo {

	/**
	 * 抓取返回结果信息
	 * 
	 * @param duration 持续时长
	 * @param reglist 地区
	 * @param chlist 电台
	 * @param prolist 节目
	 * @param classname 抓取平台
	 * @return
	 */
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
	
	/**
	 * 
	 * @param duration 持续时长
	 * @param updatebclflist 更新已有的电台
	 * @param chlist 新增的电台
	 * @param processName 执行方式名称
	 * @return
	 */
	public static Map<String, Object> getMakeSqlInfo(long duration, List<BCLiveFlowPo> updatebclflist, List<ChannelPo> chlist, String processName){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("ProcessName", processName);
		map.put("duration", duration);
		map.put("UpdateBCLiveFlow.size", updatebclflist.size());
		map.put("InsertBroadcast.size", chlist.size());
		map.put("cTime", System.currentTimeMillis());
		return map;
	}
}
