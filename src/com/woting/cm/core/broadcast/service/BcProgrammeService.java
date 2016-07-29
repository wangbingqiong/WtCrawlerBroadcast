package com.woting.cm.core.broadcast.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.woting.cm.core.broadcast.persis.po.BCProgrammePo;

@Service
public class BcProgrammeService {

	@Resource(name="defaultDAO_CM")
	private MybatisDAO<BCProgrammePo> bcProDao;
	
	@PostConstruct
    public void initParam() {
        bcProDao.setNamespace("A_PROGRAMME");
    }
	
	public void insertBCProgrammeList(List<BCProgrammePo> bcProlist) {
		bcProDao.insert("insertList", bcProlist);
	}
	
	public void deleteById(String id) {
		bcProDao.delete("deleteById", id);
	}
	
	public Map<String, Object> getBCProgrammeStr(){
		Map<String, Object> bcprostr = new HashMap<String,Object>();
		List<BCProgrammePo> bcprolist = bcProDao.queryForList("getBCProgrammeList");
		for (BCProgrammePo bcPro : bcprolist) {
			bcprostr.put(bcPro.getBcId()+bcPro.getWeekDay()+bcPro.getSort(), bcPro.getTitle()+bcPro.getBeginTime()+bcPro.getEndTime());
		}
		return bcprostr;
	}
}
