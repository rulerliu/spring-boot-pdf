package com.itmuch.cloud.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itmuch.cloud.util.PdfUtil;

@Controller
public class PdfController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/download")
	public void download (HttpServletResponse response) throws Exception {
		String data = "[{\r\n" + 
    			"	\"idcard\": \"身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员16\",\r\n" + 
    			"	\"callNum\": \"13588888888\"	\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员二\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员一\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员15\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员14\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员13\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员11\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员51\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}]";
		JSONArray parseArray = JSONObject.parseArray(data);
		PdfUtil.export(response, parseArray);
	}
	
}
