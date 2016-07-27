package com.woting.crawler.scheme.ygwcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.scheme.tools.CrawlerInfo;
import com.woting.crawler.scheme.tools.UpdateMySql;

public class YGWCrawler extends Thread {

	private String publisher;
	List<Map<String, Object>> list;

	public YGWCrawler(String publisher, List<Map<String, Object>> list) {
		this.publisher = publisher;
		this.list = list;
	}
	
	private synchronized void addCrawlerInfo(Map<String, Object> m) {
		list.add(m);
	}

	public Map<String, Object> beginYGWCrawler(String publisher) {
		long begintime = System.currentTimeMillis();
		System.out.println(publisher+"行政地区抓取");
		List<RegionPo> regionlist;
		List<ChannelPo> chlist = new ArrayList<ChannelPo>();
		YGWRegionAndCategoryCrawler regcrawler = new YGWRegionAndCategoryCrawler();
		Map<String, Object> regmap = regcrawler.getYGWRegionAndCategory("http://bk2.radio.cn/mms4/videoPlay/getAreaAndType.jspa");
		regionlist = (List<RegionPo>) regmap.get("regionlist");
		System.out.println(publisher+"电台抓取");
		for (RegionPo regionPo : regionlist) {
			YGWBroadcastCrawler chCrawler = new YGWBroadcastCrawler();
			List<ChannelPo> chs = chCrawler.getYGWChannel(regionPo.getSrcId());
			chlist.addAll(chs);
		}
		System.out.println(publisher+"电台内容分类整合");
		// 进行地区和内容分类信息整合
		YGWCatagoryCrawlerCrawler catacrawler = new YGWCatagoryCrawlerCrawler();
		Map<String, Object> catamap = catacrawler.getCatagory2ChMap(publisher);
		for (ChannelPo ch : chlist) {
			ch.setRegionName(regmap.get(ch.getRegionId()) + "");
			ch.setCatagoryId(catamap.get(ch.getChId()) + "");
			ch.setCatagoryName(catamap.get("YGW" + ch.getCatagoryId()) + "");
		}
		System.err.println(publisher+"更新中间库");
		new UpdateMySql().updateSqlInfo(regionlist, chlist, null, publisher);
		long endtime = System.currentTimeMillis();
		System.out.println(publisher+"抓取完成");
		return CrawlerInfo.getCrawlerInfo(endtime - begintime, regionlist, chlist, null,YGWCrawler.class.getSimpleName());
	}
	
	@Override
	public void run() {
		Map<String, Object> m = beginYGWCrawler(publisher);
		addCrawlerInfo(m);
	}
}
