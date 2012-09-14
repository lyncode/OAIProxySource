package com.lyncode.oai.proxy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lyncode.oai.proxy.core.ConfigurationManager;
import com.lyncode.oai.proxy.core.RepositoryManager;
import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.verbs.Identify;

@Controller
public class RepositoryController {
	private static Logger log = LogManager.getLogger(RepositoryController.class);
	
	@RequestMapping("/admin_repositories.do")
	public ModelAndView viewRepositories (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("addstep1");
		mv.addObject("error", false);
		mv.addObject("repositories", RepositoryManager.getRepositories());
		return mv;
	}
	
	@RequestMapping("/admin_repositories_add.do")
	public ModelAndView addRepository (HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		
		String url = request.getParameter("url");
		Configuration c = new Configuration();
		c.setResumptionInterval(ConfigurationManager.getConfiguration().getInt("oai.proxy.interval", 0));
		HarvesterManager h = new HarvesterManager(c, url);
		Identify id = null;
		try {
			id = h.identify();
			mv.setViewName("addstep2");
			mv.addObject("url", url);
			mv.addObject("name", id.getRepositoryName());
		} catch (BadArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("addstep1");
			mv.addObject("error", true);
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (InternalHarvestException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("addstep1");
			mv.addObject("error", true);
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (IllegalArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("addstep1");
			mv.addObject("error", true);
			mv.addObject("message", "Invalid URL");
			mv.addObject("repositories", RepositoryManager.getRepositories());
		}
		
		return mv;
	}
}
