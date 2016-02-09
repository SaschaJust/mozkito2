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
