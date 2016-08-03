package com.woting.crawler.scheme.qtcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;


public class QTBroadcastCrawler {

	@SuppressWarnings("unchecked")
	public List<ChannelPo> getQTChannel(String url){
		Document doc = null;
		List<ChannelPo> chlist = new ArrayList<ChannelPo>();
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).get();
			if(doc!=null && doc.getElementsByTag("ul").get(0)!=null) {
				Element ele = doc.getElementsByTag("ul").get(0);
				Elements eles = ele.getElementsByTag("li");
				for (Element el : eles) {
					ChannelPo ch = new ChannelPo();
					String chLiveId = el.select("a[href]").get(0).attr("href");
					chLiveId = chLiveId.replace("/channels/", "");
					String title = el.getElementsByTag("span").get(0).html();
					String jsonstr = el.select("li[data-play-info]").get(0).attr("data-play-info");
					Map<String, Object> m = (Map<String, Object>) JsonUtils.jsonToObj(jsonstr, Map.class);
					String chid = m.get("urls")+"";
					chid = chid.replace("[", "").replace("]", "");
					String chimg = m.get("thumb")+"";
					String regionid = url.replace("http://www.qingting.fm/s/categories/", "");
					ch.setId(SequenceUUID.getUUIDSubSegment(4));
					ch.setChTitle(title);
					ch.setChId(chid);
					ch.setChLiveId(chLiveId);
					ch.setChImg(chimg);
					ch.setRegionId(regionid);
					ch.setChURL("http://qingting.fm/channels/"+chLiveId);
					ch.setFlowURI("http://hls.qingting.fm/live/"+chid+".m3u8?bitrate=64&format=mpegts");
					ch.setDescn(m.get("desc")+"");
					ch.setPublisher("蜻蜓FM");
					ch.setcTime(new Timestamp(System.currentTimeMillis()));
					chlist.add(ch);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chlist;
	}
}
