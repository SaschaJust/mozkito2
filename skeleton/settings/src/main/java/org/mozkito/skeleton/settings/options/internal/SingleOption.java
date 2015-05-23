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

package org.mozkito.skeleton.settings.options.internal;

/**
 * The Class SingleOption.
 *
 * @param <T>
 *            the generic type
 */
public abstract class SingleOption<T> extends Option<T> {
	
	/** The default value. */
	String  defaultValue;
	
	/** The eyes only. */
	boolean eyesOnly;
	
	/**
	 * Instantiates a new single option.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @param returnType
	 *            the return type
	 */
	protected SingleOption(final String name, final String description, final Class<T> returnType) {
		super(name, description, returnType);
	}
	
	/**
	 * Check.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	protected abstract boolean check(String value);
	
	/**
	 * Instantiate.
	 *
	 * @param value
	 *            the value
	 * @return the t
	 */
	protected abstract T instantiate(String value);
	
	/**
	 * Present.
	 *
	 * @return true, if successful
	 */
	protected final boolean present() {
		return this.properties.containsKey(this.name);
	}
}