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

package org.mozkito.core.libs.users.model;

import java.util.HashSet;
import java.util.Set;

import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.IEntity;

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
	 * @see org.mozkito.skeleton.sequel.IEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.IEntity#id(long)
	 */
	public void id(final long id) {
		this.id = id;
	}
	
}
