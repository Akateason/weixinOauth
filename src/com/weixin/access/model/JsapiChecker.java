package com.weixin.access.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.myapp.model.DaoObject;

@SuppressWarnings("serial")
public class JsapiChecker extends DaoObject {
	
	private int 	id ;
	private String	ticket ;
	private int		expires_in ;
	private long	inputTime ; 	// 入库时间
	private long	outputTime ; 	// 超时时间	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
	
	//////////////////////////
	
	public static JsapiChecker selectJsapiInfo() {
		Record record = Db.findFirst("select * from activity.jsapi") ; 				
		if (record != null) {
			return (JsapiChecker)new JsapiChecker().fetchFromRecord(record) ;
		}
		return null ;
	}
}
