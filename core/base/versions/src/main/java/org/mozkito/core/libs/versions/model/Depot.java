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
