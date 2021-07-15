package com.hydev.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
	// localhost:8090/
	// localhost:8090
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본 폴더 src/main/resources/
		// 뷰리졸버 설정 : templates 
		return "index";
	}
}
