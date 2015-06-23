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
