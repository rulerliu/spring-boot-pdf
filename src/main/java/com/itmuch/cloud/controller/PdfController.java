package com.itmuch.cloud.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itmuch.cloud.util.PdfUtil;

@Controller
public class PdfController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/download")
	public void download (HttpServletResponse response) throws Exception {
		Object[][] datas = {{"区域", "总销售额", "总利润"}, {"江苏省" , 9045,  2256}, {"广东省", 3000, 690}};
		PdfUtil.export(response, datas);
	}
	
}
