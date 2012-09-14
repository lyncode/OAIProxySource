package com.lyncode.oai.proxy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value="/login.go", method = RequestMethod.GET)
	public String login(ModelMap model) {
 
		return "login";
 
	}
 
	@RequestMapping(value="/loginfailed.go", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	@RequestMapping(value="/logout.go", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "login";
 
	}
}
