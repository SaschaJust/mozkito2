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

package org.mozkito.core.libs.versions;

import graphs.DirectedGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMaskSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.MaskFunctor;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.SequelDatabase;

// TODO: Auto-generated Javadoc
/**
 * The Class DepotGraph represents a multi-layer view on the Git repository. Once built, it does not require the
 * repository to be present The graph can be persisted to a {@link SequelDatabase}.
 *
 * @author Sascha Just
 */
public class DepotGraph extends DirectedGraph {
	
	/**
	 * The Class ChangeSetIterator.
	 *
	 * @author Sascha Just
	 */
	public class ChangeSetIterator implements Iterator<ChangeSet> {
		
		/** The iterator. */
		private final GraphIterator<ChangeSet, Edge> iterator;
		
		/**
		 * Instantiates a new change set iterator.
		 *
		 * @param graphIterator
		 *            the graph iterator
		 */
		public ChangeSetIterator(final GraphIterator<ChangeSet, Edge> graphIterator) {
			this.iterator = graphIterator;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return this.iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		public ChangeSet next() {
			return this.iterator.next();
		}
		
	}
	
	/**
	 * The Class Edge.
	 */
	public class Edge {
		
		/** The integration path. */
		final Set<Branch> integrationPath = new HashSet<Branch>();
		
		/** The branches. */
		final Set<Branch> branches        = new HashSet<Branch>();
		
		/** The type. */
		final EdgeType    type;
		
		/** The child. */
		final ChangeSet   child;
		
		/** The parent. */
		final ChangeSet   parent;
		
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
		 */
		public Edge(final ChangeSet parent, final ChangeSet child, final EdgeType type, final Branch branch) {
			this.parent = parent;
			this.child = child;
			this.type = type;
			this.branches.add(branch);
			
		}
		
		/**
		 * Adds the branch.
		 *
		 * @param branch
		 *            the branch
		 * @return true, if successful
		 */
		public boolean addBranch(final Branch branch) {
			return this.branches.add(branch);
		}
		
		/**
		 * Gets the branch ids.
		 *
		 * @return the branch ids
		 */
		public Collection<Integer> getBranchIds() {
			return this.branches.stream().map(x -> x.id()).collect(Collectors.toList());
		}
		
		/**
		 * Gets the integration path ids.
		 *
		 * @return the integration path ids
		 */
		public Collection<Integer> getIntegrationPathIds() {
			return this.integrationPath.stream().map(x -> x.id()).collect(Collectors.toList());
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
		 * Gets the type.
		 *
		 * @return the type
		 */
		public short getType() {
			return (short) this.type.ordinal();
		}
		
	}
	
	/**
	 * The Enum EdgeType.
	 */
	public static enum EdgeType {
		
		/** The merge. */
		MERGE,
		/** The branch. */
		BRANCH,
		/** The forward. */
		FORWARD;
	}
	
	/**
	 * Load.
	 *
	 * @param database
	 *            the database
	 * @return the depot graph
	 */
	public static DepotGraph load(final SequelDatabase database) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/** The graph. */
	private final org.jgrapht.DirectedGraph<ChangeSet, Edge> graph;
	
	/** The branch heads. */
	private final Map<Branch, ChangeSet>                     branchHeads = new HashMap<Branch, ChangeSet>();
	
	/** The depot. */
	private final Depot                                      depot;
	
	/**
	 * Instantiates a new depot graph.
	 *
	 * @param depot
	 *            the depot
	 */
	public DepotGraph(final Depot depot) {
		Requires.notNull(depot);
		
		this.depot = depot;
		this.graph = new DefaultDirectedGraph<ChangeSet, Edge>(Edge.class);
	}
	
	/**
	 * Adds the branch head.
	 *
	 * @param branch
	 *            the branch
	 * @param changeSet
	 *            the change set
	 * @return true, if successful
	 */
	public boolean addBranchHead(final Branch branch,
	                             final ChangeSet changeSet) {
		if (!this.branchHeads.containsKey(branch)) {
			this.branchHeads.put(branch, changeSet);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds an integration edge between the child and the parent within the given branch.
	 *
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 * @param type
	 *            the type
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 */
	public boolean addEdge(final ChangeSet parent,
	                       final ChangeSet child,
	                       final EdgeType type,
	                       final Branch branch) {
		Requires.notNull(parent);
		Requires.notNull(child);
		Requires.notNull(type);
		Requires.notNull(branch);
		
		final Edge edge = new Edge(parent, child, type, branch);
		if (!this.graph.containsEdge(edge)) {
			return this.graph.addEdge(parent, child, edge);
		} else {
			return this.graph.getEdge(parent, child).addBranch(branch);
		}
	}
	
	/**
	 * Adds the vertex.
	 *
	 * @param changeSet
	 *            the change set
	 * @return true, if successful
	 */
	public boolean addVertex(final ChangeSet changeSet) {
		Requires.notNull(changeSet);
		Asserts.notNull(this.graph);
		
		return this.graph.addVertex(changeSet);
	}
	
	/**
	 * Compute integration graph.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeIntegrationGraph(final Branch branch) {
		ChangeSet head = getHead(branch);
		final ChangeSet root = getRootCommit(branch);
		
		while (!head.equals(root)) {
			head = skipForwards(head);
			final Set<Edge> incomingEdges = this.graph.incomingEdgesOf(head);
			
			if (incomingEdges.size() == 1) {
				head = incomingEdges.iterator().next().parent;
			} else {
				ChangeSet branchParent = null;
				final Stack<ChangeSet> delegates = new Stack<ChangeSet>();
				for (final Edge incoming : incomingEdges) {
					if (EdgeType.BRANCH.equals(incoming.type)) {
						branchParent = incoming.parent;
					} else {
						Asserts.equalTo(EdgeType.MERGE, incoming.type);
						delegates.push(incoming.parent);
					}
				}
				
				Asserts.notNull(branchParent);
				
			}
		}
	}
	
	/**
	 * Gets the branches.
	 *
	 * @return the branches
	 */
	public Set<Branch> getBranches() {
		return UnmodifiableSet.unmodifiableSet(this.branchHeads.keySet());
	}
	
	/**
	 * Gets the branch graph.
	 *
	 * @param branch
	 *            the branch
	 * @return the branch graph
	 */
	private DirectedMaskSubgraph<ChangeSet, Edge> getBranchGraph(final Branch branch) {
		return new DirectedMaskSubgraph<ChangeSet, Edge>(this.graph, new MaskFunctor<ChangeSet, Edge>() {
			
			public boolean isEdgeMasked(final Edge arg0) {
				return arg0.branches.contains(branch);
			}
			
			public boolean isVertexMasked(final ChangeSet arg0) {
				return arg0.getBranchIds().contains(branch.id());
			}
		});
	}
	
	/**
	 * Gets the branch parent.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the branch parent
	 */
	public ChangeSet getBranchParent(final ChangeSet changeSet) {
		return this.graph.incomingEdgesOf(changeSet).stream()
		                 .filter(x -> EdgeType.BRANCH.equals(x.type) || EdgeType.FORWARD.equals(x.type)).findFirst()
		                 .get().parent;
	}
	
	/**
	 * Gets the change sets.
	 *
	 * @param branch
	 *            the branch
	 * @return the change sets
	 */
	public Iterator<ChangeSet> getChangeSets(final Branch branch) {
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = getBranchGraph(branch);
		final EdgeReversedGraph<ChangeSet, Edge> reversedGraph = new EdgeReversedGraph<ChangeSet, Edge>(branchGraph);
		
		final ChangeSet head = this.branchHeads.get(branch);
		final DepthFirstIterator<ChangeSet, Edge> iterator = new DepthFirstIterator<ChangeSet, Edge>(reversedGraph,
		                                                                                             head);
		return UnmodifiableIterator.unmodifiableIterator(new ChangeSetIterator(iterator));
		
	}
	
	/**
	 * Gets the depot.
	 *
	 * @return the depot
	 */
	public final Depot getDepot() {
		return this.depot;
	}
	
	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	public Set<Edge> getEdges() {
		return UnmodifiableSet.unmodifiableSet(this.graph.edgeSet());
	}
	
	/**
	 * Gets the first commit to the given branch. This is not the root. Example:
	 * 
	 * <pre>
	 * master:   A --> B --> D
	 *             \
	 * example:     --> C --> E
	 * 
	 * example:  A --> C --> E
	 * </pre>
	 * 
	 * In this case, {@link #getFirstCommitToBranch(Branch)} returns C, since this was the first original commit to this
	 * branch and A originates from master.
	 * 
	 * @param branch
	 *            the branch
	 * @return the first commit to branch
	 */
	public ChangeSet getFirstCommitToBranch(final Branch branch) {
		Requires.notNull(branch);
		Asserts.notNull(this.branchHeads);
		
		if (!this.branchHeads.containsKey(branch)) {
			throw new IllegalArgumentException(String.format("Branch '%s' not known to graph.", branch));
		}
		
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                    this.graph,
		                                                                                                    new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                    
			                                                                                                    public boolean isEdgeMasked(final Edge arg0) {
				                                                                                                    return arg0.branches.contains(branch);
			                                                                                                    }
			                                                                                                    
			                                                                                                    public boolean isVertexMasked(final ChangeSet arg0) {
				                                                                                                    return arg0.getBranchIds()
				                                                                                                               .contains(branch.id());
			                                                                                                    }
		                                                                                                    });
		
		final ChangeSet head = this.branchHeads.get(branch);
		
		final DepthFirstIterator<ChangeSet, Edge> iterator = new DepthFirstIterator<ChangeSet, Edge>(branchGraph, head);
		ChangeSet commit = null;
		
		while (iterator.hasNext()) {
			commit = iterator.next();
			assert false;
			// todo
			// if (commit.getOrigin() == branch.id()) {
			// return commit;
			// }
		}
		
		return null;
	}
	
	/**
	 * Gets the head.
	 *
	 * @param branch
	 *            the branch
	 * @return the head
	 */
	public ChangeSet getHead(final Branch branch) {
		Requires.notNull(branch);
		Asserts.notNull(this.branchHeads);
		
		if (!this.branchHeads.containsKey(branch)) {
			throw new IllegalArgumentException(String.format("Branch '%s' not known to graph.", branch));
		}
		
		return this.branchHeads.get(branch);
	}
	
	/**
	 * Gets the integration path.
	 *
	 * @param changeSet
	 *            the change set
	 * @param branch
	 *            the branch
	 * @return the integration path
	 */
	public List<ChangeSet> getIntegrationPath(final ChangeSet changeSet,
	                                          final Branch branch) {
		final DirectedMaskSubgraph<ChangeSet, Edge> integrationGraph = new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                         this.graph,
		                                                                                                         new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                         
			                                                                                                         public boolean isEdgeMasked(final Edge arg0) {
				                                                                                                         return !arg0.integrationPath.contains(branch);
			                                                                                                         }
			                                                                                                         
			                                                                                                         public boolean isVertexMasked(final ChangeSet arg0) {
				                                                                                                         return !arg0.getBranchIds()
				                                                                                                                     .contains(branch.id());
			                                                                                                         }
		                                                                                                         });
		
		final List<Edge> path = DijkstraShortestPath.findPathBetween(integrationGraph, changeSet,
		                                                             this.branchHeads.get(branch));
		final List<ChangeSet> entries = new ArrayList<ChangeSet>(path.size() + 1);
		
		if (!path.isEmpty()) {
			entries.add(path.iterator().next().child);
			entries.addAll(path.stream().map(x -> x.parent).collect(Collectors.toList()));
		}
		
		return new LinkedList<>();
	}
	
	/**
	 * Gets the merge parents.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the merge parents
	 */
	public List<ChangeSet> getMergeParents(final ChangeSet changeSet) {
		return this.graph.incomingEdgesOf(changeSet).stream().filter(x -> EdgeType.MERGE.equals(x.type))
		                 .map(x -> x.parent).collect(Collectors.toList());
	}
	
	/**
	 * Gets the root commit.
	 *
	 * @param branch
	 *            the branch
	 * @return the root commit
	 */
	public ChangeSet getRootCommit(final Branch branch) {
		Requires.notNull(branch);
		Asserts.notNull(this.branchHeads);
		
		if (!this.branchHeads.containsKey(branch)) {
			throw new IllegalArgumentException(String.format("Branch '%s' not known to graph.", branch));
		}
		
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = getBranchGraph(branch);
		
		final EdgeReversedGraph<ChangeSet, Edge> reversedGraph = new EdgeReversedGraph<ChangeSet, Edge>(branchGraph);
		
		final ChangeSet head = this.branchHeads.get(branch);
		final DepthFirstIterator<ChangeSet, Edge> iterator = new DepthFirstIterator<ChangeSet, Edge>(reversedGraph,
		                                                                                             head);
		ChangeSet root = null;
		
		while (iterator.hasNext()) {
			root = iterator.next();
		}
		
		return root;
	}
	
	/**
	 * Gets the spin offs.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the spin offs
	 */
	public List<ChangeSet> getSpinOffs(final ChangeSet changeSet) {
		return this.graph.outgoingEdgesOf(changeSet).stream().filter(x -> EdgeType.BRANCH.equals(x.type))
		                 .map(x -> x.parent).collect(Collectors.toList());
	}
	
	/**
	 * Skip forwards.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the change set
	 */
	private ChangeSet skipForwards(final ChangeSet changeSet) {
		Set<Edge> incomingEdges = this.graph.incomingEdgesOf(changeSet);
		
		while (incomingEdges.size() == 1) {
			incomingEdges = this.graph.incomingEdgesOf(incomingEdges.iterator().next().parent);
		}
		
		return incomingEdges.iterator().next().child;
	}
	
}
