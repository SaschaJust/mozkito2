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

import org.mozkito.core.libs.versions.model.enums.IntegrationMarker;
import org.mozkito.core.libs.versions.model.enums.NavigationMarker;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class BranchEdge.
 */
public class BranchEdge implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6075643512649538152L;
	
	/** The id. */
	private long              id;
	
	/** The edge id. */
	private final long        edgeId;
	
	/** The branch id. */
	private final long        branchId;
	
	/** The navigation type. */
	private final short       navigationType;
	
	/** The integration type. */
	private final short       integrationType;
	
	/**
	 * Instantiates a new branch edge.
	 *
	 * @param edgeId
	 *            the edge id
	 * @param branchId
	 *            the branch id
	 * @param navigationMarker
	 *            the navigation marker
	 * @param integrationMarker
	 *            the integration marker
	 */
	public BranchEdge(final long edgeId, final long branchId, final NavigationMarker navigationMarker,
	        final IntegrationMarker integrationMarker) {
		super();
		Requires.notNull(navigationMarker);
		Requires.notNull(integrationMarker);
		
		this.edgeId = edgeId;
		this.branchId = branchId;
		this.navigationType = (short) navigationMarker.ordinal();
		this.integrationType = (short) integrationMarker.ordinal();
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
		final BranchEdge other = (BranchEdge) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.edgeId != other.edgeId) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the branchId
	 */
	public final long getBranchId() {
		return this.branchId;
	}
	
	/**
	 * @return the edgeId
	 */
	public final long getEdgeId() {
		return this.edgeId;
	}
	
	/**
	 * @return the integrationType
	 */
	public final short getIntegrationType() {
		return this.integrationType;
	}
	
	/**
	 * @return the navigationType
	 */
	public final short getNavigationType() {
		return this.navigationType;
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
		result = prime * result + (int) (this.branchId ^ this.branchId >>> 32);
		result = prime * result + (int) (this.edgeId ^ this.edgeId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
}
