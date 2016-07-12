package cn.myapp.util.weixin;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class OauthConfigUtil {
	
	private String appid ;
	private String secret ;
	private String redircturl ;
	
	public String getAppid() {

		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {

		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getRedircturl() {

		return redircturl;
	}
	public void setRedircturl(String redircturl) {
		this.redircturl = redircturl;
	}

	public OauthConfigUtil() {
		// TODO Auto-generated constructor stub
		Prop prop = PropKit.use("oauth_config.txt") ;
		this.setAppid(prop.get("kAPPID")) ;
		this.setSecret(prop.get("kSecret"));
		this.setRedircturl(prop.get("kRedirectUrl"));				
	}
	
	
}
