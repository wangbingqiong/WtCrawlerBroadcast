package com.woting.crawler.core.boradcast.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.crawler.core.boradcast.persis.po.CategoryPo;

@Service
public class CategoryService {
	
	@Resource(name="defaultDAO")
	private MybatisDAO<CategoryPo> categoryDao;
	
	@PostConstruct
    public void initParam() {
        categoryDao.setNamespace("A_CATEGORY");
    }
	
	public List<CategoryPo> getCategoryList(String publisher) {
		List<CategoryPo> categorylist = categoryDao.queryForList("getCategoryListByPublisher", publisher);
		return categorylist;
	}
	
	public List<CategoryPo> getCategoryList() {
		List<CategoryPo> categorylist = categoryDao.queryForList("getList");
		return categorylist;
	}
}
