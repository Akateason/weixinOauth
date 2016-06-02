package cn.myapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.core.Controller;
import com.mashape.unirest.http.exceptions.UnirestException;

import cn.myapp.module.weixin.BizHandler;
import cn.myapp.module.weixin.Click;
import cn.myapp.module.weixin.Page;
import cn.myapp.util.HttpRequest;

public class DownloadController extends Controller {
	
//	private final static int kFlexNum = 100 ;
	
	// biz
	public void testbiz() throws JsonParseException, JsonMappingException, UnirestException, IOException {
		
		
		BizHandler handler = new BizHandler() ;
		try {
			handler.loopInsert() ;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			testbiz();
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			testbiz();
			
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			
			testbiz();
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			testbiz();
			
			e.printStackTrace();
		} 
		
	}
	
	
	private final static String kURL_page = "http://weixindata.pullword.com:12345/page/129365000" ;
	// page
	public void testpage () throws UnirestException, JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("auth_usr", "subaojiang") ;
		
		String urlStr = kURL_page ;
		System.out.println(urlStr);
		String resp_fetch = HttpRequest.doGet(urlStr,map) ;
		System.out.println(resp_fetch) ;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> pageMap = objectMapper.readValue(resp_fetch, Map.class) ;		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> pages = (List<Map<String, String>>) pageMap.get("pages") ;
		String jsonFromArr = objectMapper.writeValueAsString(pages) ;
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Page.class);  
		List<Page> list = objectMapper.readValue(jsonFromArr, javaType) ;		
		for (Page page : list) {
			page.daoInsert("wx_page", "id") ;
			System.out.println(page) ;
		}
		
		renderJson("heheda") ;
	}
	
	private final static String kURL_click = "http://weixindata.pullword.com:12345/click/85502000" ;
	// click
	public void testclick() throws UnirestException, JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper() ;
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("auth_usr", "subaojiang") ;
		
		String urlStr = kURL_click ;
		System.out.println(urlStr);
		String resp_fetch = HttpRequest.doGet(urlStr,map) ;
		System.out.println(resp_fetch) ;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> clickMap = objectMapper.readValue(resp_fetch, Map.class) ;		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> clicks = (List<Map<String, String>>) clickMap.get("clicks") ;
		String jsonFromArr = objectMapper.writeValueAsString(clicks) ;
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Click.class);  
		List<Click> list = objectMapper.readValue(jsonFromArr, javaType) ;		
		for (Click click : list) {
			click.daoInsert("wx_click", "id") ;
			System.out.println(click) ;
		}
		
		renderJson("heheda") ;
		
	}
	
}
