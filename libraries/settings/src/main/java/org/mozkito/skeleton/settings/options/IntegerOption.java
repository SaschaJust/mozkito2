/***********************************************************************************************************************
 * Copyright 2015 mozkito.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 **********************************************************************************************************************/

package org.mozkito.skeleton.settings.options;

import org.mozkito.skeleton.settings.options.internal.SingleOption;

/**
 * The Class IntegerOption represents an integer within the settings tree.
 *
 * @author Sascha Just
 */
public class IntegerOption extends SingleOption<Integer> {
	
	/**
	 * Instantiates a new integer option.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @param returnType
	 *            the return type
	 */
	protected IntegerOption(final String name, final String description, final Class<Integer> returnType) {
		super(name, description, returnType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.settings.options.internal.SingleOption#check(java.lang.String)
	 */
	@Override
	protected boolean check(final String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.settings.options.internal.SingleOption#instantiate(java.lang.String)
	 */
	@Override
	protected Integer instantiate(final String value) {
		return Integer.parseInt(value);
	}
	
}
