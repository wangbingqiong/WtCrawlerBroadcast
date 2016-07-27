package com.woting.crawler.scheme.kgcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.scheme.tools.CrawlerInfo;
import com.woting.crawler.scheme.tools.UpdateMySql;

public class KGCrawler extends Thread {
	
	private String publisher;
	List<Map<String, Object>> list;
	
	public KGCrawler(String publisher,List<Map<String, Object>> list) {
		this.publisher = publisher;
		this.list = list;
	}
	
	private synchronized void addListInfo(Map<String, Object> m){
		list.add(m);
	}
	
	public Map<String , Object> beginKGCrawler(String publisher) { 
		System.out.println(publisher+"开始抓取");
		long begintime = System.currentTimeMillis();
		KGRegionCrawler regionCrawler = new KGRegionCrawler();
		KGCatagoryCrawler cataCrawler = new KGCatagoryCrawler();
		KGBroadcastCrawler bcCrawler = new KGBroadcastCrawler();
		List<ChannelPo> chlist = new ArrayList<ChannelPo>();
		System.out.println(publisher+"抓取行政区划信息");
		Map<String, Object> regmap = regionCrawler.getKGRegionList(publisher);
		List<RegionPo> regionlist = (List<RegionPo>) regmap.get("regionlist");
		System.out.println(publisher+"抓取电台信息");
		for (RegionPo regionPo : regionlist) {
			chlist.addAll(bcCrawler.getKGBroadcastList(regionPo.getRegionURL()));
		}
		System.out.println(publisher+"加载内容分类信息");
		Map<String, Object> catamap = cataCrawler.getCatagory2ChMap(publisher);
		for (ChannelPo ch : chlist) {
			ch.setRegionName(regmap.get(ch.getRegionId())+"");
			ch.setCatagoryId(catamap.get(ch.getChId())+"");
			ch.setCatagoryName(catamap.get(ch.getCatagoryId())+"");
		}
		System.out.println(publisher+"更新中间数据库");
		new UpdateMySql().updateSqlInfo(null, chlist, null, publisher);
		long endtime = System.currentTimeMillis();
		System.out.println(publisher+"抓取完成");
		return CrawlerInfo.getCrawlerInfo(endtime-begintime, null, chlist, null, KGCrawler.class.getName());
	}
	
	@Override
	public void run() {
		Map<String, Object> map = beginKGCrawler(publisher);
		addListInfo(map);
	}
}
