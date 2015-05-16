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

package org.mozkito.skeleton.sequel.model;

import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class User.
 *
 * @author Sascha Just
 */
public class User implements ISequelEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8580622659060890966L;

	/** The username. */
	private String            userName;

	/** The fullname. */
	private String            fullName;

	/** The email. */
	private String            email;

	/** The id. */
	private final Object[]    id               = new Object[] { -1 };

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
	public User(final String userName, final String email, final String fullName) {
		this.userName = userName;
		this.email = email;
		this.fullName = fullName;
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
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	@Override
	public Object[] id() {
		return this.id;
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
		builder.append("User [userName=");
		builder.append(this.userName);
		builder.append(", fullName=");
		builder.append(this.fullName);
		builder.append(", email=");
		builder.append(this.email);
		builder.append("]");
		return builder.toString();
	}

}
