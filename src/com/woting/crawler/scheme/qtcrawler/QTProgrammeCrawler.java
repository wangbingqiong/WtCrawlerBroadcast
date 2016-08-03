package com.woting.crawler.scheme.qtcrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;

public class QTProgrammeCrawler {
	
	List<ProgrammePo> fslist = new ArrayList<ProgrammePo>();
	
	@SuppressWarnings("unchecked")
	public List<ProgrammePo> begionQTProgrammeCralwer(String chLiveId){
		Document doc = null;
//		int weekday = GetData.getWeekDayNum();
		try {
			String url = "http://i.qingting.fm/qtapi/channellives/"+chLiveId+"/programs/day/1/2/3/4/5/6/7?_="+System.currentTimeMillis();
			doc = Jsoup.connect(url).ignoreContentType(true).timeout(10000).get();
			String flist = doc.getElementsByTag("body").html();
			flist = StringEscapeUtils.unescapeHtml4(flist);
			Map<String, Object> m = (Map<String, Object>) JsonUtils.jsonToObj(flist, Map.class);
			if(m.containsKey("data")) {
				Map<String, Object> ms = (Map<String, Object>) m.get("data");
				for (int i = 1; i < 8; i++) {
					List<Map<String, Object>> l = (List<Map<String, Object>>) ms.get(i+"");
					if(l!=null&&l.size()>0) {
						int sort = 0;
						for (Map<String, Object> map : l) {
							ProgrammePo fs = new ProgrammePo();
							Map<String, Object> mainfo =  (Map<String, Object>) map.get("mediainfo");
							String chid = mainfo.get("id")+"";
							fs.setId(SequenceUUID.getUUIDSubSegment(4));
							fs.setChId(chid);
							fs.setChLiveId(chLiveId);
							fs.setTitle(map.get("title")+"");
							fs.setBegintime(map.get("start_time")+"");
							fs.setEndtime(map.get("end_time")+"");
							fs.setDuration(map.get("duration")+"");
							fs.setWeekDay(i);
							fs.setSort(sort);
							fs.setcTime(new Timestamp(System.currentTimeMillis()));
							fs.setPublisher("蜻蜓FM");
							fs.setcTime(new Timestamp(System.currentTimeMillis()));
							fslist.add(fs);
							sort++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fslist;
	}
}
