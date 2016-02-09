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
