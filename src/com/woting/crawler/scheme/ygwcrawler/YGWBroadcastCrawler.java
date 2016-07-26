package com.woting.crawler.scheme.ygwcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.spiritdata.framework.util.StringUtils;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;

public class YGWBroadcastCrawler {

	public List<ChannelPo> getYGWChannel(String srcId) {
		Document doc = null;
		List<ChannelPo> chlist = new ArrayList<ChannelPo>();
		try {
			doc = Jsoup.connect("http://bk2.radio.cn/mms4/videoPlay/pcGetChannels.jspa?area="+srcId+"&type=0").ignoreContentType(true).timeout(20000).get();
			String str = doc.select("body").get(0).html();
			str = str.replace("&quot;", "\"").replace("(", "").replace(")", "");
			str = str.substring(4, str.length());
			List<Map<String, Object>> chs = (List<Map<String, Object>>) JsonUtils.jsonToObj(str, List.class);
			for (Map<String, Object> m : chs) {
				ChannelPo ch = new ChannelPo();
				String chid = m.get("channelId") + "";
				String title = m.get("channelName") + "";
				String icon = m.get("icon") + "";
				String weiboUrl = m.get("weiboUrl") + "";
				if (StringUtils.isNullOrEmptyOrSpace(icon) && StringUtils.isNullOrEmptyOrSpace(weiboUrl))
					continue;
				doc = Jsoup.connect("http://bk2.radio.cn/mms4/videoPlay/getChannelPlayInfoJson.jspa?channelId=" + chid
				+ "&terminalType=PC&location=http%3A//www.radio.cn/index.php%3Foption%3Ddefault%2Cradio").ignoreContentType(true).get();
				str = doc.select("body").get(0).html();
				str = str.substring(4, str.length());
				str = str.replace("&quot;", "\"").replace("(", "").replace(")", "");
				Map<String, Object> m2 = (Map<String, Object>) JsonUtils.jsonToObj(str, Map.class);
				List<Map<String, Object>> streams = (List<Map<String, Object>>) m2.get("streams");
				if (streams != null && streams.size() > 0) {
					Map<String, Object> m3 = streams.get(0);
					String flowurl = m3.get("url") + "";
					ch.setFlowURI(flowurl);
				}
				ch.setChId(SequenceUUID.getUUIDSubSegment(4));
				ch.setChId(chid);
				ch.setChImg(icon);
				ch.setChTitle(title);
				ch.setRegionId(srcId);
				ch.setPublisher("央广网FM");
				ch.setcTime(new Timestamp(System.currentTimeMillis()));
				chlist.add(ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chlist;
	}
}
