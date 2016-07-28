package com.weixin.access.model.module;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SignatureBuilder {
	
	public String getWeixinRandomString() {		
		Random rand = new Random();		
		int length = 8 + rand.nextInt(24) ; 
		
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";  		
		Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
	}
	
	public long getWeixinTimestamp() {
		long currentTime = (long)((new Date().getTime()) / 1000) ;				
		return currentTime ;
	}
	
	public String combineStrings(String jsapi_ticket , String noncestr , long timestamp , String urlOrg) 
			throws JsonParseException, JsonMappingException, IOException {
		
		String combinedStr = "jsapi_ticket=" + jsapi_ticket 
				+ "&noncestr=" + noncestr 
				+ "&timestamp=" + timestamp 
				+ "&url=" + urlOrg ;
		System.out.println("combine : " + combinedStr);
		return combinedStr ;
	}
	
}
