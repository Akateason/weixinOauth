package cn.myapp.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jfinal.core.Controller;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.weixin.access.model.module.AccessFetcher;
import com.weixin.access.model.module.JsapiFetcher;
import com.weixin.access.model.module.SignatureBuilder;

import cn.myapp.model.ResultObj;
import cn.myapp.util.Sha1Util;

//	/weixin/action
public class AccessController extends Controller {
	
	/**
	 * API 
	 * GET ACCESS_TOKEN from global memory cache .
	 * @param key for which app .
	 * @return access_token for app .
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void accessToken() throws JsonParseException, JsonMappingException, IOException {
				
		AccessFetcher aFetcher = new AccessFetcher() ;		
		String access_token = aFetcher.fetchAccessToken() ; 
		
		HashMap<String, Object> returnData = new HashMap<String, Object>() ;
		returnData.put("access_token", access_token) ;
		ResultObj resultObj = new ResultObj(returnData) ;
		renderJson(resultObj) ;
	}
	
		
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * API
	 * get jsapi_ticket from global memory cache .
	 * @param key for which app .
	 * @return ticket
	 * @throws UnirestException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void jsapi() throws UnirestException, JsonParseException, JsonMappingException, IOException {
		
		JsapiFetcher jFetcher = new JsapiFetcher() ;
		String ticket = jFetcher.fetchJsapiTicket() ;
		
		HashMap<String, Object> returnData = new HashMap<String, Object>() ;
		returnData.put("ticket", ticket) ;
		ResultObj resultObj = new ResultObj(returnData) ;
		renderJson(resultObj) ;		
	}	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void sign() throws JsonParseException, JsonMappingException, NoSuchAlgorithmException, IOException {
		
		String url = getPara("url") ;
		
		SignatureBuilder signatureBuilder = new SignatureBuilder() ;		
		
		//1.jsapi
		JsapiFetcher jFetcher = new JsapiFetcher() ;
		String jsapi = jFetcher.fetchJsapiTicket() ;
		
		//2.nonceStr
		String noncestr = signatureBuilder.getWeixinRandomString() ;
		
		//3.time
		long timeStr = signatureBuilder.getWeixinTimestamp() ;
				
		String string1 = signatureBuilder.combineStrings(jsapi, noncestr, timeStr, url) ;
		
		//4. sign
		String signStr = Sha1Util.sha1(string1) ;
		
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("jsapi_ticket", jsapi) ;
		map.put("noncestr", noncestr) ;
		map.put("timestamp", timeStr) ;
		map.put("url", url) ;
		map.put("sign", signStr) ;
		
		renderJson(map) ;
	}
	
}
