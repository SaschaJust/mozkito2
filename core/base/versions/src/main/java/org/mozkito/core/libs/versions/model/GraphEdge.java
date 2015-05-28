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
 * The Class GraphEdge.
 *
 * @author Sascha Just
 */
public class GraphEdge implements ISequelEntity {
	
	private static final long serialVersionUID = -8396062311825539087L;
	
	/** The id. */
	public long               id;
	
	/** The depot_id. */
	public final long         depotId;
	
	/** The source_id. */
	public final long         sourceId;
	
	/** The target_id. */
	public final long         targetId;
	
	/** The type. */
	public final short        type;
	
	/**
	 * Instantiates a new graph edge.
	 *
	 * @param depot_id
	 *            the depot_id
	 * @param source_id
	 *            the source_id
	 * @param target_id
	 *            the target_id
	 * @param type
	 *            the type
	 */
	public GraphEdge(final long depot_id, final long source_id, final long target_id, final short type) {
		super();
		this.depotId = depot_id;
		this.sourceId = source_id;
		this.targetId = target_id;
		this.type = type;
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
