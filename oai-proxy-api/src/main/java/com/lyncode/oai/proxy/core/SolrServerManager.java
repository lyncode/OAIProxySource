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
package com.lyncode.oai.proxy.core;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.xml.sax.SAXException;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 * 
 */
public class SolrServerManager {
	private static Logger log = LogManager.getLogger(SolrServerManager.class);
	private static SolrServer server = null;
	
	public static void initialize () {
		if (server == null) {
			String config = ConfigurationManager.getConfiguration().getString("solr.home");
			System.setProperty("solr.solr.home", config);
			System.setProperty("solr.data.dir", config + File.separator + "data");
			CoreContainer.Initializer initializer = new CoreContainer.Initializer();
			CoreContainer coreContainer;
			try {
				coreContainer = initializer.initialize();
				server = new EmbeddedSolrServer(coreContainer, "");
				
				try {
					server.query(new SolrQuery("*:*"));
					log.info("Solr Server initialized");
				} catch (SolrServerException e) {
					log.error(e.getMessage(), e);
				}
			} catch (IOException e1) {
				log.error(e1.getMessage(), e1);
			} catch (ParserConfigurationException e1) {
				log.error(e1.getMessage(), e1);
			} catch (SAXException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
	}
	
	public static SolrServer getServer () {
		initialize();
		return server;
	}
}
