package com.woting.crawler.core.boradcast.persis.po;

import java.sql.Timestamp;

import com.spiritdata.framework.core.model.BaseObject;

public class CategoryPo extends BaseObject {
	private static final long serialVersionUID = 2216989056832581267L;
	
	private String id;
	private String srcId;
	private String publisher;
	private String categoryName;
	private String categoryURL;
	private Timestamp cTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryURL() {
		return categoryURL;
	}
	public void setCategoryURL(String categoryURL) {
		this.categoryURL = categoryURL;
	}
	public Timestamp getcTime() {
		return cTime;
	}
	public void setcTime(Timestamp cTime) {
		this.cTime = cTime;
	}
}
