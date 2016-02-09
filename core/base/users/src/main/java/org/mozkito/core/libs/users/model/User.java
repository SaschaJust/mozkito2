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
 
package org.mozkito.core.libs.users.model;

import java.util.HashSet;
import java.util.Set;

import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class User.
 *
 * @author Sascha Just
 */
public class User implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long   serialVersionUID = 2009808652533819571L;
	
	/** The id. */
	private long                id               = -1;
	
	/** The identities. */
	private final Set<Identity> identities       = new HashSet<Identity>();
	
	/**
	 * Adds the identity.
	 *
	 * @param identity
	 *            the identity
	 */
	public void addIdentity(final Identity identity) {
		Requires.notNull(identity);
		Asserts.notNull(this.identities);
		
		this.identities.add(identity);
	}
	
	/**
	 * Gets the identities.
	 *
	 * @return the identities
	 */
	public final Set<Identity> getIdentities() {
		return this.identities;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
}
