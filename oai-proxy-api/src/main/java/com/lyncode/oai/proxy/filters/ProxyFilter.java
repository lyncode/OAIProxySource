package com.lyncode.oai.proxy.filters;

import com.lyncode.oai.proxy.data.ProxyItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.AbstractFilter;

public abstract class ProxyFilter extends AbstractFilter {
	public abstract String query ();
	
	public abstract boolean isShown (ProxyItem item);

	@Override
	public boolean isItemShown(AbstractItemIdentifier item) {
		if (item instanceof ProxyItem) return this.isShown((ProxyItem) item);
		return false;
	}
	
	
}
