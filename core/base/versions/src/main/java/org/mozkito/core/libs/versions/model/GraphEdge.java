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

import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class GraphEdge.
 *
 * @author Sascha Just
 */
public class GraphEdge implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8396062311825539087L;
	
	/** The id. */
	private long              id;
	
	/** The depot_id. */
	private final long        depotId;
	
	/** The source_id. */
	private final long        sourceId;
	
	/** The target_id. */
	private final long        targetId;
	
	/** The marker. */
	private final short       marker;
	
	/**
	 * Instantiates a new graph edge.
	 *
	 * @param depot_id
	 *            the depot_id
	 * @param source_id
	 *            the source_id
	 * @param target_id
	 *            the target_id
	 * @param branchMarker
	 *            the branch marker
	 */
	public GraphEdge(final long depot_id, final long source_id, final long target_id, final short branchMarker) {
		super();
		this.depotId = depot_id;
		this.sourceId = source_id;
		this.targetId = target_id;
		this.marker = branchMarker;
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
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Gets the marker.
	 *
	 * @return the marker
	 */
	public final short getMarker() {
		return this.marker;
	}
	
	/**
	 * Gets the source id.
	 *
	 * @return the sourceId
	 */
	public final long getSourceId() {
		return this.sourceId;
	}
	
	/**
	 * Gets the target id.
	 *
	 * @return the targetId
	 */
	public final long getTargetId() {
		return this.targetId;
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
