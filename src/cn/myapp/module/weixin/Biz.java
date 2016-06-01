package cn.myapp.module.weixin;

import com.jfinal.plugin.activerecord.Db;
import cn.myapp.model.DaoObject;

public class Biz extends DaoObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long 		id ;
	private String		bid ;
	private String 		name ;
	private String 		code ;
	private String 		info ;
	private String 		qrcode ;
	private long		ts ;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
	public static long maxID() {				 
		long maxID = Db.queryNumber("SELECT MAX(id) FROM wx_biz").longValue() ;		
		return maxID ;
	}
}
