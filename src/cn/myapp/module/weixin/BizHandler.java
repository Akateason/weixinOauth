package cn.myapp.module.weixin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;

import cn.myapp.util.HttpRequest;
import cn.myapp.util.json.JsonToMap;

public class BizHandler {
	
	private final static String kURL_range_biz = "http://weixindata.pullword.com:12345/biz/range" ;
	private final static String kURL_pull_biz  = "http://weixindata.pullword.com:12345/biz/" ;

	public void loopInsert() throws JsonParseException, JsonMappingException, UnirestException, IOException {
		
		String response = HttpRequest.sendGet(kURL_range_biz, "auth_usr=subaojiang") ;
		System.out.println(response);		
		// range of biz .
		Map<String, Object> rangeMap = JsonToMap.toMap(response) ;
		//int biz_minID = Integer.parseInt((rangeMap.get("minid")).toString()) ; 
		int biz_maxID = Integer.parseInt((rangeMap.get("maxid")).toString()) ;				
		long currentMaxID = Biz.maxID() ;
		
		while (currentMaxID + 1 <= biz_maxID) {			
			insertBizTable();			
			currentMaxID = Biz.maxID() ;
			System.out.println(currentMaxID);			
			try {
				Thread.sleep(500) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void insertBizTable() throws UnirestException, JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("auth_usr", "subaojiang") ;
		long maxID_fromDB = Biz.maxID() ;
		maxID_fromDB++ ;
		String urlStr = kURL_pull_biz + maxID_fromDB ;
		System.out.println(urlStr);
		String resp_fetch = HttpRequest.doGet(urlStr,map) ;
		System.out.println(resp_fetch) ;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> bizMap = objectMapper.readValue(resp_fetch, Map.class) ;		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> bizs = (List<Map<String, String>>) bizMap.get("bizs") ;
		String jsonFromArr = objectMapper.writeValueAsString(bizs) ;
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Biz.class);  
		List<Biz> list = objectMapper.readValue(jsonFromArr, javaType) ;		
		for (Biz biz : list) {
			if (biz.getId() < maxID_fromDB) continue ;							
			biz.daoInsert("wx_biz", "id") ;
		}
	}
}
