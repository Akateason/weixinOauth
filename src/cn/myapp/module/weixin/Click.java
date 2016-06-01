package cn.myapp.module.weixin;

import cn.myapp.model.DaoObject;

public class Click extends DaoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long 	id ;
	private String	url ;
	private String 	title ;
	private long 	read_num ;
	private long	like_num ;
	private long	ts ;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getRead_num() {
		return read_num;
	}
	public void setRead_num(long read_num) {
		this.read_num = read_num;
	}
	public long getLike_num() {
		return like_num;
	}
	public void setLike_num(long like_num) {
		this.like_num = like_num;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
}
