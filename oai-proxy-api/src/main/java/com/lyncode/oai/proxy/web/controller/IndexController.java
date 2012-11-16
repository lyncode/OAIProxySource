package com.lyncode.oai.proxy.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	@RequestMapping(value="/")
	public ModelAndView indexAction (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index/index");
		String url = request.getRequestURL().toString().replaceFirst(request.getRequestURI(),"") + "/oai/request";
		mv.addObject("oaiurl", url);
		return mv;
	}
}
