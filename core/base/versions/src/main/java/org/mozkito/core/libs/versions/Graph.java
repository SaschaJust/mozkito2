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
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMaskSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.MaskFunctor;
import org.jgrapht.graph.MaskSubgraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Roots;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class DepotGraph represents a multi-layer view on the Git repository. Once built, it does not require the
 * repository to be present The graph can be persisted to a {@link SequelDatabase}.
 *
 * @author Sascha Just
 */
public class Graph extends DirectedGraph implements ISequelEntity {
	
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
		
		/** The labels. */
		Map<Long, Label> labels;
		
		/** The child. */
		final ChangeSet  child;
		
		/** The parent. */
		final ChangeSet  parent;
		
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
			this.labels = new HashMap<Long, Label>();
			this.labels.put(branch.id(), new Label(branch, type));
		}
		
		/**
		 * Adds the branch.
		 *
		 * @param branch
		 *            the branch
		 * @param type
		 *            the type
		 * @return true, if successful
		 */
		public boolean addBranch(final Branch branch,
		                         final EdgeType type) {
			if (!this.labels.containsKey(branch.id())) {
				this.labels.put(branch.id(), new Label(branch, type));
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
		 */
		public void addIntegration(final Branch branch) {
			Requires.notNull(branch);
			if (!this.labels.containsKey(branch.id())) {
				throw new IllegalArgumentException("This edge is not part of branch " + branch);
			}
			
			this.labels.get(branch.id()).integration = true;
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
		 * Gets the branch ids.
		 *
		 * @return the branch ids
		 */
		public Collection<Long> getBranchIds() {
			return UnmodifiableCollection.unmodifiableCollection(this.labels.keySet());
		}
		
		/**
		 * Gets the integration path ids.
		 *
		 * @return the integration path ids
		 */
		public Collection<Long> getIntegrationPathIds() {
			return this.labels.values().stream().filter(x -> x.integration).map(x -> x.branch.id())
			                  .collect(Collectors.toList());
		}
		
		/**
		 * Gets the outer type.
		 *
		 * @return the outer type
		 */
		private Graph getOuterType() {
			return Graph.this;
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
		 * @param branch
		 *            the branch
		 * @return the type
		 */
		public short getType(final Branch branch) {
			if (this.labels.containsKey(branch.id())) {
				return (short) this.labels.get(branch.id()).type.ordinal();
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
		public EdgeType getType(final long branchId) {
			if (this.labels.containsKey(branchId)) {
				return this.labels.get(branchId).type;
			} else {
				throw new IllegalArgumentException("This edge is not part of branch with id " + branchId);
			}
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
			
			return this.labels.get(branch.id()).integration;
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
			return EdgeType.MERGE.equals(this.labels.values().iterator().next().type);
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
	 * The Class Label.
	 */
	public static class Label {
		
		/** The branch. */
		public Branch   branch;
		
		/** The integration. */
		public boolean  integration = false;
		
		/** The type. */
		public EdgeType type;
		
		/**
		 * Instantiates a new label.
		 *
		 * @param branch
		 *            the branch
		 * @param type
		 *            the type
		 */
		public Label(final Branch branch, final EdgeType type) {
			this.branch = branch;
			this.type = type;
		}
		
	}
	
	/**
	 * The Class Pointer.
	 */
	static class Pointer {
		
		/** The head. */
		ChangeSet        head;
		
		/** The parent. */
		ChangeSet        parent;
		
		/** The merges. */
		Stack<ChangeSet> merges = new Stack<>();
		
		/**
		 * Instantiates a new pointer.
		 *
		 * @param head
		 *            the head
		 * @param subGraph
		 *            the sub graph
		 * @param graph
		 *            the graph
		 */
		public Pointer(final ChangeSet head, final MaskSubgraph<ChangeSet, Edge> subGraph, final Graph graph) {
			Requires.notNull(head);
			Requires.notNull(subGraph);
			Requires.notNull(graph);
			
			this.head = head;
			this.parent = graph.getBranchParent(head, subGraph);
			this.merges.addAll(graph.getMergeParents(head, subGraph));
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("head=");
			builder.append(this.head.getCommitHash());
			builder.append(", parent=");
			builder.append(this.parent != null
			                                  ? this.parent.getCommitHash()
			                                  : "");
			builder.append(", mergeParents=");
			builder.append(this.merges.stream().map(c -> c.getCommitHash()).collect(Collectors.joining(",")));
			return builder.toString();
		}
		
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1312749091519616410L;
	
	/**
	 * Load.
	 *
	 * @param database
	 *            the database
	 * @return the depot graph
	 */
	public static Graph load(final SequelDatabase database) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/** The graph. */
	private final org.jgrapht.DirectedGraph<ChangeSet, Edge> graph;
	
	/** The depot. */
	private final Depot                                      depot;
	
	/** The id. */
	private long                                             id;
	
	/** The endpoints. */
	private final Map<Branch, Head>                          heads       = new HashMap<>();
	
	/** The roots. */
	private final Map<Branch, Roots>                         roots       = new HashMap<>();
	
	/** The edges. */
	private final Collection<Edge>                           edges       = new LinkedList<>();
	
	/** The vertices. */
	private Map<String, ChangeSet>                           vertices    = new HashMap<>();
	
	/** The branch heads. */
	private Map<String, Branch>                              branchHeads;
	
	private final List<ConvergenceEdge>                      convergence = new LinkedList<ConvergenceEdge>();
	
	/**
	 * Instantiates a new depot graph.
	 *
	 * @param depot
	 *            the depot
	 */
	public Graph(final Depot depot) {
		Requires.notNull(depot);
		
		this.depot = depot;
		this.graph = new DefaultDirectedGraph<ChangeSet, Edge>(Edge.class);
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
			this.edges.add(edge);
			return this.graph.addEdge(parent, child, edge);
		} else {
			return this.graph.getEdge(parent, child).addBranch(branch, type);
		}
	}
	
	/**
	 * Adds the head.
	 *
	 * @param branch
	 *            the branch
	 * @param changeSet
	 *            the change set
	 */
	public void addHead(final Branch branch,
	                    final ChangeSet changeSet) {
		this.heads.put(branch, new Head(branch.id(), changeSet.id()));
	}
	
	/**
	 * Adds the root.
	 *
	 * @param branch
	 *            the branch
	 * @param changeSet
	 *            the change set
	 */
	public void addRoot(final Branch branch,
	                    final ChangeSet changeSet) {
		if (!this.roots.containsKey(branch)) {
			this.roots.put(branch, new Roots(branch.id()));
		}
		this.roots.get(branch).add(changeSet.id());
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
	 * Check path.
	 *
	 * @param pointers
	 *            the pointers
	 * @param current
	 *            the current
	 * @param delegate
	 *            the delegate
	 * @return true, if successful
	 */
	private boolean checkPath(final Stack<Pointer> pointers,
	                          final Pointer current,
	                          final ChangeSet delegate) {
		for (final Pointer pointer : pointers) {
			if (!pointer.equals(current)) {
				if (DijkstraShortestPath.findPathBetween(this.graph, delegate, pointer.parent) != null) {
					// found earlier integration
					// next delegate
					// System.err.println("Found earlier path " + delegate.getCommitHash() + " -> "
					// + pointer.parent.getCommitHash());
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Compute integration graph.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeIntegrationGraph(final Branch branch) {
		final List<ChangeSet> baseLine = new LinkedList<>();
		final Set<ChangeSet> blackList = new HashSet<>();
		
		// fetch the tier1 graph for that branch (a projection of all vertices and edges in the particular branch
		// space).
		final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                              new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                        this.graph,
		                                                                                                                                        new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                        
			                                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                        return !edge.inBranch(branch);
			                                                                                                                                        }
			                                                                                                                                        
			                                                                                                                                        public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                        return !vertex.getBranchIds()
				                                                                                                                                                      .contains(branch.id())
				                                                                                                                                                || blackList.contains(vertex);
			                                                                                                                                        }
		                                                                                                                                        }));
		
		final EdgeReversedGraph<ChangeSet, Edge> baseLineGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                                new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                          this.graph,
		                                                                                                                                          new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                          
			                                                                                                                                          public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                          return !EdgeType.FORWARD.equals(edge.getType(branch))
				                                                                                                                                                  || !edge.inBranch(branch);
			                                                                                                                                          }
			                                                                                                                                          
			                                                                                                                                          public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                          return !vertex.getBranchIds()
				                                                                                                                                                        .contains(branch.id());
			                                                                                                                                          }
			                                                                                                                                          
		                                                                                                                                          }));
		
		// collect base line commits
		final ChangeSet head = getHead(branch);
		final DepthFirstIterator<ChangeSet, Edge> baseLineIterator = new DepthFirstIterator<ChangeSet, Edge>(
		                                                                                                     baseLineGraph,
		                                                                                                     head);
		
		while (baseLineIterator.hasNext()) {
			baseLine.add(0, baseLineIterator.next());
		}
		
		DepthFirstIterator<ChangeSet, Edge> revIterator = null;
		ChangeSet pointer = null;
		
		DijkstraShortestPath<ChangeSet, Edge> dsp;
		final Set<ChangeSet> tmpBlackList = new HashSet<>();
		ChangeSet previous = null;
		
		for (final ChangeSet current : baseLine) {
			// System.err.println("BL: " + current.getCommitHash());
			revIterator = new DepthFirstIterator<ChangeSet, Edge>(branchGraph, current);
			// revIterator.addTraversalListener(listener);
			POINTERS: while (revIterator.hasNext()) {
				pointer = revIterator.next();
				if (tmpBlackList.contains(pointer)) {
					continue POINTERS;
				}
				
				dsp = new DijkstraShortestPath<ChangeSet, Edge>(branchGraph, current, pointer);
				for (final Edge edge : dsp.getPathEdgeList()) {
					tmpBlackList.add(edge.child);
					tmpBlackList.add(edge.parent);
					edge.integrationPath.add(branch);
				}
			}
			
			if (previous != null) {
				this.graph.getEdge(previous, current).integrationPath.add(branch);
			}
			
			blackList.addAll(tmpBlackList);
			blackList.add(current);
			tmpBlackList.clear();
			previous = current;
		}
	}
	
	/**
	 * Compute integration graph.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeIntegrationGraph2(final Branch branch) {
		// fetch the tier1 graph for that branch (a projection of all vertices and edges in the particular branch
		// space).
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = tier1Graph(branch);
		// fetch all roots of this branch to check if we find them all
		final List<Long> roots = new ArrayList<>(getRoots(branch).getChangeSetIds());
		// used to store the pointers
		final Stack<Pointer> pointers = new Stack<>();
		
		// create first pointer
		Pointer pointer = new Pointer(getHead(branch), branchGraph, this);
		pointers.push(pointer);
		
		// process until we run out of pointers
		WHILE: while (!pointers.isEmpty()) {
			if (!pointer.merges.isEmpty()) {
				final Pointer p = new Pointer(pointer.merges.pop(), branchGraph, this);
				if (checkPath(pointers, pointer, p.head)) {
					pointers.pop();
					pointer = pointers.peek();
				} else {
					this.graph.getEdge(p.head, pointer.head).addIntegration(branch);;
					pointer = p;
					pointers.push(pointer);
				}
			} else if (pointer.parent != null) {
				final Pointer previous = pointers.pop();
				pointer = new Pointer(pointer.parent, branchGraph, this);
				pointers.push(pointer);
				if (checkPath(pointers, pointer, pointer.head)) {
					pointers.pop();
					pointer = pointers.peek();
				} else {
					this.graph.getEdge(pointer.head, previous.head).addIntegration(branch);
				}
			} else {
				// root
				roots.remove(pointer.head);
				pointers.pop();
			}
		}
		
		// Ensures.empty(roots);
	}
	
	/**
	 * Compute integration points.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeIntegrationPoints(final Branch branch) {
		final List<ChangeSet> baseLine = new LinkedList<>();
		final Set<ChangeSet> blackList = new HashSet<>();
		
		// fetch the tier1 graph for that branch (a projection of all vertices and edges in the particular branch
		// space).
		final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                              new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                        this.graph,
		                                                                                                                                        new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                        
			                                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                        return !edge.inBranch(branch);
			                                                                                                                                        }
			                                                                                                                                        
			                                                                                                                                        public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                        return !vertex.getBranchIds()
				                                                                                                                                                      .contains(branch.id())
				                                                                                                                                                || blackList.contains(vertex);
			                                                                                                                                        }
		                                                                                                                                        }));
		
		final EdgeReversedGraph<ChangeSet, Edge> baseLineGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                                new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                          this.graph,
		                                                                                                                                          new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                          
			                                                                                                                                          public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                          return !EdgeType.FORWARD.equals(edge.getType(branch))
				                                                                                                                                                  || !edge.inBranch(branch);
			                                                                                                                                          }
			                                                                                                                                          
			                                                                                                                                          public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                          return !vertex.getBranchIds()
				                                                                                                                                                        .contains(branch.id());
			                                                                                                                                          }
			                                                                                                                                          
		                                                                                                                                          }));
		
		final ChangeSet head = getHead(branch);
		
		final DepthFirstIterator<ChangeSet, Edge> baseLineIterator = new DepthFirstIterator<ChangeSet, Edge>(
		                                                                                                     baseLineGraph,
		                                                                                                     head);
		
		while (baseLineIterator.hasNext()) {
			baseLine.add(0, baseLineIterator.next());
		}
		
		DepthFirstIterator<ChangeSet, Edge> revIterator = null;
		ChangeSet pointer = null;
		
		for (final ChangeSet current : baseLine) {
			revIterator = new DepthFirstIterator<ChangeSet, Edge>(branchGraph, current);
			while (revIterator.hasNext()) {
				pointer = revIterator.next();
				this.convergence.add(new ConvergenceEdge(branch.id(), pointer.id(), current.id()));
				
				blackList.add(pointer);
			}
			blackList.add(current);
		}
	}
	
	/**
	 * Gets the branches.
	 *
	 * @return the branches
	 */
	public Set<Branch> getBranches() {
		return UnmodifiableSet.unmodifiableSet(this.heads.keySet());
	}
	
	/**
	 * Gets the branch parent.
	 *
	 * @param changeSet
	 *            the change set
	 * @param graph
	 *            the graph
	 * @return the branch parent
	 */
	public ChangeSet getBranchParent(final ChangeSet changeSet,
	                                 final MaskSubgraph<ChangeSet, Edge> graph) {
		final Optional<Edge> edge = graph.incomingEdgesOf(changeSet).stream().filter(x -> !x.isMerge()).findFirst();
		if (edge.isPresent()) {
			return edge.get().parent;
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the change sets.
	 *
	 * @param branch
	 *            the branch
	 * @return the change sets
	 */
	public Iterator<ChangeSet> getChangeSets(final Branch branch) {
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = tier1Graph(branch);
		final EdgeReversedGraph<ChangeSet, Edge> reversedGraph = new EdgeReversedGraph<ChangeSet, Edge>(branchGraph);
		
		final ChangeSet head = getHead(branch);
		final DepthFirstIterator<ChangeSet, Edge> iterator = new DepthFirstIterator<ChangeSet, Edge>(reversedGraph,
		                                                                                             head);
		return UnmodifiableIterator.unmodifiableIterator(new ChangeSetIterator(iterator));
		
	}
	
	/**
	 * Gets the convergence.
	 *
	 * @return the convergence
	 */
	public List<ConvergenceEdge> getConvergence() {
		return UnmodifiableList.unmodifiableList(this.convergence);
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
	 * Gets the edge.
	 *
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the edge
	 */
	public Edge getEdge(final long from,
	                    final long to) {
		return this.graph.edgeSet().parallelStream().filter(e -> e.parent.id() == from && e.child.id() == to).findAny()
		                 .get();
	}
	
	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	public Collection<Edge> getEdges() {
		return UnmodifiableCollection.unmodifiableCollection(this.edges);
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
		
		return this.vertices.get(this.branchHeads.entrySet().stream().filter(es -> es.getValue().equals(branch))
		                                         .findAny().get().getKey());
		
	}
	
	/**
	 * Gets the heads.
	 *
	 * @return the heads
	 */
	public Collection<Head> getHeads() {
		return UnmodifiableCollection.unmodifiableCollection(this.heads.values());
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
		final DirectedMaskSubgraph<ChangeSet, Edge> integrationGraph = integrationGraph(branch);
		
		final List<Edge> path = DijkstraShortestPath.findPathBetween(integrationGraph, changeSet, getHead(branch));
		final List<ChangeSet> entries = new ArrayList<ChangeSet>(path.size() + 1);
		
		if (!path.isEmpty()) {
			entries.add(path.iterator().next().child);
			entries.addAll(path.stream().map(x -> x.parent).collect(Collectors.toList()));
		}
		
		return entries;
	}
	
	/**
	 * Gets the merge parents.
	 *
	 * @param changeSet
	 *            the change set
	 * @param graph
	 *            the graph
	 * @return the merge parents
	 */
	public List<ChangeSet> getMergeParents(final ChangeSet changeSet,
	                                       final MaskSubgraph<ChangeSet, Edge> graph) {
		return graph.incomingEdgesOf(changeSet).stream().filter(x -> !x.isMerge()).map(x -> x.parent)
		            .collect(Collectors.toList());
	}
	
	/**
	 * Gets the parents.
	 *
	 * @param changeSet
	 *            the change set
	 * @param graph
	 *            the graph
	 * @return the parents
	 */
	public Set<Edge> getParents(final ChangeSet changeSet,
	                            final MaskSubgraph<ChangeSet, Edge> graph) {
		return graph.incomingEdgesOf(changeSet);
	}
	
	/**
	 * Gets the roots.
	 *
	 * @return the roots
	 */
	public Collection<Roots> getRoots() {
		return UnmodifiableCollection.unmodifiableCollection(this.roots.values());
	}
	
	/**
	 * Gets the root commit.
	 *
	 * @param branch
	 *            the branch
	 * @return the root commit
	 */
	public Roots getRoots(final Branch branch) {
		Requires.notNull(branch);
		
		if (this.roots.containsKey(branch)) {
			return this.roots.get(branch);
		}
		
		final DirectedMaskSubgraph<ChangeSet, Edge> branchGraph = tier1Graph(branch);
		
		final EdgeReversedGraph<ChangeSet, Edge> reversedGraph = new EdgeReversedGraph<ChangeSet, Edge>(branchGraph);
		
		final ChangeSet head = getHead(branch);
		
		Logger.info("Fetching reversed tier1 graph for branch '%s' starting at HEAD '%s'", branch.getName(),
		            head.getCommitHash());
		
		final DepthFirstIterator<ChangeSet, Edge> iterator = new DepthFirstIterator<ChangeSet, Edge>(reversedGraph,
		                                                                                             head);
		ChangeSet current = null;
		this.roots.put(branch, new Roots(branch.id()));
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (getParents(current, branchGraph).isEmpty()) {
				this.roots.get(branch).add(current.id());
			}
		}
		
		return this.roots.get(branch);
	}
	
	/**
	 * Gets the spin offs.
	 *
	 * @param changeSet
	 *            the change set
	 * @param branch
	 *            the branch
	 * @return the spin offs
	 */
	public List<ChangeSet> getSpinOffs(final ChangeSet changeSet,
	                                   final Branch branch) {
		return this.graph.outgoingEdgesOf(changeSet).stream().filter(x -> EdgeType.BRANCH.equals(x.getType(branch)))
		                 .map(x -> x.parent).collect(Collectors.toList());
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param id
	 *            the id
	 * @return the vertex
	 */
	public ChangeSet getVertex(final long id) {
		return this.graph.vertexSet().stream().filter(x -> x.id() == id).findAny().get();
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param hash
	 *            the hash
	 * @return the vertex
	 */
	public ChangeSet getVertex(final String hash) {
		return this.vertices.get(hash);
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
	
	/**
	 * Integration graph.
	 *
	 * @param branch
	 *            the branch
	 * @return the directed mask subgraph
	 */
	private DirectedMaskSubgraph<ChangeSet, Edge> integrationGraph(final Branch branch) {
		return new DirectedMaskSubgraph<ChangeSet, Edge>(this.graph, new MaskFunctor<ChangeSet, Edge>() {
			
			public boolean isEdgeMasked(final Edge arg0) {
				return !arg0.integrates(branch);
			}
			
			public boolean isVertexMasked(final ChangeSet arg0) {
				return !arg0.getBranchIds().contains(branch.id());
			}
		});
	}
	
	/**
	 * Sets the branch heads.
	 *
	 * @param branchHeads
	 *            the branch heads
	 */
	public void setBranchHeads(final Map<String, Branch> branchHeads) {
		this.branchHeads = branchHeads;
	}
	
	/**
	 * Sets the change sets.
	 *
	 * @param changeSets
	 *            the change sets
	 */
	public void setChangeSets(final Map<String, ChangeSet> changeSets) {
		this.vertices = changeSets;
	}
	
	/**
	 * Skip forwards.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the change set
	 */
	public ChangeSet skipForwards(final ChangeSet changeSet) {
		Set<Edge> incomingEdges = this.graph.incomingEdgesOf(changeSet);
		
		while (incomingEdges.size() == 1) {
			incomingEdges = this.graph.incomingEdgesOf(incomingEdges.iterator().next().parent);
		}
		
		return incomingEdges.iterator().next().child;
	}
	
	/**
	 * Gets the branch graph.
	 *
	 * @param branch
	 *            the branch
	 * @return the branch graph
	 */
	private DirectedMaskSubgraph<ChangeSet, Edge> tier1Graph(final Branch branch) {
		return new DirectedMaskSubgraph<ChangeSet, Edge>(this.graph, new MaskFunctor<ChangeSet, Edge>() {
			
			public boolean isEdgeMasked(final Edge arg0) {
				return !arg0.inBranch(branch);
			}
			
			public boolean isVertexMasked(final ChangeSet arg0) {
				return !arg0.getBranchIds().contains(branch.id());
			}
		});
	}
	
}
