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
 
package org.mozkito.core.libs.versions.model;

import org.mozkito.core.libs.versions.IdentityCache;
import org.mozkito.libraries.sequel.IBuilder;

/**
 * The Class IdentityBuilder.
 * 
 * Used to create {@link Identity} instances during parsing. These should be cached in an {@link IdentityCache}.
 *
 * @author Sascha Just
 */
public class IdentityBuilder implements IBuilder<Identity> {
	
	/** The email. */
	private String email;
	
	/** The fullname. */
	private String fullname;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IBuilder#create()
	 */
	public Identity create() {
		return new Identity(this.email, this.fullname);
	}
	
	/**
	 * Email.
	 *
	 * @param email
	 *            the email
	 * @return the identity builder
	 */
	public IdentityBuilder email(final String email) {
		this.email = email;
		return this;
	}
	
	/**
	 * Fullname.
	 *
	 * @param fullname
	 *            the fullname
	 * @return the identity builder
	 */
	public IdentityBuilder fullname(final String fullname) {
		this.fullname = fullname;
		return this;
	}
	
}
