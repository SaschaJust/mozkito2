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

import org.mozkito.core.libs.versions.model.enums.ReferenceType;
import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class Tag.
 *
 * @author Sascha Just
 */
public class Tag extends Reference implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4737963776452999836L;
	
	/** The hash. */
	private final String      hash;
	
	/** The identity id. */
	private final Long        identityId;
	
	/** The timestamp. */
	private final Instant     timestamp;
	
	/** The message. */
	private final String      message;
	
	/**
	 * Instantiates a new tag.
	 *
	 * @param depot
	 *            the depot
	 * @param changeSetId
	 *            the change set id
	 * @param name
	 *            the name
	 */
	public Tag(final Depot depot, final long changeSetId, final String name) {
		this(depot, changeSetId, name, null, null, null, null);
	}
	
	/**
	 * Instantiates a new tag.
	 *
	 * @param depot
	 *            the depot
	 * @param changeSetId
	 *            the change set id
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
	public Tag(final Depot depot, final long changeSetId, final String name, final String hash, final String message,
	        final Identity identity, final Instant timestamp) {
		super(depot, ReferenceType.TAG, name, changeSetId);
		this.hash = hash;
		this.message = message;
		this.identityId = identity != null
		                                  ? identity.getId()
		                                  : null;
		this.timestamp = timestamp;
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
	public final Long getIdentityId() {
		return this.identityId;
	}
	
	/**
	 * @return the message
	 */
	public final String getMessage() {
		return this.message;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tag [id()=");
		builder.append(getId());
		builder.append(", depot_id=");
		builder.append(getDepotId());
		builder.append(", type=");
		builder.append(getType());
		builder.append(", name=");
		builder.append(getName());
		builder.append(", head_id=");
		builder.append(getHeadId());
		builder.append(", hash=");
		builder.append(this.hash);
		builder.append(", identity_id=");
		builder.append(this.identityId);
		builder.append(", timestamp=");
		builder.append(this.timestamp);
		builder.append(", message=");
		builder.append(this.message);
		builder.append("]");
		return builder.toString();
	}
	
}
