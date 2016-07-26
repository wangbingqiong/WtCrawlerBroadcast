package com.woting.crawler.scheme.ygwcrawler;

import java.util.ArrayList;
import java.util.List;

import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

public class YGWCrawler {

	private List<RegionPo> regionlist;
	private List<ChannelPo> chlist = new ArrayList<ChannelPo>();
	
	public void beginYGWCrawler() {
		try {
			YGWRegionAndCategoryCrawler regcrawler = new YGWRegionAndCategoryCrawler();
			regionlist = regcrawler.getYGWRegionAndCategory("http://bk2.radio.cn/mms4/videoPlay/getAreaAndType.jspa");
			for (RegionPo regionPo : regionlist) {
				YGWBroadcastCrawler chCrawler = new YGWBroadcastCrawler();
				List<ChannelPo> chs = chCrawler.getYGWChannel(regionPo.getSrcId());
				chlist.addAll(chs);
			}
			new YGWUpdate().updatYGWInfo(regionlist, chlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new YGWCrawler().beginYGWCrawler();
	}
}
