package com.woting.crawler.core.boradcast.persis.po;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringEscapeUtils;

import com.spiritdata.framework.core.model.BaseObject;

public class ChannelPo extends BaseObject {

	private static final long serialVersionUID = 3689798849105581882L;
	
	private String id;
	private String chTitle;
	private String chId;
	private String chLiveId;
	private String frequency;
	private String regionId;
	private String regionName;
	private String categoryId;
	private String categoryName;
	private String chImg;
	private String chURL;
	private String flowURI;
	private String descn;
	private String publisher;
	private Timestamp cTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChTitle() {
		return chTitle;
	}
	public void setChTitle(String chTitle) {
		this.chTitle = chTitle;
	}
	public Timestamp getcTime() {
		return cTime;
	}
	public void setcTime(Timestamp cTime) {
		this.cTime = cTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getChImg() {
		return chImg;
	}
	public void setChImg(String chImg) {
		this.chImg = chImg;
	}
	public String getChURL() {
		return chURL;
	}
	public void setChURL(String chURL) {
		this.chURL = chURL;
	}
	public String getFlowURI() {
		return flowURI;
	}
	public void setFlowURI(String flowURI) {
		this.flowURI = flowURI;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = StringEscapeUtils.unescapeHtml4(descn);
	}
}
