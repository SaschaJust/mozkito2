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

import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class IdentityCache.
 *
 * @author Sascha Just
 */
public class IdentityCache {
	
	/** The Constant UNKNOWN_IDENTITY. */
	private static final String           UNKNOWN_IDENTITY = "<UNKNOWN>";
	
	/** The identities. */
	private final Map<Identity, Identity> identities       = new HashMap<>();
	
	/**
	 * Instantiates a new identity cache.
	 */
	public IdentityCache() {
		getUnknown();
	}
	
	/**
	 * Gets the unknown.
	 *
	 * @return the unknown
	 */
	public Identity getUnknown() {
		return request(UNKNOWN_IDENTITY, UNKNOWN_IDENTITY);
	}
	
	/**
	 * Gets the.
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
				}
			}
		}
		
		identity = this.identities.get(identity);
		Asserts.notNull(identity);
		return identity;
	}
	
}
