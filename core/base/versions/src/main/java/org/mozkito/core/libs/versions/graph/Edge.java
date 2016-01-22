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

package org.mozkito.core.libs.versions.graph;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.enums.BranchMarker;
import org.mozkito.core.libs.versions.model.enums.IntegrationMarker;
import org.mozkito.core.libs.versions.model.enums.NavigationMarker;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Edge.
 */
public class Edge {
	
	/**
     * 
     */
	private final Graph  graph;
	
	/** The labels. */
	Map<Long, Label>     labels;
	
	/** The child. */
	final Vertex         child;
	
	/** The parent. */
	final Vertex         parent;
	
	private BranchMarker branchMarker;
	
	private long         id;
	
	/**
	 * Instantiates a new edge.
	 *
	 * @param graph
	 *            the graph
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 * @param type
	 *            the type
	 */
	public Edge(final Graph graph, final Vertex parent, final Vertex child, final BranchMarker type) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.branchMarker = type;
		this.labels = new HashMap<Long, Label>();
	}
	
	/**
	 * Adds the integration.
	 *
	 * @param reference
	 *            the branch
	 * @param marker
	 *            the marker
	 */
	public void addIntegration(final Reference reference,
	                           final IntegrationMarker marker) {
		Requires.notNull(reference);
		if (!this.labels.containsKey(reference.getId())) {
			throw new IllegalArgumentException("This edge is not part of branch " + reference);
		}
		
		this.labels.get(reference.getId()).integrationMarker = marker;
	}
	
	/**
	 * Adds the navigation.
	 *
	 * @param reference
	 *            the branch
	 * @param marker
	 *            the marker
	 */
	public void addNavigation(final Reference reference,
	                          final NavigationMarker marker) {
		Requires.notNull(reference);
		if (!this.labels.containsKey(reference.getId())) {
			throw new IllegalArgumentException("This edge is not part of branch " + reference);
		}
		
		this.labels.get(reference.getId()).navigationMarker = marker;
	}
	
	/**
	 * @param reference
	 */
	public void assign(final Reference reference) {
		if (!this.labels.containsKey(reference.getId())) {
			final Label label = new Label(reference);
			label.navigationMarker = null;
			label.integrationMarker = IntegrationMarker.DIVERGE;
			this.labels.put(reference.getId(), label);
		}
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
		final Edge other = (Edge) obj;
		if (!getOuterType().equals(other.getOuterType())) {
			return false;
		}
		if (this.child == null) {
			if (other.child != null) {
				return false;
			}
		} else if (!this.child.equals(other.child)) {
			return false;
		}
		if (this.parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!this.parent.equals(other.parent)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public BranchMarker getBranchMarker() {
		return this.branchMarker;
	}
	
	/**
	 * @return the id
	 */
	public final long getId() {
		return this.id;
	}
	
	/**
	 * Gets the labels.
	 *
	 * @return the labels
	 */
	public Map<Long, Label> getLabels() {
		return UnmodifiableMap.unmodifiableMap(this.labels);
	}
	
	/**
	 * Gets the navigation marker.
	 *
	 * @param branchId
	 *            the branch id
	 * @return the navigation marker
	 */
	public NavigationMarker getNavigationMarker(final long branchId) {
		if (this.labels.containsKey(branchId)) {
			return this.labels.get(branchId).navigationMarker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch with id " + branchId);
		}
	}
	
	/**
	 * Gets the navigation marker.
	 *
	 * @param reference
	 *            the branch
	 * @return the navigation marker
	 */
	public NavigationMarker getNavigationMarker(final Reference reference) {
		if (this.labels.containsKey(reference.getId())) {
			return this.labels.get(reference.getId()).navigationMarker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch with id " + reference.getId());
		}
	}
	
	/**
	 * Gets the outer type.
	 *
	 * @return the outer type
	 */
	private Graph getOuterType() {
		return this.graph;
	}
	
	/**
	 * Gets the source id.
	 *
	 * @return the source id
	 */
	public long getSourceId() {
		return this.parent.getId();
	}
	
	/**
	 * Gets the target id.
	 *
	 * @return the target id
	 */
	public long getTargetId() {
		return this.child.getId();
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
		result = prime * result + getOuterType().hashCode();
		result = prime * result + (this.child == null
		                                             ? 0
		                                             : this.child.hashCode());
		result = prime * result + (this.parent == null
		                                              ? 0
		                                              : this.parent.hashCode());
		return result;
	}
	
	/**
	 * In branch.
	 *
	 * @param reference
	 *            the branch
	 * @return true, if successful
	 */
	public boolean inBranch(final Reference reference) {
		return this.labels.containsKey(reference.getId());
	}
	
	/**
	 * Integrates.
	 *
	 * @param reference
	 *            the branch
	 * @return true, if successful
	 */
	public boolean integrates(final Reference reference) {
		Requires.notNull(reference);
		if (!this.labels.containsKey(reference.getId())) {
			throw new IllegalArgumentException();
		}
		
		return IntegrationMarker.INTEGRATE.equals(this.labels.get(reference.getId()).integrationMarker);
	}
	
	/**
	 * Checks if is merge.
	 *
	 * @return true, if is merge
	 */
	public boolean isMerge() {
		return BranchMarker.MERGE_PARENT.equals(this.branchMarker);
	}
	
	/**
	 * Sets the type.
	 *
	 * @param marker
	 *            the type
	 */
	public void setBranchMarker(final BranchMarker marker) {
		this.branchMarker = marker;
	}
	
	/**
	 * @param id
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
	/**
	 * To string.
	 *
	 * @param reference
	 *            the branch
	 * @return the string
	 */
	public String toString(final Reference reference) {
		
		final StringBuilder builder = new StringBuilder();
		builder.append("Edge [");
		builder.append(this.branchMarker != null ? this.branchMarker.name() : null).append(" ");
		builder.append(labels.containsKey(reference.getId()) && labels.get(reference.getId()).navigationMarker != null ? this.labels.get(reference.getId()).navigationMarker.name(): null).append(" ");
		builder.append(labels.containsKey(reference.getId()) && labels.get(reference.getId()).integrationMarker != null ? this.labels.get(reference.getId()).integrationMarker.name(): null).append(" ");
		builder.append(child != null ? this.child.getHash() : null);
		builder.append(" -> ");
		builder.append(parent != null ? this.parent.getHash() : "null");
		builder.append("]");
		return builder.toString();
	}
	
}
