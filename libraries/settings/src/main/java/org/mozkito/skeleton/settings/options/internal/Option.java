/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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