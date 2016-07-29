package com.woting.crawler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.jsonconf.JsonConfig;
import com.spiritdata.framework.util.JsonUtils;
import com.woting.cm.core.broadcast.persis.po.BCLiveFlowPo;
import com.woting.cm.core.broadcast.service.BcLiveFlowService;
import com.woting.cm.core.dict.mem._CacheDictionary;
import com.woting.cm.core.dict.model.DictModel;
import com.woting.cm.core.dict.service.DictService;
import com.woting.crawler.core.boradcast.persis.po.ChannelPo;
import com.woting.crawler.core.boradcast.service.ChannelService;
import com.woting.crawler.ext.SpringShell;
import com.woting.crawler.scheme.control.SchemeControl;
import com.woting.crawler.scheme.etl.CompareInfo;
import com.woting.crawler.scheme.kgcrawler.KGCrawler;
import com.woting.crawler.scheme.qtcrawler.QTCrawler;
import com.woting.crawler.scheme.ygwcrawler.YGWCrawler;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Booter {
	
	public static void main(String[] args) {
		long beginTime=System.currentTimeMillis();
        //获取运行路径
        String rootPath=Booter.class.getResource("").getPath();
        if (rootPath.indexOf("!")!=-1) {//jar包
            rootPath=rootPath.substring(0, rootPath.indexOf("!"));
            String[] _s=rootPath.split("/");
            if (_s.length>1) {
                rootPath="/";
                for (int i=0; i<_s.length-1; i++) {
                    if (_s[i].equals("file:")) continue;
                    if (_s[i].length()>0) rootPath+=_s[i]+"/";
                }
            }
        } else {//class
            rootPath=rootPath.substring(0, rootPath.length()-"com.woting.crawler".length()-1);
            String[] _s=rootPath.split("/");
            if (_s.length>1) {
                rootPath="/";
                for (int i=0; i<_s.length-1; i++) if (_s[i].length()>0) rootPath+=_s[i]+"/";
            }
        }
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("linux")||os.toLowerCase().startsWith("unix")||os.toLowerCase().startsWith("aix")) rootPath+="/";
        else if (os.toLowerCase().startsWith("window")&&rootPath.startsWith("/")) rootPath=rootPath.substring(1);
        SystemCache.setCache(new CacheEle<String>(CrawlerConstants.APP_PATH, "系统运行的路径", rootPath));

        //logback加载xml内容
        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            String logConfFileName="logback-log";
            if (os.toLowerCase().startsWith("linux")||os.toLowerCase().startsWith("unix")||os.toLowerCase().startsWith("aix")) logConfFileName="conf/"+logConfFileName+"-linux.xml";
            else if (os.toLowerCase().startsWith("window")) logConfFileName=rootPath+"conf/"+logConfFileName+"-window.xml";
            configurator.doConfigure(logConfFileName);
        } catch (JoranException e) {
            e.printStackTrace();
        }
        Logger logger = LoggerFactory.getLogger(Booter.class);
        logger.info("内容抓取，环境初始化开始");
        logger.info("系统运行路径 [{}]", (SystemCache.getCache(CrawlerConstants.APP_PATH)).getContent());
        logger.info("计算并加载运行目录，用时[{}]毫秒", System.currentTimeMillis()-beginTime);
        
      //Spring环境加载
        long _begin=System.currentTimeMillis();
        SpringShell.init();
        logger.info("加载Spring配置，用时[{}]毫秒", System.currentTimeMillis()-_begin);

//        //内容资源库环境加载
//        _begin=System.currentTimeMillis();
//        LoadCacheService loadCacheService=(LoadCacheService)SpringShell.getBean("loadCacheService");
//        loadCacheService.loadContent();
//        logger.info("加载内容资源库数据，用时[{}]毫秒", System.currentTimeMillis()-_begin);
//        logger.info("内容抓取，环境初始化结束，共用时[{}]毫秒", System.currentTimeMillis()-beginTime);
        
        //开始抓取 
//        SchemeControl schemeControl = new SchemeControl();
//        List<Map<String, Object>> resultlist = schemeControl.progress_1();
//        System.out.println(JsonUtils.objToJson(resultlist));
        DictService dictService = (DictService) SpringShell.getBean("dictService");
        _CacheDictionary _c = dictService.loadCache();
        System.out.println(JsonUtils.objToJson(_c.getDictModelById("2")));
	}
}
