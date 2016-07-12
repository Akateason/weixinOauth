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
import com.weixin.access.model.JsapiChecker;

import cn.myapp.util.HttpRequest;
import cn.myapp.util.HttpRequest.TypeOfRequest;

public class JsapiFetcher {

	private final static String kURL_JSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket" ;

	public String fetchJsapiTicket() throws JsonParseException, JsonMappingException, IOException {
		
		Gson gson = new Gson() ;
		long currentTime = (long)((new Date().getTime()) / 1000) ;
		
		// 1 get access_token (cached in memory .)
		AccessFetcher aFetcher = new AccessFetcher() ;
		String access_token = aFetcher.fetchAccessToken() ; 
		
		// 2 get jsapi_ticket .
		// select access from DB .		
		JsapiChecker jChecker = JsapiChecker.selectJsapiInfo() ;
		
		// 1 exist . go check it
		if (jChecker != null) {
			// 1.1 intime return .
			System.out.println("jsapi current : " + currentTime);
			System.out.println("jsapi output  : " + jChecker.getOutputTime());
			
			if (currentTime < jChecker.getOutputTime()) {
				System.out.println("jsapi intime ticket is : " + jChecker.getTicket()) ;
			}
			// 1.2 expire . request .
			else {				
				jChecker = getJspaiWithKey(access_token, gson, currentTime) ;
				// 1.2.1 update DB .
				Record record = Db.findFirst("select * from activity.jsapi")
						.set("ticket", jChecker.getTicket())
						.set("expires_in", jChecker.getExpires_in())
						.set("inputTime", jChecker.getInputTime())
						.set("outputTime", jChecker.getOutputTime()) ;
				Db.update("jsapi", record) ;
				System.out.println("jsapi overtime : " + jChecker.getTicket()) ;
			}	
		}
		// 2 not exist . request and insert in DB . 
		else {						
			jChecker = getJspaiWithKey(access_token, gson, currentTime) ;
			jChecker.daoInsert("jsapi", "id");
		}
		
		return jChecker.getTicket() ;
	}
	
	protected JsapiChecker getJspaiWithKey(String accessToken,Gson gson, long currentTime) throws JsonParseException, JsonMappingException, IOException {
		String response = requestForJsapi(accessToken,"jsapi") ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		@SuppressWarnings("unchecked")
		Map<String, Object> map = objectMapper.readValue(response, Map.class) ;		
		
		String ticket = (String) map.get("ticket") ;
		Integer expires_in = (Integer) map.get("expires_in") ;
		
		JsapiChecker jChecker = new JsapiChecker() ;		
		jChecker.setTicket(ticket);
		jChecker.setExpires_in(expires_in);
				
		long overTime = currentTime + expires_in ;
		jChecker.setInputTime(currentTime) ;
		jChecker.setOutputTime(overTime) ;
		
		return jChecker ;
	}
	
	protected String requestForJsapi(String access_token, String type) {
		// do request .
		HashMap<String, Object> paramMap = new HashMap<String, Object>() ; 
		paramMap.put("access_token", access_token) ;
		paramMap.put("type", type) ;
		
		String response = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_JSAPITICKET, paramMap) ;
		System.out.println(response) ;
		return response ;
	}
	
	
}
