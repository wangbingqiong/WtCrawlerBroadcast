package com.woting.crawler.scheme.ygwcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;
import com.woting.crawler.scheme.tools.UpdateMySql;

public class YGWCrawler {
	
	public void beginYGWCrawler(String publisher) {
		try {
			System.out.println("开始央广网地区抓取");
			List<RegionPo> regionlist;
			List<ChannelPo> chlist = new ArrayList<ChannelPo>();
			YGWRegionAndCategoryCrawler regcrawler = new YGWRegionAndCategoryCrawler();
			Map<String, Object> regmap = regcrawler.getYGWRegionAndCategory("http://bk2.radio.cn/mms4/videoPlay/getAreaAndType.jspa");
			regionlist = (List<RegionPo>) regmap.get("regionlist");
			System.out.println("开始央广网电台抓取");
			for (RegionPo regionPo : regionlist) {
				YGWBroadcastCrawler chCrawler = new YGWBroadcastCrawler();
				List<ChannelPo> chs = chCrawler.getYGWChannel(regionPo.getSrcId());
				chlist.addAll(chs);
			}
			//进行地区和内容分类信息整合
			YGWCatagoryCrawlerCrawler catacrawler = new YGWCatagoryCrawlerCrawler();
			Map<String, Object> catamap = catacrawler.getCatagory2ChMap(publisher);
			for (ChannelPo ch : chlist) {
				ch.setRegionName(regmap.get(ch.getRegionId())+"");
				ch.setCatagoryId(catamap.get(ch.getChId())+"");
				ch.setCatagoryName(catamap.get("YGW"+ch.getCatagoryId())+"");
			}
			new UpdateMySql().updateSqlInfo(regionlist, chlist, null, publisher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
