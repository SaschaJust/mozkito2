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
 
package org.mozkito.core.libs.versions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The IdentityCache is a cache that maps <code>email</code> and <code>fullname</code> of a user in a version control
 * system to its {@link Identity} entity. The purpose of the class is to reuse identities throughout {@link Depot}s.
 *
 * @author Sascha Just
 */
public class IdentityCache {
	
	/** The Constant UNKNOWN_IDENTITY. */
	private static final String            UNKNOWN_IDENTITY = "<UNKNOWN>";
	
	/** The identities. */
	private final ConcurrentMap<Identity, Identity>  identities       = new ConcurrentHashMap<>();
	
	/** The dumper. */
	private final IDumper<Identity> dumper;
	
	/**
	 * Instantiates a new identity cache.
	 *
	 * @param dumper
	 *            the dumper
	 */
	public IdentityCache(final IDumper<Identity> dumper) {
		Requires.notNull(dumper);
		this.dumper = dumper;
		
		getUnknown();
	}
	
	/**
	 * Gets the default identity which is used when both <code>email</code> and <code>fullname</code> are
	 * <code>null</code>.
	 *
	 * @return the <code>UNKNOWN_IDENTITY</code>
	 */
	public Identity getUnknown() {
		return request(UNKNOWN_IDENTITY, UNKNOWN_IDENTITY);
	}
	
	/**
	 * New identity helper function to work around broken DCL.
	 *
	 * @param identity the identity
	 * @return the identity
	 */
	private final Identity newIdentity(Identity identity) {
		dumper.saveLater(identity);
		return identity;
	}
	
	/**
	 * Looks up and returns the identity represented by <code>email</code> and <code>fullname</code> in the local cash.
	 * If both arguments are <code>null</code>, the default <code>UNKNOWN_IDENTIY</code> is returned. If the identity is
	 * not known to the cash, a new identity will be created and stored in the cache.
	 *
	 * @param email
	 *            the email
	 * @param fullname
	 *            the fullname
	 * @return the identity
	 */
	public Identity request(final String email,
	                        final String fullname) {
		if (email == null && fullname == null) {
			return getUnknown();
		}
		
		Identity identity = new Identity(email, fullname);
		
		Asserts.notNull(this.identities);
		
		identity = this.identities.computeIfAbsent(identity, k -> newIdentity(k));
		
		Asserts.notNull(identity);
		return identity;
	}
	
}
