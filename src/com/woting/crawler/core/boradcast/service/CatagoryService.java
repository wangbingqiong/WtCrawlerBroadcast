package com.woting.crawler.core.boradcast.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.crawler.core.boradcast.persis.po.CatagoryPo;

@Service
public class CatagoryService {
	
	@Resource(name="defaultDAO")
	private MybatisDAO<CatagoryPo> catagoryDao;
	
	@PostConstruct
    public void initParam() {
        catagoryDao.setNamespace("A_CATAGORY");
    }
	
	public List<CatagoryPo> getCatagoryList(String publisher) {
		List<CatagoryPo> catagorylist = catagoryDao.queryForList("getCatagoryListByPublisher", publisher);
		return catagorylist;
	}
}
