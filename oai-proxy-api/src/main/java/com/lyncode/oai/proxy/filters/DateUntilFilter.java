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
package com.lyncode.oai.proxy.filters;

import java.util.Date;

import org.apache.solr.client.solrj.util.ClientUtils;

import com.lyncode.oai.proxy.data.ProxyItem;
import com.lyncode.oai.proxy.util.DateUtils;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 * 
 */
public class DateUntilFilter extends ProxyFilter {
	private Date until;
	
	public DateUntilFilter (Date until) {
		this.until = until;
	}
	/* (non-Javadoc)
	 * @see com.lyncode.oai.proxy.filters.ProxyFilter#query()
	 */
	@Override
	public String query() {
		return "item.lastmodified:[* TO "
                + ClientUtils.escapeQueryChars(DateUtils.formatToSolr(until))
                + "]";
	}
	@Override
	public boolean isShown(ProxyItem item) {
		if (item.getDatestamp().compareTo(until) <= 0)
            return true;
        return false;
	}
	
	

}
