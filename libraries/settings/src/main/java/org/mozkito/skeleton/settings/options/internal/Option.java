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

import java.util.Properties;

/**
 * The Class Option.
 *
 * @param <T>
 *            the generic type
 */
public abstract class Option<T> {
	
	/** The properties. */
	Properties properties;
	
	/** The instance. */
	T          instance = null;
	
	/** The name. */
	String     name;
	
	/** The description. */
	String     description;
	
	/** The return type. */
	Class<T>   returnType;
	
	/**
	 * Instantiates a new option.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @param returnType
	 *            the return type
	 */
	protected Option(final String name, final String description, final Class<T> returnType) {
		this.name = name;
		this.description = description;
		this.returnType = returnType;
	}
	
	/**
	 * Gets the.
	 *
	 * @return the t
	 */
	public final T get() {
		if (this.instance == null) {
			if (this instanceof SingleOption) {
				this.instance = ((SingleOption<T>) this).instantiate(this.properties.getProperty(this.name));
			} else if (this instanceof CompositeOption) {
				this.instance = ((CompositeOption<T>) this).instantiate();
			} else {
				throw new RuntimeException();
			}
		}
		
		return this.instance;
	}
	
	/**
	 * Inject.
	 *
	 * @param properties
	 *            the properties
	 */
	final void inject(final Properties properties) {
		this.properties = properties;
	}
}