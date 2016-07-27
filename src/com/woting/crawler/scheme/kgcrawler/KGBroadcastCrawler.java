package com.woting.crawler.scheme.kgcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;

public class KGBroadcastCrawler {

	public List<ChannelPo> getKGBroadcastList(String regionurl) {
		List<ChannelPo> chlist = new ArrayList<ChannelPo>();
		Document doc;
		try {
			doc = Jsoup.connect(regionurl).ignoreContentType(true).timeout(10000).get();
			String str = doc.select("body").get(0).html();
			str = StringEscapeUtils.unescapeHtml4(str);
			Map<String, Object> bcmap = (Map<String, Object>) JsonUtils.jsonToObj(str, Map.class);
			List<Map<String, Object>> bclist = (List<Map<String, Object>>) bcmap.get("program_info_list");
			for (Map<String, Object> m : bclist) {
				String hz = m.get("hz")+"";
				String chtitle = m.get("channel_name")+"";
				String chid = m.get("channel_key")+"";
				String regid = m.get("location_key")+"";
				String chimg = m.get("channel_image_url")+"";
				String flowurl = "http://fm.shuoba.org/channel3/"+chid+"/48.m3u8";
				ChannelPo ch = new ChannelPo();
				ch.setId(SequenceUUID.getUUIDSubSegment(4));
				ch.setChId(chid);
				ch.setChTitle(chtitle);
				ch.setFrequency(hz);
				ch.setRegionId(regid);
				ch.setPublisher("酷狗FM");
				ch.setChImg(chimg);
				ch.setFlowURI(flowurl);
				ch.setcTime(new Timestamp(System.currentTimeMillis()));
				chlist.add(ch);
			}
		} catch (Exception e) {e.printStackTrace();}
		return chlist;
	}
}
