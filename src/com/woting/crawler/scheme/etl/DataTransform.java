package com.woting.crawler.scheme.etl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.util.SequenceUUID;
import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.persis.po.BCProgrammePo;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;

public abstract class DataTransform {
	
	public static Map<String, Object> getBcByCh(List<ChannelPo> chlist) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<BroadcastPo> bclist = new ArrayList<BroadcastPo>();
		List<BCLiveFlowPo> bclflist = new ArrayList<BCLiveFlowPo>();
		for (ChannelPo ch : chlist) {
			BroadcastPo bc = new BroadcastPo();
			BCLiveFlowPo bclf = new BCLiveFlowPo();
			String bcid = SequenceUUID.getUUIDSubSegment(4);
			bc.setId(bcid);
			bc.setBcTitle(ch.getChTitle());
			bc.setBcPubType(2);
			bc.setBcPublisher(ch.getRegionName() + "人民广播电台");
			bc.setBcImg(ch.getChImg());
			bc.setBcURL(ch.getChURL());
			bc.setDescn(ch.getDescn());
			bc.setPubCount(0);
			bclist.add(bc);
			bclf.setBcId(bcid);
			bclf.setBcSrcType(2);
			bclf.setBcSource("蜻蜓FM");
			bclf.setFlowURI(ch.getFlowURI());
			bclf.setIsMain(0);
			bclf.setBcSrcChannelId(ch.getChId());
			bclflist.add(bclf);
		}
		map.put("broadcast", bclist);
		map.put("bcliveflow", bclflist);
		return map;
	}

	public static BroadcastPo updateBcByCh(BroadcastPo bc, ChannelPo ch) {
		bc.setBcPublisher(ch.getRegionName() + "人民广播电台");
		bc.setBcImg(ch.getChImg());
		bc.setBcURL(ch.getChURL());
		bc.setDescn(ch.getDescn());
		return bc;
	}

	public static BCLiveFlowPo getBclfByBcAndCh(BroadcastPo bc, ChannelPo ch) {
		BCLiveFlowPo bclf = new BCLiveFlowPo();
		bclf.setBcId(bc.getId());
		bclf.setBcSource("蜻蜓FM");
		bclf.setFlowURI(ch.getFlowURI());
		bclf.setBcSrcChannelId(ch.getChId());
		return bclf;
	}

	public static List<BCProgrammePo> getBcpByBclfAndFes(Map<String, Object> bclfmap, List<ProgrammePo> feslist) {
		List<BCProgrammePo> bcplist = new ArrayList<BCProgrammePo>();
		for (ProgrammePo fs : feslist) {
			BCProgrammePo bcp = new BCProgrammePo();
			bcp.setBcId(bclfmap.get(fs.getChId()) + "");
			bcp.setTitle(fs.getTitle());
			bcp.setWeekDay(fs.getWeekDay());
			bcp.setBeginTime(fs.getBegintime());
			bcp.setEndTime(fs.getEndtime());
			bcplist.add(bcp);
		}
		return bcplist;
	}
}
