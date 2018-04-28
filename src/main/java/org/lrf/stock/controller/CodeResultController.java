package org.lrf.stock.controller;

import java.util.Date;
import java.util.Map;

import org.lrf.stock.service.CodeResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CodeResultController {

	@Autowired
	private CodeResultService codeResultService;

	@RequestMapping("show_coderesult")
	public String showCoderesult() {
		return "coderesult_table";
	}

	@RequestMapping("coderesults")
	@ResponseBody
	public Map<String, Object> coderesult(@RequestParam("keyWord")String keyWord,@RequestParam("startDate")Date startDate,@RequestParam("endDate")Date endDate) {
		return codeResultService.getCodeResultByKeyWordAndPeriod(keyWord,startDate,endDate);
	}
}
