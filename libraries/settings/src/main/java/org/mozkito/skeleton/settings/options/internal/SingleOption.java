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