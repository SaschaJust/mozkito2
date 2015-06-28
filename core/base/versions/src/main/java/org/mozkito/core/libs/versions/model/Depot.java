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

import java.net.URI;
import java.time.Instant;

import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class Depot.
 *
 * @author Sascha Just
 */
public class Depot implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2547303402415479035L;
	
	/** The id. */
	private int               id;
	
	/** The name. */
	private String            name;
	
	/** The origin. */
	private URI               origin;
	
	private final Instant     mined;
	
	/**
	 * Instantiates a new depot.
	 *
	 * @param name
	 *            the name
	 * @param origin
	 *            the origin
	 * @param mined
	 *            the mined
	 */
	public Depot(final String name, final URI origin, final Instant mined) {
		super();
		this.name = name;
		this.origin = origin;
		this.mined = mined;
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
		final Depot other = (Depot) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the mined.
	 *
	 * @return the mined
	 */
	public Instant getMined() {
		return this.mined;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * @return the origin
	 */
	public final URI getOrigin() {
		return this.origin;
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
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	@Override
	public long getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = (int) id;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @param origin
	 *            the origin to set
	 */
	public final void setOrigin(final URI origin) {
		this.origin = origin;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Depot [id=");
		builder.append(this.id);
		builder.append(", ");
		if (this.name != null) {
			builder.append("name=");
			builder.append(this.name);
			builder.append(", ");
		}
		if (this.origin != null) {
			builder.append("origin=");
			builder.append(this.origin);
			builder.append(", ");
		}
		if (this.mined != null) {
			builder.append("mined=");
			builder.append(this.mined);
		}
		builder.append("]");
		return builder.toString();
	}
}
