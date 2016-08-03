package com.woting.crawler.core.boradcast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String, Object> map = new HashMap<String,Object>();
		List<ProgrammePo> prolist = new ArrayList<ProgrammePo>();
		List<ProgrammePo> plist = new ArrayList<ProgrammePo>();
		for (int i = 0; ; i++) {
			map.put("publisher", publisher);
		    map.put("beginnum", i*10000);
		    map.put("size", 10000);
		    System.out.println("查询执行了"+i);
		    plist = pgDao.queryForList("getProListByPublisher", map);
		    if(plist.size()==0)return prolist;
			prolist.addAll(plist);
		    map.clear();
		    plist.clear();
		}
	}
	
	public void insertProgrammeList(List<ProgrammePo> pglist){
		int num=0;
		List<ProgrammePo> pplist = new ArrayList<ProgrammePo>();
		for (int i = 0; i < pglist.size(); i++) {
			pplist.add(pglist.get(i));
			num++;
			if(num==1000) {
				pgDao.insert("insertlist", pplist);
				pplist.clear();
				num=0;
			}
		}
		pgDao.insert("insertlist", pplist);
	}
	
	public void deleteProgramme(String publisher) {
		pgDao.delete("deleteByPublisher", publisher);
	}
}
