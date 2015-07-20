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

package org.mozkito.core.libs.versions;

import java.util.HashMap;
import java.util.Map;

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
	private final Map<Identity, Identity>  identities       = new HashMap<>();
	
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
		
		if (!this.identities.containsKey(identity)) {
			synchronized (this.identities) {
				if (!this.identities.containsKey(identity)) {
					this.identities.put(identity, identity);
					this.dumper.saveLater(identity);
				}
			}
		}
		
		identity = this.identities.get(identity);
		Asserts.notNull(identity);
		return identity;
	}
	
}
