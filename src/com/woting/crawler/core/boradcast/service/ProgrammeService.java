package com.woting.crawler.core.boradcast.service;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.crawler.core.boradcast.persis.po.ProgrammePo;

@Service
public class ProgrammeService {
	
	@Resource(name="defaultDAO")
	private MybatisDAO<ProgrammePo> pgDao;
	
	@PostConstruct
    public void initParam() {
        pgDao.setNamespace("A_PROGRAMME");
    }
	
	public List<ProgrammePo> getProgrammeList(String publisher) {
		List<ProgrammePo> prolist = pgDao.queryForList("", publisher);
		return prolist;
	}
	
	public void insertProgrammeList(List<ProgrammePo> pglist){
		pgDao.insert("insertlist", pglist);
	}
	
	public void deleteProgramme(String publisher) {
		pgDao.delete("deleteByPublisher", publisher);
	}
	
}
