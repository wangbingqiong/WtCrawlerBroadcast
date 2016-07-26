package com.woting.crawler.scheme.qtcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.spiritdata.framework.util.SequenceUUID;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

public class QTRegionCrawler {
	
	public List<RegionPo> getQTRegion(String url){
		Document doc = null;
		List<RegionPo> reglist = new ArrayList<RegionPo>();
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).get();
			if(doc!=null) {
				Elements eles = doc.select("div[class=sub-categories clearfix]");
				if(eles.size()>0) {
					Elements els = eles.get(0).getElementsByTag("a");
					if(els.size()>0) {
						for (Element el : els) {
							RegionPo reg = new RegionPo();
							String hrefid = el.attr("href");
							String title = el.getElementsByTag("h5").get(0).html();
							hrefid = hrefid.replace("/categories/", "");
							reg.setId(SequenceUUID.getUUIDSubSegment(4));
							reg.setSrcId(hrefid);
							reg.setRegionURL("http://www.qingting.fm/#/categories/"+hrefid);
							reg.setRegionName(title);
							reg.setPublisher("蜻蜓FM");
							reg.setcTime(new Timestamp(System.currentTimeMillis()));
							reglist.add(reg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reglist;
	}
}
