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
