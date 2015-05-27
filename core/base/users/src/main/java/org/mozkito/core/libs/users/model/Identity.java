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

import org.mozkito.libraries.sequel.ISequelEntity;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class User.
 *
 * @author Sascha Just
 */
public class Identity implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8580622659060890966L;
	
	/** The username. */
	private String            userName;
	
	/** The fullname. */
	private String            fullName;
	
	/** The email. */
	private String            email;
	
	/** The id. */
	private Integer           id               = -1;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param userName
	 *            the user name
	 * @param email
	 *            the email
	 * @param fullName
	 *            the full name
	 */
	public Identity(final String userName, final String email, final String fullName) {
		this.userName = userName;
		this.email = email;
		this.fullName = fullName;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Identity other = (Identity) obj;
		if (this.email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!this.email.equals(other.email)) {
			return false;
		}
		if (this.fullName == null) {
			if (other.fullName != null) {
				return false;
			}
		} else if (!this.fullName.equals(other.fullName)) {
			return false;
		}
		if (this.userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!this.userName.equals(other.userName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Gets the fullname.
	 *
	 * @return the fullname
	 */
	public String getFullName() {
		return this.fullName;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.email == null
		                                             ? 0
		                                             : this.email.hashCode());
		result = prime * result + (this.fullName == null
		                                                ? 0
		                                                : this.fullName.hashCode());
		result = prime * result + (this.userName == null
		                                                ? 0
		                                                : this.userName.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id()
	 */
	@Override
	public Integer id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id(java.lang.Object)
	 */
	@Override
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isInteger(id);
		
		this.id = (Integer) id;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email
	 *            the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}
	
	/**
	 * Sets the fullname.
	 *
	 * @param fullname
	 *            the fullname to set
	 */
	public void setFullname(final String fullname) {
		this.fullName = fullname;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username
	 *            the username to set
	 */
	public void setUserName(final String username) {
		this.userName = username;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Identity [userName=");
		builder.append(this.userName);
		builder.append(", fullName=");
		builder.append(this.fullName);
		builder.append(", email=");
		builder.append(this.email);
		builder.append("]");
		return builder.toString();
	}
	
}
