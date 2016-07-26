package com.woting.crawler.scheme.qtcrawler;

import java.util.ArrayList;
import java.util.List;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;


public class QTCrawler {
	private List<RegionPo> regionlist;
	private List<ChannelPo> chlist = new ArrayList<ChannelPo>();
	private List<ProgrammePo> fslist = new ArrayList<ProgrammePo>();

	public boolean beginQTCrawler() {
		System.out.println("蜻蜓抓取开始");
		QTRegionCrawler region = new QTRegionCrawler();
		QTBroadcastCrawler chcrawler = new QTBroadcastCrawler();
		regionlist = region.getQTRegion("http://www.qingting.fm/#/categories/1209");
		for (RegionPo regionPo : regionlist) {
			try {
				List<ChannelPo> chs = chcrawler.getQTChannel("http://www.qingting.fm/s/categories/" + regionPo.getSrcId());
				chlist.addAll(chs);
				for (ChannelPo channelPo : chs) {
					try {
						QTProgrammeCrawler fscrawler = new QTProgrammeCrawler();
						fslist.addAll(fscrawler.begionQTProgrammeCralwer(channelPo.getChLiveId()));
					} catch (Exception e) {e.printStackTrace();continue;}
				}
			} catch (Exception e) {e.printStackTrace();continue;}
		}
		System.out.println("蜻蜓中间数据库更新");
		return false;
	}
}
