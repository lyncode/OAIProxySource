/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 */
package com.lyncode.oai.proxy;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.oai.proxy.data.ProxyIdentify;
import com.lyncode.oai.proxy.data.ProxyItemRepository;
import com.lyncode.oai.proxy.data.ProxySetRepository;
import com.lyncode.xoai.dataprovider.OAIDataProvider;
import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 */
public class ProxyDataProvider extends HttpServlet {
	private static final long serialVersionUID = -5867723882301285668L;
	private static Logger log = LogManager
            .getLogger(ProxyDataProvider.class);
	
	
	
	@Override
	public void init() throws ServletException {
		log.info("OAI Proxy Initialized");
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ProxyIdentify identify = new ProxyIdentify(req);
		ProxySetRepository listsets = new ProxySetRepository();
		ProxyItemRepository itemRepository = new ProxyItemRepository();
		
		try {
			XOAIManager.initialize("config" + File.separator + "xoai");
			log.debug("Creating XOAI Data Provider Instance");
			
			log.debug("Requested context: "+ req.getPathInfo().replace("/", ""));
			
			OAIDataProvider dataprovider = new OAIDataProvider(
					req.getPathInfo().replace("/", ""), 
                    identify, 
                    listsets, 
                    itemRepository);
			log.debug("Reading parameters from request");
			
            OutputStream out = resp.getOutputStream();
            OAIRequestParameters parameters = new OAIRequestParameters();
            parameters.setFrom(req.getParameter("from"));
            parameters.setUntil(req.getParameter("until"));
            parameters.setSet(req.getParameter("set"));
            parameters.setVerb(req.getParameter("verb"));
            parameters
                    .setMetadataPrefix(req.getParameter("metadataPrefix"));
            parameters.setIdentifier(req.getParameter("identifier"));
            parameters.setResumptionToken(req
                    .getParameter("resumptionToken"));

            resp.setContentType("application/xml");
            
            dataprovider.handle(parameters, out);

            out.flush();
            out.close();
		} catch (InvalidContextException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Requested OAI context \""
                    + req.getPathInfo().replace("/", "")
                    + "\" does not exist");
		} catch (OAIException e) {
			log.error(e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred, please, contact the develop team development@lyncode.com");
		} catch (com.lyncode.xoai.dataprovider.exceptions.ConfigurationException e) {
			log.error(e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred, please, contact the develop team development@lyncode.com");
		}
	}
	
}
