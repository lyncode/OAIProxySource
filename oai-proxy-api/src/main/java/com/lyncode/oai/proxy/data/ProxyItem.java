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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.xml.xoai.Element;
import com.lyncode.xoai.dataprovider.xml.xoai.Element.Field;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 * 
 */
public class ProxyItem extends AbstractItem {
	private static Logger log = LogManager.getLogger(ProxyItem.class);
	public static final String IDENTIFIER_FIELD = "id";
	public static final String DATE_FIELD = "last_modified";
	public static final String DELETED_FIELD = "deleted";
	public static final String REPOSITORY_ID = "repid";
	public static final String METADATA_START = "dc_";
	
	private SolrDocument document;
	
	public ProxyItem (SolrDocument document) {
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItem#getAbout()
	 */
	@Override
	public List<AbstractAbout> getAbout() {
		return new ArrayList<AbstractAbout>();
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItem#getMetadata()
	 */
	@Override
	public ItemMetadata getMetadata() {
		return new ItemMetadata(this.getMedatata());
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<String> getMetadataValues (String metadata) {
		List<String> res = new ArrayList<String>();
		for (String field : document.getFieldNames()) {
			log.debug("Field "+field+" = "+metadata);
			if (field.equals(metadata)) {
				List<String> values = (List<String>) document.get(field);
				for (String v : values) {
					res.add(v);
				}
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private Metadata getMedatata() {
		Metadata metadata = new Metadata();
		for (String field : document.getFieldNames()) {
			if (field.startsWith(METADATA_START)) {
				List<String> values = (List<String>) document.get(field);
				String name = field.replace(METADATA_START, "");
				for (String v : values) {
					Element e = new Element();
					Field f = new Field();
					f.setValue(v);
					e.getField().add(f);
					e.setName(name);
					metadata.getElement().add(e);
				}
			}
		}
		return metadata;
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return (String) document.get(IDENTIFIER_FIELD);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier#getDatestamp()
	 */
	@Override
	public Date getDatestamp() {
		return (Date) document.get(DATE_FIELD);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier#getSets()
	 */
	@Override
	public List<ReferenceSet> getSets() {
		return new ArrayList<ReferenceSet>();
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier#isDeleted()
	 */
	@Override
	public boolean isDeleted() {
		return (Boolean) document.get(DELETED_FIELD);
	}

}
