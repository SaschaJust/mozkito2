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

import java.time.Instant;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class Tag.
 *
 * @author Sascha Just
 */
public class Tag implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4737963776452999836L;
	
	/** The id. */
	private long              id;
	
	/** The commit hash. */
	private final long        changesetId;
	
	/** The name. */
	private final String      name;
	
	/** The hash. */
	private final String      hash;
	
	/** The identity id. */
	private final Long        identityId;
	
	/** The timestamp. */
	private final Instant     timestamp;
	
	/** The message. */
	private final String      message;
	
	private final long        depotId;
	
	/**
	 * Instantiates a new tag.
	 * 
	 * @param depot
	 *
	 * @param changeSet
	 *            the change set
	 * @param name
	 *            the name
	 */
	public Tag(final Depot depot, final ChangeSet changeSet, final String name) {
		this(depot, changeSet, name, null, null, null, null);
	}
	
	/**
	 * Instantiates a new tag.
	 * 
	 * @param depot
	 *
	 * @param changeSet
	 *            the change set
	 * @param name
	 *            the name
	 * @param hash
	 *            the hash
	 * @param message
	 *            the message
	 * @param identity
	 *            the identity
	 * @param timestamp
	 *            the timestamp
	 */
	public Tag(final Depot depot, final ChangeSet changeSet, final String name, final String hash,
	        final String message, final Identity identity, final Instant timestamp) {
		super();
		this.depotId = depot.id();
		if (changeSet == null) {
			Logger.error("name: " + name);
			Logger.error("name: " + hash);
		}
		this.changesetId = changeSet.id();
		this.name = name;
		this.hash = hash;
		this.message = message;
		this.identityId = identity != null
		                                  ? identity.id()
		                                  : null;
		this.timestamp = timestamp;
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
		final Tag other = (Tag) obj;
		if (this.depotId != other.depotId) {
			return false;
		}
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
	 * Gets the changeset id.
	 *
	 * @return the changesetId
	 */
	public final long getChangesetId() {
		return this.changesetId;
	}
	
	/**
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final long getDepotId() {
		return this.depotId;
	}
	
	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public final String getHash() {
		return this.hash;
	}
	
	/**
	 * Gets the identity id.
	 *
	 * @return the identityId
	 */
	public final long getIdentityId() {
		return this.identityId;
	}
	
	/**
	 * @return the message
	 */
	public final String getMessage() {
		return this.message;
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
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public final Instant getTimestamp() {
		return this.timestamp;
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
		result = prime * result + (int) (this.depotId ^ this.depotId >>> 32);
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(long)
	 */
	public void id(final long id) {
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tag [changesetId=");
		builder.append(this.changesetId);
		builder.append(", ");
		builder.append("name=");
		builder.append(this.name);
		builder.append(", ");
		if (this.hash != null) {
			builder.append("hash=");
			builder.append(this.hash);
			builder.append(", ");
		}
		if (this.identityId != null) {
			builder.append("identityId=");
			builder.append(this.identityId);
			builder.append(", ");
		}
		if (this.timestamp != null) {
			builder.append("timestamp=");
			builder.append(this.timestamp);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
