/**
 * 
 */
package com.lyncode.oai.proxy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.oai.proxy.core.RepositoryManager;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;

/**
 * @author Lyncode
 *
 */
public class AddRepositoryServlet extends HttpServlet {
	private static Logger log = LogManager.getLogger(AddRepositoryServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String name = req.getParameter("name");
		String url = req.getParameter("url");
		boolean active = req.getParameter("active") != null;
		String set = req.getParameter("set");
		if (set.trim().equals("Set (Blank if none)")) set = null;
		
		Repository r = new Repository();
		r.setActive(active);
		r.setLastHarvest("None");
		r.setName(name);
		r.setURL(url);
		r.setSet(set);
		
		try {
			RepositoryManager.create(r);
	        req.getRequestDispatcher("/admin_repositories.jsp").forward(req, resp);
		} catch (MarshallingException e) {
			log.debug(e.getMessage(), e);
		}
	}

}
