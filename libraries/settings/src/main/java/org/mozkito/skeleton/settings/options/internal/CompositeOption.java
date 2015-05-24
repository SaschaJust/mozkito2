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

import java.util.HashMap;
import java.util.Map;

import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class CompositeOption.
 *
 * @param <T>
 *            the generic type
 */
public abstract class CompositeOption<T> extends Option<T> {
	
	public static enum Type {
		REQUIRED, OPTIONAL;
	}
	
	/** The options. */
	private final Map<String, Option<?>> options = new HashMap<>();
	
	/**
	 * Instantiates a new composite option.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @param returnType
	 *            the return type
	 */
	protected CompositeOption(final String name, final String description, final Class<T> returnType) {
		super(name, description, returnType);
	}
	
	/**
	 * Append.
	 *
	 * @param option
	 *            the option
	 * @return the composite option
	 */
	public final CompositeOption<T> append(final Option<?> option,
	                                       final Type type) {
		Requires.notNull(option);
		
		this.options.put(option.name, option);
		return this;
	}
	
	/**
	 * Gets the.
	 *
	 * @param childName
	 *            the child name
	 * @return the object
	 */
	public final Object get(final String childName) {
		Requires.notNull(childName);
		
		return this.options.get(childName).get();
	}
	
	/**
	 * Instantiate.
	 *
	 * @return the t
	 */
	public abstract T instantiate();
}
