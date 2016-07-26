package com.woting.crawler.core.boradcast.persis.po;

import java.sql.Timestamp;

import com.spiritdata.framework.core.model.BaseObject;

public class RegionPo extends BaseObject {

	private static final long serialVersionUID = -5803349026628226320L;
	private String id;
	private String srcId;
	private String regionName;
	private String regionURL;
	private String publisher;
	private Timestamp cTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionURL() {
		return regionURL;
	}
	public void setRegionURL(String regionURL) {
		this.regionURL = regionURL;
	}
	
}
