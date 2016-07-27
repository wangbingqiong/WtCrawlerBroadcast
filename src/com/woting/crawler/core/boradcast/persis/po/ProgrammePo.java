package com.woting.crawler.core.boradcast.persis.po;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringEscapeUtils;

import com.spiritdata.framework.core.model.BaseObject;

public class ProgrammePo extends BaseObject {

	private static final long serialVersionUID = 2709001766491122551L;
	
	private String id;
	private String chId; //id
	private String chLiveId; //蜻蜓平台里电台节目资源对应Id
	private String title; //节目名称
	private int weekDay; //星期的第几天
	private int sort;
	private String begintime; //节目开始直播时间
	private String endtime; //节目结束直播时间
	private String duration; //节目持续时长
	private Timestamp cTime; //节目播放时长
	private String publisher; //抓取平台
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getChId() {
		return chId;
	}
	public void setChId(String chId) {
		this.chId = chId;
	}
	public String getChLiveId() {
		return chLiveId;
	}
	public void setChLiveId(String chLiveId) {
		this.chLiveId = chLiveId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = StringEscapeUtils.unescapeHtml4(title);
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Timestamp getcTime() {
		return cTime;
	}
	public void setcTime(Timestamp cTime) {
		this.cTime = cTime;
	}
	
}
