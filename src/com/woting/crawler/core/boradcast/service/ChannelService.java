package com.woting.crawler.core.boradcast.service;
import java.util.List;
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
		chDao.insert("insertlist", chlist);
	}
	
	public List<ChannelPo> getChannelList(String publisher){
		List<ChannelPo> chlist = chDao.queryForList("getList", publisher);
		return chlist;
	}
	
	public void deleteChannel(String publisher) {
		chDao.delete("deleteByPublisher", publisher);
	}
}
