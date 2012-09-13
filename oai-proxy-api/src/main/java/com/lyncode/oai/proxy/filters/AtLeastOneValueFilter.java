/**
 * 
 */
package com.lyncode.oai.proxy.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;

import com.lyncode.oai.proxy.data.ProxyItem;
import com.lyncode.oai.proxy.filters.data.MetadataFieldOperator;

/**
 * @author jmelo
 * 
 */
public class AtLeastOneValueFilter extends ProxyFilter {
	private static Logger log = LogManager.getLogger(AtLeastOneValueFilter.class);
	
	private String _field;
	private MetadataFieldOperator _operator = MetadataFieldOperator.UNDEF;
	private List<String> _values;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lyncode.oai.proxy.filters.ProxyFilter#query()
	 */
	@Override
	public String query() {
		String field = this.getField();
		List<String> parts = new ArrayList<String>();
		if (this.getField() != null) {
			for (String v : this.getValues())
				this.buildQuery(field.replace(".", "_"),
						ClientUtils.escapeQueryChars(v), parts);
			if (parts.size() > 0) {
				return StringUtils.join(parts.iterator(), " OR ");
			}
		}
		return "";
	}

	private void buildQuery(String field, String value, List<String> parts) {
		switch (this.getOperator()) {
		case CONTAINS:
			parts.add("(" + field + ":*" + value + "*)");
			break;
		case ENDS_WITH:
			parts.add("(" + field + ":*" + value + ")");
			break;
		case STARTS_WITH:
			parts.add("(" + field + ":" + value + "*)");
			break;
		case EQUAL:
			parts.add("(" + field + ":" + value + ")");
			break;
		case GREATER:
			parts.add("(" + field + ":[" + value + " TO *])");
			break;
		case LOWER:
			parts.add("(" + field + ":[* TO " + value + "])");
			break;
		case LOWER_OR_EQUAL:
			parts.add("(-(" + field + ":[" + value + " TO *]))");
			break;
		case GREATER_OR_EQUAL:
			parts.add("(-(" + field + ":[* TO " + value + "]))");
			break;
		}
	}

	private MetadataFieldOperator getOperator() {
		if (_operator == MetadataFieldOperator.UNDEF) {
			_operator = MetadataFieldOperator.valueOf(super.getParameter(
					"operator").toUpperCase());
		}
		return _operator;
	}

	private String getField() {
		if (_field == null) {
			_field = super.getParameter("field");
		}
		return _field;
	}

	private List<String> getValues() {
		if (_values == null) {
			_values = super.getParameters("value");
		}
		return _values;
	}

	@Override
	public boolean isShown(ProxyItem item) {
		
		log.debug("Check if item: "+item.getIdentifier()+" is shown or not");
		
		if (this.getField() == null)
			return true;

		for (String praticalValue : item.getMetadataValues(this.getField().replace(".", "_"))) {
			log.debug("Checking if value '"+praticalValue+"' "+this.getOperator().name()+":");
			for (String theoreticValue : this.getValues()) {
				log.debug(theoreticValue);
				switch (this.getOperator()) {
				case CONTAINS:
					if (praticalValue.contains(theoreticValue))
						return true;
					break;
				case ENDS_WITH:
					if (praticalValue.endsWith(theoreticValue))
						return true;
					break;
				case EQUAL:
					if (praticalValue.equals(theoreticValue))
						return true;
					break;
				case GREATER:
					if (praticalValue.compareTo(theoreticValue) > 0)
						return true;
					break;
				case GREATER_OR_EQUAL:
					if (praticalValue.compareTo(theoreticValue) >= 0)
						return true;
					break;
				case LOWER:
					if (praticalValue.compareTo(theoreticValue) < 0)
						return true;
					break;
				case LOWER_OR_EQUAL:
					if (praticalValue.compareTo(theoreticValue) <= 0)
						return true;
					break;
				}
			}
		}
		
		log.debug("Item will not be shown");
		
		return false;
	}
}
