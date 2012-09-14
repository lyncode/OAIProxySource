package com.lyncode.oai.proxy.web.controller;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lyncode.oai.proxy.core.ConfigurationManager;
import com.lyncode.oai.proxy.core.RepositoryManager;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.verbs.Identify;

@Controller
public class RepositoryController {
	private static Logger log = LogManager.getLogger(RepositoryController.class);
	
	private String nextRun () {
		org.apache.commons.configuration.Configuration config = ConfigurationManager.getConfiguration();
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, config.getInt("schedule.hour", 0));
		c.set(Calendar.MINUTE, config.getInt("schedule.minute", 0));
		c.set(Calendar.SECOND, 0);
		
		if (c.before(Calendar.getInstance())) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return c.getTime().toString();
	}
	
	@RequestMapping("/admin_repositories.go")
	public ModelAndView viewRepositories (HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("repositories");
		mv.addObject("error", false);
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		return mv;
	}
	
	@RequestMapping("/admin_repositories_add.go")
	public ModelAndView addRepository (HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		
		String url = request.getParameter("url");
		Configuration c = new Configuration();
		c.setResumptionInterval(ConfigurationManager.getConfiguration().getInt("oai.proxy.interval", 0));
		HarvesterManager h = new HarvesterManager(c, url);
		Identify id = null;
		try {
			id = h.identify();
			mv.addObject("error", false);
			mv.setViewName("addstep1");
			mv.addObject("url", url);
			mv.addObject("name", id.getRepositoryName());
		} catch (BadArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories");
			mv.addObject("error", true);
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (InternalHarvestException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories");
			mv.addObject("error", true);
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (IllegalArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories");
			mv.addObject("error", true);
			mv.addObject("message", "Invalid URL");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (IllegalStateException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories");
			mv.addObject("error", true);
			mv.addObject("message", "Invalid URL");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		}
		
		return mv;
	}

	@RequestMapping("/admin_repositories_del.go")
	public ModelAndView deleteRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories");
		mv.addObject("error", false);
		
		String id = request.getParameter("id");
		if (id != null) {
			RepositoryManager.delete(RepositoryManager.getByID(id));
		}
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}


	@RequestMapping("/admin_repositories_activate.go")
	public ModelAndView activateRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories");
		mv.addObject("error", false);
		
		String id = request.getParameter("id");
		if (id != null) {
			Repository r = RepositoryManager.getByID(id);
			r.setActive(true);
			try {
				RepositoryManager.save(r);
			} catch (MarshallingException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			} catch (IOException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			}
		}
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}

	@RequestMapping("/admin_repositories_deactivate.go")
	public ModelAndView deactivateRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories");
		mv.addObject("error", false);
		
		String id = request.getParameter("id");
		if (id != null) {
			Repository r = RepositoryManager.getByID(id);
			r.setActive(false);
			try {
				RepositoryManager.save(r);
			} catch (MarshallingException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			} catch (IOException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			}
		}
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}

	@RequestMapping("/admin_repositories_add_final.go")
	public ModelAndView addRepositoryFinal (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories");
		
		
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		boolean active = request.getParameter("active") != null;
		String set = request.getParameter("set");
		if (set.trim().equals("Set (Blank if none)")) set = null;
		
		Repository r = new Repository();
		r.setActive(active);
		r.setLastHarvest("None");
		r.setName(name);
		r.setURL(url);
		r.setSet(set);
		

		try {
			RepositoryManager.create(r);
			mv.addObject("error", false);
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (MarshallingException e) {
			mv.setViewName("addstep1");
			mv.addObject("error", true);
			mv.addObject("message", "Error while saving repository "+name+". Please try again, if the problem persists contact Lyncode.");
			log.debug(e.getMessage(), e);
		} catch (IOException e) {
			mv.setViewName("addstep1");
			mv.addObject("error", true);
			mv.addObject("message", "Error while saving repository "+name+". Please try again, if the problem persists contact Lyncode.");
			log.debug(e.getMessage(), e);
		}
		
		return mv;
	}
}
