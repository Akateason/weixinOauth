package cn.myapp.module.weixin;

import cn.myapp.model.DaoObject;

public class Page extends DaoObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long 		id ;
	private String		url ;
	private String 		title ;
	private String 		content ;
	private long 		ts ;
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
}
