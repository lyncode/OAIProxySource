package com.lyncode.oai.proxy.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyncode.oai.proxy.model.dao.api.UserDao;
import com.lyncode.oai.proxy.model.entity.User;

@Controller
public class MemberController {
	private static Logger log = LogManager.getLogger(MemberController.class);
	@Autowired UserDao userRepository;
	
	@RequestMapping(value="/enter", method = RequestMethod.GET)
	public String enter(ModelMap model) {
		return "member/enter";
 
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "member/login";
 
	}
	@RequestMapping(value="/member/index", method = RequestMethod.GET)
	public String member(ModelMap model) {
		return "member/area";
 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", true);
		return "member/login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "index/index";
 
	}
	

	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register(ModelMap model) {
		model.put("success", false);
		model.put("email", "");
		return "member/register";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerPost(HttpServletRequest req, ModelMap model) {
		String email = req.getParameter("email");
		String pwd = req.getParameter("pwd");
		String pwd2 = req.getParameter("pwda");
		
		model.put("email", "");
		model.put("success", false);
		
		if (pwd.length() < 5) {
			model.put("error", "Password too short");
			model.put("email", email);
		} else {
			if (!pwd.equals(pwd2)) {
				model.put("error", "Passwords do not match");
				model.put("email", email);
			} else {
				User u = userRepository.selectUserByEmail(email);
				
				if (u != null) {
					model.put("error", "Email already in use");
				} else {
					User user = new User();
					user.setEmail(email);
					user.setPassword(pwd);
					user.setActive(true);
					user.setActivationKey(userRepository.generateActivationKey());
					userRepository.saveUser(user);
					model.put("success", true);
					log.debug("User successfully registered");
				}
			}
		}
		
		return "member/register";
	}
}
