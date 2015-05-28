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

import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class IntegrationEdge.
 *
 * @author Sascha Just
 */
public class IntegrationEdge implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7225504055028243042L;
	
	/** The id. */
	public long               id;
	
	/** The depot id. */
	public long               depotId;
	
	/** The edge id. */
	public long               edgeId;
	
	/** The branch id. */
	public long               branchId;
	
	/**
	 * Instantiates a new branch edge.
	 *
	 * @param depotId
	 *            the depot id
	 * @param edgeId
	 *            the edge id
	 * @param branchId
	 *            the branch id
	 */
	public IntegrationEdge(final long depotId, final long edgeId, final long branchId) {
		super();
		this.depotId = depotId;
		this.edgeId = edgeId;
		this.branchId = branchId;
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
	
}
