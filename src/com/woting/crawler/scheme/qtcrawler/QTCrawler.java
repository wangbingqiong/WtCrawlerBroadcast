package com.woting.crawler.scheme.qtcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.util.JsonUtils;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.scheme.tools.CrawlerInfo;
import com.woting.crawler.scheme.tools.UpdateMySql;

import thredds.catalog.CrawlableCatalog;

public class QTCrawler {
	private List<RegionPo> regionlist;
	private List<ChannelPo> chlist = new ArrayList<ChannelPo>();
	private List<ProgrammePo> fslist = new ArrayList<ProgrammePo>();

	public void beginQTCrawler(String publisher) {
		System.out.println("蜻蜓抓取开始");
		//抓取，先从地区开始
		QTRegionCrawler region = new QTRegionCrawler();
		QTBroadcastCrawler chcrawler = new QTBroadcastCrawler();
		QTProgrammeCrawler fscrawler = new QTProgrammeCrawler();
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
						fslist.addAll(fscrawler.begionQTProgrammeCralwer(channelPo.getChLiveId()));
					} catch (Exception e) {e.printStackTrace();continue;}
				}
			} catch (Exception e) {e.printStackTrace();continue;}
		}
		//开始内容分类抓取
		QTCatagoryCrawler cataCrawler = new QTCatagoryCrawler();
		Map<String, Object> map = cataCrawler.getCatagory2ChMap(publisher);
		Map<String, Object> chmap = (Map<String, Object>) map.get("chmap");
		Map<String, Object> catamap = (Map<String, Object>) map.get("catamap");
		for (ChannelPo channelPo : chlist) {
			channelPo.setCatagoryId(chmap.get(channelPo.getChLiveId())+"");
			channelPo.setCatagoryName(catamap.get(channelPo.getCatagoryId())+"");
			channelPo.setRegionName(regm.get(channelPo.getRegionId())+"");
		}
		System.out.println("蜻蜓中间数据库更新");
		new UpdateMySql().updateSqlInfo(regionlist, chlist, fslist, publisher);
	}
}
