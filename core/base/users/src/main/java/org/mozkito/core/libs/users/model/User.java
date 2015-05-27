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

import org.mozkito.libraries.sequel.ISequelEntity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class User.
 *
 * @author Sascha Just
 */
public class User implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long   serialVersionUID = 2009808652533819571L;
	
	/** The id. */
	private Integer             id               = -1;
	
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
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id()
	 */
	public Integer id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id(java.lang.Object)
	 */
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isInteger(id);
		
		this.id = (Integer) id;
	}
	
}
