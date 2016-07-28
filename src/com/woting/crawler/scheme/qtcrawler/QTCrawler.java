package com.woting.crawler.scheme.qtcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.scheme.tools.CrawlerInfo;
import com.woting.crawler.scheme.tools.UpdateMySql;

public class QTCrawler extends Thread {
	private List<RegionPo> regionlist;
	private List<ChannelPo> chlist = new ArrayList<ChannelPo>();
	private List<ProgrammePo> prolist = new ArrayList<ProgrammePo>();
	
	private String publisher;
	List<Map<String, Object>> list;
	
	public QTCrawler(String publisher, List<Map<String, Object>> list) {
		this.publisher = publisher;
		this.list = list;
	}
	
	private synchronized void addCrawlerInfo(Map<String, Object> m) {
		list.add(m);
	}

	public Map<String, Object> beginQTCrawler(String publisher) {
		System.out.println(publisher+"抓取行政区划开始");
		long begtime = System.currentTimeMillis();
		//抓取，先从地区开始
		QTRegionCrawler region = new QTRegionCrawler();
		QTBroadcastCrawler chcrawler = new QTBroadcastCrawler();
		Map<String, Object> regm = region.getQTRegion("http://www.qingting.fm/#/categories/1209");
		regionlist = (List<RegionPo>) regm.get("regionlist");
		regm.remove("regionlist");
		for (RegionPo regionPo : regionlist) {
			try {
				//抓取地区下电台信息
				List<ChannelPo> chs = chcrawler.getQTChannel("http://www.qingting.fm/s/categories/" + regionPo.getSrcId());
				chlist.addAll(chs);
				for (ChannelPo channelPo : chs) {
					try {
						//抓取电台的节目信息
						QTProgrammeCrawler procrawler = new QTProgrammeCrawler();
						prolist.addAll(procrawler.begionQTProgrammeCralwer(channelPo.getChLiveId()));
					} catch (Exception e) {e.printStackTrace();continue;}
				}
			} catch (Exception e) {e.printStackTrace();continue;}
		}
		//开始内容分类抓取
		QTCategoryCrawler cateCrawler = new QTCategoryCrawler();
		Map<String, Object> map = cateCrawler.getCategory2ChMap(publisher);
		Map<String, Object> chmap = (Map<String, Object>) map.get("chmap");
		Map<String, Object> catemap = (Map<String, Object>) map.get("catemap");
		for (ChannelPo channelPo : chlist) {
			channelPo.setCategoryId(chmap.get(channelPo.getChLiveId())+"");
			channelPo.setCategoryName(catemap.get(channelPo.getCategoryId())+"");
			channelPo.setRegionName(regm.get(channelPo.getRegionId())+"");
		}
		System.out.println(publisher+"中间数据库更新");
		new UpdateMySql().updateSqlInfo(regionlist, chlist, prolist, publisher);
		long endtime = System.currentTimeMillis();
		return CrawlerInfo.getCrawlerInfo(endtime-begtime, regionlist, chlist, prolist, QTCrawler.class.getSimpleName());
	}
	
	@Override
	public void run() {
		Map<String, Object> m = beginQTCrawler(publisher);
		addCrawlerInfo(m);
	}
}
