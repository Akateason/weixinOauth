package cn.myapp.util.weixin;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class AppPageUtil {

	private String pagePath ;
		
	public String getPagePath() {
		return pagePath;
	}
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	
	public AppPageUtil(String key) {
		// TODO Auto-generated constructor stub
		Prop prop = PropKit.use("appPage_config.txt") ;
		this.setPagePath(prop.get(key)) ;
	}
	
}
