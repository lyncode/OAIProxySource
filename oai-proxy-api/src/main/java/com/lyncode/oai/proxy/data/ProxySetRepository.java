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

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 * 
 */
public class ProxySetRepository extends AbstractSetRepository {

	/**
	 * Supports virtual sets
	 * 
	 * @see com.lyncode.xoai.dataprovider.data.AbstractSetRepository#supportSets()
	 */
	@Override
	public boolean supportSets() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractSetRepository#retrieveSets(int, int)
	 */
	@Override
	public ListSetsResult retrieveSets(int offset, int length) {
		return new ListSetsResult(false, new ArrayList<Set>());
	}

	/* (non-Javadoc)
	 * @see com.lyncode.xoai.dataprovider.data.AbstractSetRepository#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String setSpec) {
		return false;
	}

}
