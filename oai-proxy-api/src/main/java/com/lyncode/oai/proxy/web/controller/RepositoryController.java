package com.lyncode.oai.proxy.web.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lyncode.oai.proxy.ProxyApplication;
import com.lyncode.oai.proxy.core.ConfigurationManager;
import com.lyncode.oai.proxy.core.RepositoryManager;
import com.lyncode.oai.proxy.harvest.ProxyHarvester;
import com.lyncode.oai.proxy.job.HarvestJob;
import com.lyncode.oai.proxy.util.DateUtils;
import com.lyncode.oai.proxy.util.PatternUtils;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import com.lyncode.xoai.serviceprovider.verbs.Identify;

@Controller
public class RepositoryController {
	private static Logger log = LogManager.getLogger(RepositoryController.class);
	

	@RequestMapping(value="/repository/add", method = RequestMethod.GET)
	public String addRepositoryStep1 (Model model) {
		
		return "repository/add/step1";
	}

	@RequestMapping(value="/repository/add/step2", method = RequestMethod.POST)
	public String addRepositoryStep2 (HttpServletRequest request, Model model) {
		String url = request.getParameter("url").trim();
		model.addAttribute("url", url);
		if (!url.startsWith("http://"))
			url = "http://" + url;
		
		if (PatternUtils.validHttpURI(url)) {
			// Harvester Configuration
			Configuration configuration = new Configuration();
			configuration.setResumptionInterval(ConfigurationManager.getConfiguration().getInt("oai.proxy.interval", 0));
			
			// Harvester Manager
			HarvesterManager manager = new HarvesterManager(configuration, url);
			
			// Identify it!
			try {
				Identify identify = manager.identify();
				model.addAttribute("identify", identify);
				return "repository/add/step2";
			} catch (InternalHarvestException e) {
				log.debug(e.getMessage(), e);
				model.addAttribute("error", "The URL given do not behave as an OAI-PMH interface, please provide a valid URL.");
			} catch (BadArgumentException e) {
				log.error(e.getMessage(), e);
				model.addAttribute("error", "Something went wrong, please try again. If the problem persists, please contact the administrators. Thanks");
			}
		} else
			model.addAttribute("error", "The URL given isn't valid");

		return "repository/add/step1";
	}
	
	private String nextRun () {
		return ProxyApplication.getTrigger().getNextFireTime().toString();
	}
	
	@RequestMapping("/admin_repositories.go")
	public ModelAndView viewRepositories (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
		mv.addObject("isAdmin", true);
		mv.addObject("error", false);
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		mv.addObject("harvest", HarvestJob.isRunning());
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
			mv.setViewName("repositories/add");
			mv.addObject("url", url);
			mv.addObject("name", id.getRepositoryName());
		} catch (BadArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories/index");
			mv.addObject("error", true);
			mv.addObject("harvest", HarvestJob.isRunning());
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (InternalHarvestException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories/index");
			mv.addObject("error", true);
			mv.addObject("harvest", HarvestJob.isRunning());
			mv.addObject("message", "Unable to harvest repository");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (IllegalArgumentException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories/index");
			mv.addObject("error", true);
			mv.addObject("harvest", HarvestJob.isRunning());
			mv.addObject("message", "Invalid URL");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (IllegalStateException e) {
			log.debug(e.getMessage(), e);
			mv.setViewName("repositories/index");
			mv.addObject("error", true);
			mv.addObject("message", "Invalid URL");
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		}
		
		return mv;
	}

	@RequestMapping("/admin_repositories_reset.go")
	public ModelAndView resetRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
		mv.addObject("error", false);
		
		String id = request.getParameter("id");
		if (id != null) {
			Repository r = RepositoryManager.getByID(id);
			try {
				RepositoryManager.delete(r);
				r.setLastHarvest("None");
				RepositoryManager.create(r);
			} catch (IOException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			} catch (MarshallingException e) {
				log.debug(e.getMessage(), e);
				mv.addObject("error", true);
				mv.addObject("message", "Unable to update repository");
			}
		}
		mv.addObject("harvest", HarvestJob.isRunning());
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}
	

	@RequestMapping("/admin_run_harvest.go")
	public ModelAndView runHarvest (HttpServletRequest request) {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				log.info("Aggregation started");
				if (!HarvestJob.isRunning()) {
					HarvestJob.setRunning(true);
					for (Repository r : RepositoryManager.getRepositories()) {
						if (r.isActive()) {
							ProxyHarvester h = new ProxyHarvester(r);
							try {
								h.harvest();
								r.setLastHarvest(DateUtils.formatToSolr(new Date()));
							} catch (CannotDisseminateFormatException e1) {
								r.setLastHarvest("Error: "+e1.getMessage());
								log.debug(e1.getMessage(), e1);
							} catch (NoSetHierarchyException e1) {
								r.setLastHarvest("Error: "+e1.getMessage());
								log.debug(e1.getMessage(), e1);
							} catch (InternalHarvestException e1) {
								r.setLastHarvest("Error: "+e1.getMessage());
								log.debug(e1.getMessage(), e1);
							}
							try {
								RepositoryManager.save(r);
							} catch (MarshallingException e) {
								log.error(e.getMessage(), e);
							} catch (IOException e) {
								log.error(e.getMessage(), e);
							}
						}
					}
					HarvestJob.setRunning(false);
				} else log.info("Another aggregation is running");
				log.info("Aggregation ended");
			}
		});
		th.start();
		return new ModelAndView("repositories/harvest");
	}
	
	@RequestMapping("/admin_repositories_del.go")
	public ModelAndView deleteRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
		mv.addObject("error", false);
		
		String id = request.getParameter("id");
		if (id != null) {
			RepositoryManager.delete(RepositoryManager.getByID(id));
		}
		mv.addObject("next", nextRun());
		mv.addObject("harvest", HarvestJob.isRunning());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}


	@RequestMapping("/admin_repositories_activate.go")
	public ModelAndView activateRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
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
		mv.addObject("harvest", HarvestJob.isRunning());
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}

	@RequestMapping("/admin_repositories_deactivate.go")
	public ModelAndView deactivateRepository (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
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
		mv.addObject("harvest", HarvestJob.isRunning());
		mv.addObject("next", nextRun());
		mv.addObject("repositories", RepositoryManager.getRepositories());
		
		return mv;
	}

	@RequestMapping("/admin_repositories_add_final.go")
	public ModelAndView addRepositoryFinal (HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("repositories/index");
		
		
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

		mv.addObject("harvest", HarvestJob.isRunning());

		try {
			RepositoryManager.create(r);
			mv.addObject("error", false);
			mv.addObject("next", nextRun());
			mv.addObject("repositories", RepositoryManager.getRepositories());
		} catch (MarshallingException e) {
			mv.setViewName("repositories/add");
			mv.addObject("error", true);
			mv.addObject("message", "Error while saving repository "+name+". Please try again, if the problem persists contact Lyncode.");
			log.debug(e.getMessage(), e);
		} catch (IOException e) {
			mv.setViewName("repositories/add");
			mv.addObject("error", true);
			mv.addObject("message", "Error while saving repository "+name+". Please try again, if the problem persists contact Lyncode.");
			log.debug(e.getMessage(), e);
		}
		
		return mv;
	}
}
