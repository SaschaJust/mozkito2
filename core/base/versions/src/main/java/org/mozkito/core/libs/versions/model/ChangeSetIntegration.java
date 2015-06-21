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

import org.mozkito.core.libs.versions.IntegrationType;
import org.mozkito.skeleton.sequel.IEntity;

/**
 * The Class ChangeSetIntegration.
 *
 * @author Sascha Just
 */
public class ChangeSetIntegration implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5384066977342816045L;
	
	/** The change set id. */
	private long              changeSetId;
	
	/** The integration type. */
	private final short       integrationType;
	
	/**
	 * Instantiates a new change set integration.
	 *
	 * @param changeSet
	 *            the change set
	 * @param type
	 *            the type
	 */
	public ChangeSetIntegration(final ChangeSet changeSet, final IntegrationType type) {
		this.changeSetId = changeSet.id();
		this.integrationType = type.getValue();
	}
	
	/**
	 * Instantiates a new change set integration.
	 *
	 * @param changeSetId
	 *            the change set id
	 * @param integrationType
	 *            the integration type
	 */
	public ChangeSetIntegration(final long changeSetId, final IntegrationType integrationType) {
		super();
		this.changeSetId = changeSetId;
		this.integrationType = integrationType.getValue();
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
		final ChangeSetIntegration other = (ChangeSetIntegration) obj;
		if (this.changeSetId != other.changeSetId) {
			return false;
		}
		if (this.integrationType != other.integrationType) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the integrationType
	 */
	public final short getIntegrationType() {
		return this.integrationType;
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
		result = prime * result + (int) (this.changeSetId ^ this.changeSetId >>> 32);
		result = prime * result + this.integrationType;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IEntity#id()
	 */
	public long id() {
		return this.changeSetId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IEntity#id(long)
	 */
	public void id(final long id) {
		this.changeSetId = id;
	}
	
}
