package com.weixin.access.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.myapp.model.DaoObject;

@SuppressWarnings("serial")
public class AccessChecker extends DaoObject {
	
	private int		id ;
	private String  access_token ;	// wx
	private int     expires_in ;	// wx
	private long	inputTime ; 	// 入库时间
	private long	outputTime ; 	// 超时时间 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public long getInputTime() {
		return inputTime;
	}
	public void setInputTime(long inputTime) {
		this.inputTime = inputTime;
	}
	public long getOutputTime() {
		return outputTime;
	}
	public void setOutputTime(long outputTime) {
		this.outputTime = outputTime;
	}	
	
	////////////////////////////////////////////////////////////////////
	
	public static AccessChecker selectAccessInfo() {
		Record record = Db.findFirst("select * from activity.access") ;		
		if (record != null) {
			return (AccessChecker)new AccessChecker().fetchFromRecord(record) ;
		}
		return null ;
	}
	
}
