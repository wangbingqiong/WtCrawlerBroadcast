package com.woting.crawler.core.boradcast.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.crawler.core.boradcast.persis.po.RegionPo;

@Service
public class RegionService {
	
	@Resource(name="defaultDAO")
	private MybatisDAO<RegionPo> regDao;
	
	@PostConstruct
    public void initParam() {
        regDao.setNamespace("A_REGION");
    }
	
	public List<RegionPo> getChannelList(String publisher){
		List<RegionPo> reglist = regDao.queryForList("getRegListByPublisher", publisher);
		return reglist;
	}
	
	public void insertRegionList(List<RegionPo> reglist) {
		regDao.insert("insertlist", reglist);
	}
	
	public void deleteRegion(String publisher) {
		regDao.delete("deleteByPublisher", publisher);
	}

	public Map<String, Object> getRegionMap(String publisher) {
		Map<String, Object> m = new HashMap<String,Object>();
		List<RegionPo> reglist = getChannelList(publisher);
		for (RegionPo regionPo : reglist) {
			m.put(regionPo.getSrcId(), regionPo.getRegionName());
		}
		return m;
	}
}
