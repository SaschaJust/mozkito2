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

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Edge.
 */
public class Edge {
	
	/**
     * 
     */
	private final Graph graph;
	
	/** The labels. */
	Map<Long, Label>    labels;
	
	/** The child. */
	final ChangeSet     child;
	
	/** The parent. */
	final ChangeSet     parent;
	
	/**
	 * Instantiates a new edge.
	 *
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 * @param type
	 *            the type
	 * @param branch
	 *            the branch
	 * @param graph
	 */
	public Edge(final Graph graph, final ChangeSet parent, final ChangeSet child, final BranchMarker type,
	        final Branch branch) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.labels = new HashMap<Long, Label>();
		final Label label = new Label(branch, type);
		label.branchMarker = type;
		label.navigationMarker = null;
		label.integrationMarker = IntegrationMarker.DIVERGE;
		this.labels.put(branch.id(), label);
	}
	
	/**
	 * Adds the branch.
	 *
	 * @param branch
	 *            the branch
	 * @param marker
	 *            the type
	 * @return true, if successful
	 */
	public boolean addBranch(final Branch branch,
	                         final BranchMarker marker) {
		
		if (!this.labels.containsKey(branch.id())) {
			final Label label = new Label(branch, marker);
			label.branchMarker = marker;
			label.navigationMarker = null;
			label.integrationMarker = IntegrationMarker.DIVERGE;
			this.labels.put(branch.id(), label);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds the integration.
	 *
	 * @param branch
	 *            the branch
	 * @param marker
	 *            the marker
	 */
	public void addIntegration(final Branch branch,
	                           final IntegrationMarker marker) {
		Requires.notNull(branch);
		if (!this.labels.containsKey(branch.id())) {
			throw new IllegalArgumentException("This edge is not part of branch " + branch);
		}
		
		this.labels.get(branch.id()).integrationMarker = marker;
	}
	
	/**
	 * Adds the navigation.
	 *
	 * @param branch
	 *            the branch
	 * @param marker
	 *            the marker
	 */
	public void addNavigation(final Branch branch,
	                          final NavigationMarker marker) {
		Requires.notNull(branch);
		if (!this.labels.containsKey(branch.id())) {
			throw new IllegalArgumentException("This edge is not part of branch " + branch);
		}
		
		this.labels.get(branch.id()).navigationMarker = marker;
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
	 * @param branch
	 *            the branch
	 * @return the type
	 */
	public BranchMarker getBranchMarker(final Branch branch) {
		if (this.labels.containsKey(branch.id())) {
			return this.labels.get(branch.id()).branchMarker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch " + branch);
		}
	}
	
	/**
	 * Gets the type.
	 *
	 * @param branchId
	 *            the branch id
	 * @return the type
	 */
	public BranchMarker getBranchMarker(final long branchId) {
		if (this.labels.containsKey(branchId)) {
			return this.labels.get(branchId).branchMarker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch with id " + branchId);
		}
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
	 * @param branch
	 *            the branch
	 * @return the navigation marker
	 */
	public NavigationMarker getNavigationMarker(final Branch branch) {
		if (this.labels.containsKey(branch.id())) {
			return this.labels.get(branch.id()).navigationMarker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch with id " + branch.id());
		}
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
		return this.parent.id();
	}
	
	/**
	 * Gets the target id.
	 *
	 * @return the target id
	 */
	public long getTargetId() {
		return this.child.id();
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
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 */
	public boolean inBranch(final Branch branch) {
		return this.labels.containsKey(branch.id());
	}
	
	/**
	 * Integrates.
	 *
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 */
	public boolean integrates(final Branch branch) {
		Requires.notNull(branch);
		if (!this.labels.containsKey(branch.id())) {
			throw new IllegalArgumentException();
		}
		
		return IntegrationMarker.INTEGRATE.equals(this.labels.get(branch.id()).integrationMarker);
	}
	
	/**
	 * Checks if is merge.
	 *
	 * @return true, if is merge
	 */
	public boolean isMerge() {
		if (this.labels.isEmpty()) {
			throw new IllegalStateException("Edge is not attached to a branch.");
		}
		return BranchMarker.MERGE_PARENT.equals(this.labels.values().iterator().next().branchMarker);
	}
	
	/**
	 * Sets the type.
	 *
	 * @param branch
	 *            the branch
	 * @param marker
	 *            the type
	 */
	public void setBranchMarker(final Branch branch,
	                            final BranchMarker marker) {
		if (this.labels.containsKey(branch.id())) {
			this.labels.get(branch.id()).branchMarker = marker;
		} else {
			throw new IllegalArgumentException("This edge is not part of branch with id " + branch.id());
		}
	}
	
	/**
	 * To string.
	 *
	 * @param branch
	 *            the branch
	 * @return the string
	 */
	public String toString(final Branch branch) {
		
		final StringBuilder builder = new StringBuilder();
		builder.append("Edge [");
		builder.append(this.labels.get(branch.id()).branchMarker.name()).append(" ");
		builder.append(this.labels.get(branch.id()).navigationMarker.name()).append(" ");
		builder.append(this.labels.get(branch.id()).integrationMarker.name()).append(" ");
		builder.append(this.child.getCommitHash());
		builder.append(" -> ");
		builder.append(this.parent.getCommitHash());
		builder.append("]");
		return builder.toString();
	}
	
}
