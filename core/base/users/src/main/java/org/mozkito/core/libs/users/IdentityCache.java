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

package org.mozkito.core.libs.users;

import java.util.HashMap;
import java.util.Map;

import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class IdentityCache.
 *
 * @author Sascha Just
 */
public class IdentityCache {
	
	/** The Constant UNKNOWN_IDENTITY. */
	private static final String           UNKNOWN_IDENTITY = "UNKNOWN";
	
	/** The identities. */
	private final Map<Identity, Identity> identities       = new HashMap<>();
	
	/**
	 * Instantiates a new identity cache.
	 */
	public IdentityCache() {
		request(UNKNOWN_IDENTITY, null, null);
	}
	
	/**
	 * Exists.
	 *
	 * @param username
	 *            the username
	 * @param fullname
	 *            the fullname
	 * @param email
	 *            the email
	 * @return true, if successful
	 */
	public boolean exists(final String username,
	                      final String fullname,
	                      final String email) {
		return this.identities.containsKey(new Identity(username, email, fullname));
	}
	
	/**
	 * Gets the unknown.
	 *
	 * @return the unknown
	 */
	public Identity getUnknown() {
		return request(UNKNOWN_IDENTITY, null, null);
	}
	
	/**
	 * Gets the.
	 *
	 * @param username
	 *            the username
	 * @param fullname
	 *            the fullname
	 * @param email
	 *            the email
	 * @return the identity
	 */
	public Identity request(final String username,
	                        final String fullname,
	                        final String email) {
		final Identity identity = new Identity(username, fullname, email);
		
		Asserts.notNull(this.identities);
		
		if (!this.identities.containsKey(identity)) {
			this.identities.put(identity, identity);
		}
		
		return this.identities.get(identity);
	}
}
