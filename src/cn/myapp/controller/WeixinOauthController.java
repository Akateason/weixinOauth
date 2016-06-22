package cn.myapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.weixin.oauth.model.SNSUserInfo;
import com.weixin.oauth.model.WeixinOauth2Token;

import cn.myapp.util.UrlEncode;
import cn.myapp.util.weixin.AdvancedUtil;
import cn.myapp.util.weixin.OauthConfigUtil;

public class WeixinOauthController extends Controller {
		
	// static 
	//private final static String kURL_accesstoken = "https://api.weixin.qq.com/cgi-bin/token" ;
	private final static String kOauthoLink = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect" ;	
	private final static String kSCOPE = "snsapi_userinfo" ;
	
	/**
	 * OauthInfo 
	 * appid appsecret redirecturl .	  
	 */
	private OauthConfigUtil oauthInfo(String key) {
		OauthConfigUtil configUtil = new OauthConfigUtil(key) ;
		return configUtil ;		
	}
	
	/**
	 * 提供授权页面URL
	 * @param key . 应用名 . 例如fish **
	 */
	public void oauthUrl() {		
		String key = getPara("key") ;
		
		String encodingUrl = UrlEncode.urlEncodeUTF8(oauthInfo(key).getRedircturl()) ;
		System.out.println(encodingUrl) ;
		
		String strOauthLink = kOauthoLink ;
		strOauthLink = strOauthLink.replace("APPID", oauthInfo(key).getAppid()) ;
		strOauthLink = strOauthLink.replace("REDIRECT_URI", encodingUrl) ;
		strOauthLink = strOauthLink.replace("SCOPE", kSCOPE) ;
		strOauthLink = strOauthLink.replace("STATE", key) ;
		
		System.out.println(strOauthLink) ;
		renderJson(strOauthLink) ;
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
		String key = getPara("state") ;
		
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil
					.getOauth2AccessToken(oauthInfo(key).getAppid(), oauthInfo(key).getSecret(), code) ;
			String accessToken = weixinOauth2Token.getAccessToken();
			String openId = weixinOauth2Token.getOpenId();
			
			// Select user from DB .
			SNSUserInfo userInfo = SNSUserInfo.selectByOpenId(openId) ;
			
			// not exist in DB ?
			if (userInfo == null) {				
				// fetch from server. and insert .
				userInfo = AdvancedUtil.getSNSUserInfo(accessToken,openId) ;
				userInfo.daoInsert("user", "userID") ;
				userInfo = SNSUserInfo.selectByOpenId(openId) ;
			}
			
			// return .
			HashMap<String, Object> map = new HashMap<String, Object>() ;
			Gson gson = new Gson() ;
			map.put("oauth", gson.toJson(weixinOauth2Token)) ;
			map.put("user", gson.toJson(userInfo)) ;
			String jString = map.toString() ;
			renderJson(gson.fromJson(jString, Map.class)) ;
		}
		
	}
	
//	public void testToken() {				
//		HashMap<String, Object> paramMap = new HashMap<String, Object>() ; 
//		paramMap.put("grant_type", "client_credential") ;
//		paramMap.put("appid", oauthInfo().getAppid()) ;
//		paramMap.put("secret", oauthInfo().getSecret()) ;
//		
//		String response = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_accesstoken, paramMap) ;
//		System.out.println(response) ;
//		
//		renderJson(response) ;		
//	}
	
}
