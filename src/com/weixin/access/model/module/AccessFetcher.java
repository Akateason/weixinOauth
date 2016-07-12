package com.weixin.access.model.module;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.weixin.access.model.AccessChecker;

import cn.myapp.util.HttpRequest;
import cn.myapp.util.HttpRequest.TypeOfRequest;
import cn.myapp.util.weixin.OauthConfigUtil;

public class AccessFetcher {
	
	private final static String kURL_accesstoken = "https://api.weixin.qq.com/cgi-bin/token" ;
	
	/**
	 * OauthInfo 
	 * @appid, @appsecret, @redirecturl .	  
	 */
	private OauthConfigUtil oauthInfo() {
		OauthConfigUtil configUtil = new OauthConfigUtil() ;
		return configUtil ;		
	}
	
	/**
	 * FUNC
	 * fetch access_token ( global cache in memory ).
	 */
	public String fetchAccessToken() throws JsonParseException, JsonMappingException, IOException {			
		
		Gson gson = new Gson() ;
		long currentTime = (long)((new Date().getTime()) / 1000) ;
		
		// select access from DB .
		AccessChecker aChecker = AccessChecker.selectAccessInfo() ;
		// 1 exist . go check it
		if (aChecker != null) {
			// 1.1 intime return .
			System.out.println("current : " + currentTime);
			System.out.println("output : " + aChecker.getOutputTime());
			
			if (currentTime < aChecker.getOutputTime()) {
				System.out.println("intime : " + aChecker.getAccess_token()) ;
			}
			// 1.2 expire . request .
			else {
				aChecker = getAccessWithKey(gson, currentTime) ;				
				// 1.2.1 update DB .
				Record record = Db.findFirst("select * from activity.access")
						.set("access_token", aChecker.getAccess_token())
						.set("expires_in", aChecker.getExpires_in())
						.set("inputTime", aChecker.getInputTime())
						.set("outputTime", aChecker.getOutputTime()) ;
				Db.update("access", record) ;
				System.out.println("overtime : " + aChecker.getAccess_token()) ;
			}	
		}
		// 2 not exist . request and insert in DB . 
		else {
			aChecker = getAccessWithKey(gson, currentTime) ;
			aChecker.daoInsert("access", "id") ;
			System.out.println("get new : " + aChecker.getAccess_token()) ;
		}
		
		return aChecker.getAccess_token() ;
	}
	
	protected String requestForAccessToken() {
		// do request .
		HashMap<String, Object> paramMap = new HashMap<String, Object>() ; 
		paramMap.put("grant_type", "client_credential") ;
		paramMap.put("appid", oauthInfo().getAppid()) ;
		paramMap.put("secret", oauthInfo().getSecret()) ;
		String response = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_accesstoken, paramMap) ;
		System.out.println(response) ;
		return response ;
	}
	
	protected AccessChecker getAccessWithKey(Gson gson,long currentTime) 
			throws JsonParseException, JsonMappingException, IOException {
		String response = requestForAccessToken() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		@SuppressWarnings("unchecked")
		Map<String, Object> map = objectMapper.readValue(response, Map.class) ;		
		
		String access_token = (String) map.get("access_token") ;
		Integer expires_in = (Integer) map.get("expires_in") ;
		
		AccessChecker aChecker = new AccessChecker() ;		
		aChecker.setAccess_token(access_token) ;
		aChecker.setExpires_in(expires_in) ;
		
		long overTime = currentTime + expires_in ;
		aChecker.setInputTime(currentTime) ;
		aChecker.setOutputTime(overTime) ;
		
		return aChecker ;
	}
	
}
