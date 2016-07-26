package com.woting.crawler.scheme.kgcrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class KGThread extends Thread {

	private int begnum;
	private int endnum;
	int num;
	List<Map<String, Object>> list;

	public KGThread(int begnum, int endnum, List<Map<String, Object>> list, int num) {
		this.begnum = begnum;
		this.endnum = endnum;
		this.list = list;
		this.num = num;
	}

	private synchronized void putList(Map<String, Object> m) {
		list.add(m);
	}

	private synchronized void addNum() {
		num=num+1;
	}

	@Override
	public void run() {
		Document doc;
		for (int i = begnum; i < endnum; i++) {
			try {
				doc = Jsoup.connect("http://fm.kugou.com/share/channel/" + i + "/?source=qq?r=1466758594")
						.timeout(100000).ignoreContentType(true).get();
				Elements elements = doc.select("meta[name=title]");
				String name = elements.get(0).attr("content").replace("酷狗FM--", "");
				elements = doc.select("img[id=logo]");
				String img = elements.get(0).attr("src");
				elements = doc.select("source[id=sourceUrl]");
				String url = elements.get(0).attr("src");
				Jsoup.connect(url).ignoreContentType(true).get();
				if (name.contains("未知") || name.contains("测试")) continue;
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("channelName", name);
				m.put("channelImg", img);
				m.put("url", url);
				putList(m);
			} catch (Exception e) {
//				e.printStackTrace();
				continue;
			}
		}
		addNum();
	}
}
