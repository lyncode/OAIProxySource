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

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.lyncode.oai.proxy.core.SolrServerManager;
import com.lyncode.oai.proxy.filters.DateFromFilter;
import com.lyncode.oai.proxy.filters.DateUntilFilter;
import com.lyncode.oai.proxy.filters.ProxyFilter;
import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.filter.FilterScope;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 * 
 */
public class ProxyItemRepository extends AbstractItemRepository {

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItem(java.lang.String)
	 */
	@Override
	public AbstractItem getItem(String identifier)
			throws IdDoesNotExistException {
		try {
			SolrQuery query = new SolrQuery(ProxyItem.IDENTIFIER_FIELD+":"+ClientUtils.escapeQueryChars(identifier));
			SolrDocumentList list = SolrServerManager.getServer().query(query).getResults();
			if (list.getNumFound() > 0) {
				return new ProxyItem(list.get(0));
			} else throw new IdDoesNotExistException();
		} catch (SolrServerException e) {
			throw new IdDoesNotExistException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length) {
		try {
			List<String> whereCond = new ArrayList<String>();
	        for (Filter filter : filters)
	        {
	            if (filter.getFilter() instanceof ProxyFilter)
	            {
	            	ProxyFilter proxyFilter = (ProxyFilter) filter.getFilter();
	            	whereCond.add("(" + proxyFilter.query() + ")");
	            }
	        }
	        if (whereCond.isEmpty()) whereCond.add("*:*");
	        SolrQuery query = new SolrQuery("(" + StringUtils.join(whereCond.iterator(), ") AND (") + ")");
	        query.setStart(offset);
	        query.setRows(length);
			SolrDocumentList list = SolrServerManager.getServer().query(query).getResults();
			List<AbstractItemIdentifier> results = new ArrayList<AbstractItemIdentifier>();
			for (SolrDocument doc : list)
				results.add(new ProxyItem(doc));
			return new ListItemIdentifiersResult(list.getNumFound() > offset + length, results, (int) list.getNumFound());
		} catch (SolrServerException e) {
			return new ListItemIdentifiersResult(false, new ArrayList<AbstractItemIdentifier>());
		}
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length, Date from) {
		filters.add(new Filter(new DateFromFilter(from), FilterScope.Query));
		return getItemIdentifiers(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiersUntil(java.util.List, int, int, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, Date until) {
		filters.add(new Filter(new DateUntilFilter(until), FilterScope.Query));
		return getItemIdentifiers(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int, java.util.Date, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length, Date from, Date until) {
		filters.add(new Filter(new DateFromFilter(from), FilterScope.Query));
		filters.add(new Filter(new DateUntilFilter(until), FilterScope.Query));
		return getItemIdentifiers(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int, java.lang.String)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length, String setSpec) {
		// NO SETS!
		return new ListItemIdentifiersResult(false, new ArrayList<AbstractItemIdentifier>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int, java.lang.String, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length, String setSpec, Date from) {
		// NO SETS!
		return new ListItemIdentifiersResult(false, new ArrayList<AbstractItemIdentifier>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiersUntil(java.util.List, int, int, java.lang.String, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, String setSpec,
			Date until) {
		// NO SETS!
		return new ListItemIdentifiersResult(false, new ArrayList<AbstractItemIdentifier>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemIdentifiers(java.util.List, int, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public ListItemIdentifiersResult getItemIdentifiers(List<Filter> filters,
			int offset, int length, String setSpec, Date from, Date until) {
		// NO SETS!
		return new ListItemIdentifiersResult(false, new ArrayList<AbstractItemIdentifier>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length) {
		try {
			List<String> whereCond = new ArrayList<String>();
	        for (Filter filter : filters)
	        {
	            if (filter.getFilter() instanceof ProxyFilter)
	            {
	            	ProxyFilter proxyFilter = (ProxyFilter) filter.getFilter();
	            	whereCond.add("(" + proxyFilter.query() + ")");
	            }
	        }
	        if (whereCond.isEmpty()) whereCond.add("*:*");
	        SolrQuery query = new SolrQuery("(" + StringUtils.join(whereCond.iterator(), ") AND (") + ")");
	        query.setStart(offset);
	        query.setRows(length);
			SolrDocumentList list = SolrServerManager.getServer().query(query).getResults();
			List<AbstractItem> results = new ArrayList<AbstractItem>();
			for (SolrDocument doc : list)
				results.add(new ProxyItem(doc));
			return new ListItemsResults(list.getNumFound() > offset + length, results, (int) list.getNumFound());
		} catch (SolrServerException e) {
			return new ListItemsResults(false, new ArrayList<AbstractItem>());
		}
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int, java.util.Date)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length, Date from) {
		filters.add(new Filter(new DateFromFilter(from), FilterScope.Query));
		return getItems(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemsUntil(java.util.List, int, int, java.util.Date)
	 */
	@Override
	public ListItemsResults getItemsUntil(List<Filter> filters, int offset,
			int length, Date until) {
		filters.add(new Filter(new DateUntilFilter(until), FilterScope.Query));
		return getItems(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int, java.util.Date, java.util.Date)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length, Date from, Date until) {
		filters.add(new Filter(new DateFromFilter(from), FilterScope.Query));
		filters.add(new Filter(new DateUntilFilter(until), FilterScope.Query));
		return getItems(filters, offset, length);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int, java.lang.String)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length, String setSpec) {
		return new ListItemsResults(false, new ArrayList<AbstractItem>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int, java.lang.String, java.util.Date)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length, String setSpec, Date from) {
		return new ListItemsResults(false, new ArrayList<AbstractItem>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItemsUntil(java.util.List, int, int, java.lang.String, java.util.Date)
	 */
	@Override
	public ListItemsResults getItemsUntil(List<Filter> filters, int offset,
			int length, String setSpec, Date until) {
		return new ListItemsResults(false, new ArrayList<AbstractItem>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemRepository#getItems(java.util.List, int, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public ListItemsResults getItems(List<Filter> filters, int offset,
			int length, String setSpec, Date from, Date until) {
		return new ListItemsResults(false, new ArrayList<AbstractItem>());
	}

}
