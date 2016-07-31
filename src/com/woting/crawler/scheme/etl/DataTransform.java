package com.woting.crawler.scheme.etl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.jsonconf.JsonConfig;
import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.persis.po.BCProgrammePo;
import com.woting.cm.core.broadcast.persis.po.BroadcastPo;
import com.woting.cm.core.dict.persis.po.DictMasterPo;
import com.woting.cm.core.dict.persis.po.DictRefResPo;
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
			bc.setPubCount(1);
			bc.setcTime(new Timestamp(System.currentTimeMillis()));
			bclist.add(bc);
			bclf.setId(SequenceUUID.getUUIDSubSegment(4));
			bclf.setBcId(bcid);
			bclf.setBcSrcType(2);
			bclf.setBcSource(ch.getPublisher());
			bclf.setFlowURI(ch.getFlowURI());
			bclf.setIsMain(0);
			bclf.setBcSrcChannelId(ch.getChId());
			bclf.setcTime(new Timestamp(System.currentTimeMillis()));
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
		bclf.setBcSource(ch.getPublisher());
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
	
	public static List<BCLiveFlowPo> getUpdateBCLiveFlowByCh(List<ChannelPo> chlist,Map<String, Object> bclfmap){
		List<BCLiveFlowPo> updatebclflist = new ArrayList<BCLiveFlowPo>();
		for (ChannelPo ch : chlist) {
			if(bclfmap.containsKey(ch.getPublisher()+"::"+ch.getChId())){
				BCLiveFlowPo bclf = new BCLiveFlowPo();
				bclf.setBcSource(ch.getPublisher());
				bclf.setFlowURI(ch.getFlowURI());
				bclf.setBcSrcChannelId(ch.getChId());
				bclf.setcTime(new Timestamp(System.currentTimeMillis()));
				updatebclflist.add(bclf);
			}
		}
		return updatebclflist;
	}
	
	public static List<DictRefResPo> getResDictRegionByBcAndDictM(List<BroadcastPo> bclist, DictMasterPo dictm, Map<String, Object> dictd){
		List<DictRefResPo> reslist = new ArrayList<DictRefResPo>();
		for (BroadcastPo bc : bclist) {
			String dictdid = dictd.get(bc.getBcPublisher().replace("人民广播电台", ""))+"";
			if(!dictdid.equals("null")){
				DictRefResPo resdict = new DictRefResPo();
			    resdict.setId(SequenceUUID.getPureUUID());
			    resdict.setRefName("电台-"+dictm.getDmName());
			    resdict.setResTableName("wt_Broadcast");
			    resdict.setResId(bc.getId());
			    resdict.setDictMid(dictm.getId());
			    resdict.setDictMName(dictm.getDmName());
			    resdict.setDictDid(dictdid);
			    resdict.setTitle(bc.getBcPublisher().replace("人民广播电台", ""));
			    resdict.setBCode(dictdid);
			    resdict.setPathNames(bc.getBcPublisher().replace("人民广播电台", ""));
			    resdict.setPathIds(dictdid);
			    resdict.setCTime(new Timestamp(System.currentTimeMillis()));
			    reslist.add(resdict);
			}
		}
		return reslist;
	}
	
	public static List<DictRefResPo> getResDictCategory(List<BCLiveFlowPo> bclflist, DictMasterPo dictm, Map<String, Object> dictd, Map<String, Object> chid_cateid, JsonConfig jsonConfig){
		List<DictRefResPo> reslist = new ArrayList<DictRefResPo>();
		for (BCLiveFlowPo bclf : bclflist) {
			String jsonkey_1 = bclf.getBcSource()+chid_cateid.get(bclf.getBcSource()+bclf.getBcSrcChannelId())+""; //publish+cateid
			String jsonkey_2 = ""; //dicdid
			String dicdname = "";
			String dictdid = "";
			try {
				jsonkey_2 = jsonConfig.getString(jsonkey_1);
				dictdid = jsonConfig.getString(jsonkey_1);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			dicdname = dictd.get(jsonkey_2)+"";
			if(!dictdid.equals("null")){
				DictRefResPo resdict = new DictRefResPo();
			    resdict.setId(SequenceUUID.getPureUUID());
			    resdict.setRefName("电台-"+dictm.getDmName());
			    resdict.setResTableName("wt_Broadcast");
			    resdict.setResId(bclf.getBcId());
			    resdict.setDictMid(dictm.getId());
			    resdict.setDictMName(dictm.getDmName());
			    resdict.setDictDid(dictdid);
			    resdict.setTitle(dicdname);
			    resdict.setBCode(dictdid);
			    resdict.setPathNames(dicdname);
			    resdict.setPathIds(dictdid);
			    resdict.setCTime(new Timestamp(System.currentTimeMillis()));
			    reslist.add(resdict);
			}
		}
		return reslist;
	}
}
