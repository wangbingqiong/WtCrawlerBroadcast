package com.woting.crawler.core.boradcast.persis.po;

import java.sql.Timestamp;

import com.spiritdata.framework.core.model.BaseObject;

public class CatagoryPo extends BaseObject {
	private static final long serialVersionUID = 2216989056832581267L;
	
	private String id;
	private String srcId;
	private String publisher;
	private String catagoryName;
	private String catagoryURL;
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
	public String getCatagoryName() {
		return catagoryName;
	}
	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}
	public String getCatagoryURL() {
		return catagoryURL;
	}
	public void setCatagoryURL(String catagoryURL) {
		this.catagoryURL = catagoryURL;
	}
	public Timestamp getcTime() {
		return cTime;
	}
	public void setcTime(Timestamp cTime) {
		this.cTime = cTime;
	}
}
