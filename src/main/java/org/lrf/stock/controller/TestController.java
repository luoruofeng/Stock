package org.lrf.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	@RequestMapping("index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("main")
	public String main() {
		return "main";
	}
}
