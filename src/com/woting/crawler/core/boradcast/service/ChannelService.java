package com.woting.crawler.core.boradcast.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;

@Service
public class ChannelService {

	@Resource(name="defaultDAO")
	private MybatisDAO<ChannelPo> chDao;
	
	@PostConstruct
    public void initParam() {
        chDao.setNamespace("A_CHANNEL");
    }
	
	public void insertChannelList(List<ChannelPo> chlist){
		chDao.insert("insertList", chlist);
	}
	
	public List<ChannelPo> getChannelList(){
		List<ChannelPo> chlist = chDao.queryForList("getChList");
		return chlist;
	}
	
	public List<ChannelPo> getChannelList(String publisher){
		List<ChannelPo> chlist = chDao.queryForList("getChListByPublisher", publisher);
		return chlist;
	}
	
	public Map<String, Object> getChIdAndCateidMap(){
		List<ChannelPo> chlist = getChannelList();
		Map<String, Object> m = new HashMap<String,Object>();
		for (ChannelPo ch : chlist) {
			m.put(ch.getPublisher()+ch.getChId(), ch.getCategoryId());
		}
		return m;
	}
	
	public void deleteChannel(String publisher) {
		chDao.delete("deleteByPublisher", publisher);
	}
}
