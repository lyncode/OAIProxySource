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
package com.lyncode.oai.proxy.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import com.lyncode.oai.proxy.core.ConfigurationManager;
import com.lyncode.oai.proxy.core.SolrServerManager;
import com.lyncode.xoai.dataprovider.core.DeleteMethod;
import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 */
public class ProxyIdentify extends AbstractIdentify {
	private static Logger log = LogManager.getLogger(ProxyIdentify.class);

	
	 private HttpServletRequest _request;

	    public ProxyIdentify(HttpServletRequest request)
	    {
	        _request = request;
	    }
	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getRepositoryName()
	 */
	@Override
	public String getRepositoryName() {
		return ConfigurationManager.getConfiguration().getString("proxy.name");
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getAdminEmails()
	 */
	@Override
	public List<String> getAdminEmails() {
		List<String> list = new ArrayList<String>();
		list.add(ConfigurationManager.getConfiguration().getString("proxy.admin.mail"));
		return list;
	}

	private String _baseUrl;
	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getBaseUrl()
	 */
	@Override
	public String getBaseUrl() {
        if (_baseUrl == null)
        {
            _baseUrl = _request.getRequestURL().toString()
                    .replace(_request.getPathInfo(), "");
        }
        return _baseUrl + _request.getPathInfo();
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getEarliestDate()
	 */
	@Override
	public Date getEarliestDate() {
		SolrServer server = SolrServerManager.getServer();
		SolrQuery query = new SolrQuery("*:*").addField(ProxyItem.DATE_FIELD).addSortField(ProxyItem.DATE_FIELD, ORDER.asc);
		try {
			SolrDocumentList results = server.query(query).getResults();
			if (results.getNumFound() > 0) {
				return (Date) results.get(0).get(ProxyItem.DATE_FIELD);
			}
		} catch (SolrServerException e) {
			log.debug(e.getMessage(), e);
		}
		return new Date();
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getDeleteMethod()
	 */
	@Override
	public DeleteMethod getDeleteMethod() {
		return DeleteMethod.PERSISTENT;
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractIdentify#getGranularity()
	 */
	@Override
	public Granularity getGranularity() {
		return Granularity.Second;
	}

}
