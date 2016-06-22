package cn.myapp.util.weixin;

import java.util.Map;

import com.google.gson.Gson;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class OauthConfigUtil {
	
	private Map<String, Object> info ;		
	private String appid ;
	private String secret ;
	private String redircturl ;
	
	public String getAppid() {
		if (appid == null) {
			appid = (String) this.info.get("kAPPID") ;
		}
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		if (secret == null) {
			secret = (String) this.info.get("kSecret") ;
		}
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getRedircturl() {
		if (redircturl == null) {
			redircturl = (String) this.info.get("kRedirectUrl") ;
		}
		return redircturl;
	}
	public void setRedircturl(String redircturl) {
		this.redircturl = redircturl;
	}
	public Map<String, Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}
	
	public OauthConfigUtil(String Key) {
		// TODO Auto-generated constructor stub
		this.setInfo(getOauthInfo(Key)) ;
	}

	private Map<String, Object> getOauthInfo(String key) {
		Prop prop = PropKit.use("oauth_config.txt") ; 
		String strOauthInfo =  prop.get(key) ;
		Gson gson = new Gson() ;
		@SuppressWarnings("unchecked")
		Map<String, Object> map = gson.fromJson(strOauthInfo, Map.class) ;
		return map ;
	}
	
	
}
