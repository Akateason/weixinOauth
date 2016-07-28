package cn.myapp.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.weixin.oauth.model.SNSUserInfo;
import com.weixin.oauth.model.WeixinOauth2Token;

import cn.myapp.util.UrlEncode;
import cn.myapp.util.weixin.AdvancedUtil;
import cn.myapp.util.weixin.AppPageUtil;
import cn.myapp.util.weixin.OauthConfigUtil;
 
//	/weixin
public class WeixinOauthController extends Controller {
	 
	static Logger log = Logger.getLogger(WeixinOauthController.class.getName()) ;
	 
	// static 
	private final static String kOauthoLink = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect" ;	
	private final static String kSCOPE = "snsapi_userinfo" ;
	
	/**
	 * OauthInfo 
	 * appid appsecret redirecturl .	  
	 */
	private OauthConfigUtil oauthInfo() {
		OauthConfigUtil configUtil = new OauthConfigUtil() ;
		return configUtil ;		
	}
	
	/**
	 * 提供授权页面URL
	 * @param key . 应用名 . 例如fish **
	 */
	public void oauthUrl() {		
		String key = getPara("key") ;
		
		String strOauthLink = getOauthUrlWithKey(key) ;		
		System.out.println(strOauthLink);
		redirect(strOauthLink) ;
		render(getRender());
	}
	
	private String getOauthUrlWithKey(String key) {
		String encodingUrl = UrlEncode.urlEncodeUTF8(oauthInfo().getRedircturl()) ;
		System.out.println(encodingUrl) ;
		
		String strOauthLink = kOauthoLink ;
		strOauthLink = strOauthLink.replace("APPID", oauthInfo().getAppid()) ;
		strOauthLink = strOauthLink.replace("REDIRECT_URI", encodingUrl) ;
		strOauthLink = strOauthLink.replace("SCOPE", kSCOPE) ;
		strOauthLink = strOauthLink.replace("STATE", key) ;
		System.out.println(strOauthLink) ;
		return strOauthLink ;
	}
	
	/**
	 * 监听微信回调 . 获得openid 和 用户信息 .
	 * @param code,state 
	 * @throws ServletException
	 * @throws IOException
	 * @throws UnirestException 
	 */	
	public void listener() throws ServletException, IOException, UnirestException {
		
		getRequest().setCharacterEncoding("UTF-8");
		getResponse().setCharacterEncoding("UTF-8");		
		
		// 用户同意授权后，能获取到code
		String code = getPara("code") ;
		String key  = getPara("state") ;
		
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil
					.getOauth2AccessToken(oauthInfo().getAppid(), oauthInfo().getSecret(), code) ;
//			log.info("appid : " + oauthInfo().getAppid()) ;
//			log.info("secret : " + oauthInfo().getSecret()) ;
			if (weixinOauth2Token == null) {				
				redirect(getOauthUrlWithKey(key));
				render(getRender());
				return ;
			}
			
			String accessToken = weixinOauth2Token.getAccessToken() ;
//			log.info("accessToken : " + accessToken) ;
			String openId = weixinOauth2Token.getOpenId() ;
//			log.info("openID : " + openId) ;
			
			// Select user from DB .
			SNSUserInfo userInfo = SNSUserInfo.selectByOpenId(openId) ;
//			log.info("userinfo name : " + userInfo.getNickname()) ;
			
			// not exist in DB ?
			if (userInfo == null) {				
				// fetch from server. and insert .
				userInfo = AdvancedUtil.getSNSUserInfo(accessToken,openId) ;
				userInfo.setNickname(filterEmoji(userInfo.getNickname())) ;				
				
				userInfo.daoInsert("user", "userID") ;
				userInfo = SNSUserInfo.selectByOpenId(openId) ;
//				log.info("new user insert") ;
			}
			
			// return .
//			HashMap<String, Object> map = new HashMap<String, Object>() ;
//			Gson gson = new Gson() ;
//			map.put("oauth", gson.toJson(weixinOauth2Token)) ;
//			map.put("user", gson.toJson(userInfo)) ;			
//			String jString = map.toString() ;			
//			renderJson(gson.fromJson(jString, Map.class)) ;			
//			log.info("info : " + map.toString()) ;			
//			setAttr("info", map) ;			
			
			AppPageUtil pageUtil = new AppPageUtil(key) ;
			log.info("pagepath : " + pageUtil.getPagePath()) ;
//			render(pageUtil.getPagePath()) ;
			String urlStr = pageUtil.getPagePath() ;
			urlStr += "?info=" + openId ; //userInfo.getUserID() ;
			redirect(urlStr, true) ;
			render(getRender()) ;			
		}		
	}
	
	
	public String filterEmoji(String source) {  
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()) 
            {
                source = emojiMatcher.replaceAll("*");
                return source ; 
            }
        return source;
       }
       return source;  
    }
	
//	public void test () {
//		String urlStr = "http://wei.subaojiang.com/weiOperation/enyu1.html" ;
//		urlStr += "?info=" + "dadadadadadda332323223232" ;
//		
//		setAttr("info", "caonima") ;
//		
//		forwardAction(urlStr) ;
//		render(getRender()) ;	
//	}
	
}
 