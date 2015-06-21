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

import java.time.Instant;
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
import org.jgrapht.DirectedGraph;
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
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.IEntity;
import org.mozkito.skeleton.sequel.Database;

// TODO: Auto-generated Javadoc
/**
 * The Class DepotGraph represents a multi-layer view on the Git repository. Once built, it does not require the
 * repository to be present The graph can be persisted to a {@link Database}.
 *
 * @author Sascha Just
 */
public class Graph implements IEntity {
	
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
	public static Graph load(final Database database) {
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
	private final Map<Branch, Set<Root>>                     roots       = new HashMap<>();
	
	/** The edges. */
	private final Collection<Edge>                           edges       = new LinkedList<>();
	
	/** The vertices. */
	private Map<String, ChangeSet>                           vertices    = new HashMap<>();
	
	/** The branch heads. */
	private Map<String, Branch>                              branchHeads;
	
	/** The convergence. */
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
	                       final BranchMarker type,
	                       final Branch branch) {
		Requires.notNull(parent);
		Requires.notNull(child);
		Requires.notNull(type);
		Requires.notNull(branch);
		
		final Edge edge = new Edge(this, parent, child, type, branch);
		
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
			this.roots.put(branch, new HashSet<Root>());
		}
		
		this.roots.get(branch).add(new Root(branch.id(), changeSet.id()));
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
	 * Compute integration points.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeConvergence(final Branch branch) {
		Requires.notNull(branch);
		final Instant start = Instant.now();
		final List<ChangeSet> baseLine = new LinkedList<>();
		final Set<ChangeSet> blackList = new HashSet<>();
		
		// fetch the tier1 graph for that branch (a projection of all vertices and edges in the particular branch
		// space).
		final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                              new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                        this.graph,
		                                                                                                                                        new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                        
			                                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                        return !edge.inBranch(branch)
				                                                                                                                                                || blackList.contains(edge.parent);
			                                                                                                                                        }
			                                                                                                                                        
			                                                                                                                                        public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                        return !vertex.getBranchIds()
				                                                                                                                                                      .contains(branch.id())
				                                                                                                                                                || blackList.contains(vertex);
			                                                                                                                                        }
		                                                                                                                                        }));
		
		final DirectedGraph<ChangeSet, Edge> baseLineGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                            new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                      this.graph,
		                                                                                                                                      new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                      
			                                                                                                                                      public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                      if (BranchMarker.MERGE_PARENT.equals(edge.getBranchMarker(branch))
				                                                                                                                                              || !edge.inBranch(branch)) {
					                                                                                                                                      return true;
				                                                                                                                                      }
				                                                                                                                                      return false;
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
			final ChangeSet tmp = baseLineIterator.next();
			baseLine.add(0, tmp);
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
		Logger.info("Convergence computation duration: " + (Instant.now().getEpochSecond() - start.getEpochSecond())
		        + "s.");
	}
	
	/**
	 * Compute integration graph.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeIntegrationGraph(final Branch branch) {
		Requires.notNull(branch);
		final Instant start = Instant.now();
		final List<ChangeSet> baseLine = new LinkedList<>();
		final Set<ChangeSet> blackList = new HashSet<ChangeSet>();
		
		final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                              new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                        this.graph,
		                                                                                                                                        new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                        
			                                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                        return !edge.inBranch(branch)
				                                                                                                                                                || blackList.contains(edge.parent);
			                                                                                                                                        }
			                                                                                                                                        
			                                                                                                                                        public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                        return !vertex.getBranchIds()
				                                                                                                                                                      .contains(branch.id())
				                                                                                                                                                || blackList.contains(vertex);
			                                                                                                                                        }
		                                                                                                                                        }));
		
		final DirectedGraph<ChangeSet, Edge> baseLane = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                       new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                 this.graph,
		                                                                                                                                 new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                 
			                                                                                                                                 public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                 if (BranchMarker.MERGE_PARENT.equals(edge.getBranchMarker(branch))
				                                                                                                                                         || !edge.inBranch(branch)) {
					                                                                                                                                 return true;
				                                                                                                                                 }
				                                                                                                                                 return false;
			                                                                                                                                 }
			                                                                                                                                 
			                                                                                                                                 public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                 return !vertex.getBranchIds()
				                                                                                                                                               .contains(branch.id());
			                                                                                                                                 }
			                                                                                                                                 
		                                                                                                                                 }));
		
		final ChangeSet head = getHead(branch);
		final DepthFirstIterator<ChangeSet, Edge> baseLineIterator = new DepthFirstIterator<ChangeSet, Edge>(baseLane,
		                                                                                                     head);
		Set<Edge> outgoingEdges;
		List<ChangeSet> pointers = new LinkedList<>();
		
		while (baseLineIterator.hasNext()) {
			final ChangeSet tmp = baseLineIterator.next();
			baseLine.add(0, tmp);
		}
		
		for (final ChangeSet current : baseLine) {
			pointers.add(current);
			
			while (!pointers.isEmpty()) {
				final ChangeSet pointer = pointers.remove(0);
				
				outgoingEdges = branchGraph.outgoingEdgesOf(pointer);
				
				for (final Edge edge : outgoingEdges) {
					if (!blackList.contains(edge.parent)) { // redundant?
						edge.addIntegration(branch, IntegrationMarker.INTEGRATE);
						pointers.add(edge.parent);
					}
					
				}
				
				blackList.add(pointer);
				pointers = pointers.stream().filter(p -> !blackList.contains(p)).collect(Collectors.toList());
			}
		}
		
		Logger.info("Integration computation duration: " + (Instant.now().getEpochSecond() - start.getEpochSecond())
		        + "s.");
	}
	
	/**
	 * Compute navigation graph.
	 *
	 * @param branch
	 *            the branch
	 */
	public void computeNavigationGraph(final Branch branch) {
		final Instant start = Instant.now();
		
		final ChangeSet head = getHead(branch);
		final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		                                                                                              new DirectedMaskSubgraph<ChangeSet, Edge>(
		                                                                                                                                        this.graph,
		                                                                                                                                        new MaskFunctor<ChangeSet, Edge>() {
			                                                                                                                                        
			                                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                                        return !edge.inBranch(branch);
			                                                                                                                                        }
			                                                                                                                                        
			                                                                                                                                        public boolean isVertexMasked(final ChangeSet vertex) {
				                                                                                                                                        return !vertex.getBranchIds()
				                                                                                                                                                      .contains(branch.id());
			                                                                                                                                        }
			                                                                                                                                        
		                                                                                                                                        }));
		// used for graph traversal for every lane switch
		final List<ChangeSet> pointers = new LinkedList<>();
		
		// used to store edges we already traversed
		final Set<Edge> blackList = new HashSet<>();
		
		ChangeSet current;
		// start at head
		pointers.add(head);
		Set<Edge> outgoingEdges;
		
		while (!pointers.isEmpty()) {
			current = pointers.remove(0);
			do {
				// filter outgoing edges against the blacklist. we could do this in the mask functor, but this will
				// cause problems on when to update the blacklist.
				outgoingEdges = branchGraph.outgoingEdgesOf(current).stream().filter(e -> !blackList.contains(e))
				                           .collect(Collectors.toSet());
				
				for (final Edge outgoingEdge : outgoingEdges) {
					blackList.add(outgoingEdge);
					if (BranchMarker.BRANCH_PARENT.equals(outgoingEdge.getBranchMarker(branch))) {
						// check whether the target vertex already has an incoming forward edge. then this must be a
						// switch edge
						if (branchGraph.incomingEdgesOf(outgoingEdge.parent).stream()
						               .anyMatch(e -> NavigationMarker.FORWARD.equals(e.getNavigationMarker(branch)))) {
							outgoingEdge.addNavigation(branch, NavigationMarker.SWITCH);
						} else {
							// there can only be one forward edge. if we found it, go deeper that lane
							outgoingEdge.addNavigation(branch, NavigationMarker.FORWARD);
							current = outgoingEdge.parent;
						}
					} else {
						// this is a merge edge, thus it has to be a switch
						Asserts.equalTo(BranchMarker.MERGE_PARENT, outgoingEdge.getBranchMarker(branch));
						outgoingEdge.addNavigation(branch, NavigationMarker.SWITCH);
						pointers.add(outgoingEdge.parent);
					}
					
				}
			} while (!outgoingEdges.isEmpty());
		}
		
		Logger.info("Navigation graph computation duration: "
		        + (Instant.now().getEpochSecond() - start.getEpochSecond()) + "s.");
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
	public Set<Root> getRoots() {
		return this.roots.values().stream().flatMap(set -> set.stream()).collect(Collectors.toSet());
	}
	
	/**
	 * Gets the roots.
	 *
	 * @param branch
	 *            the branch
	 * @return the roots
	 */
	public Set<Root> getRoots(final Branch branch) {
		return UnmodifiableSet.unmodifiableSet(this.roots.get(branch));
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
	public List<ChangeSet> getSwitches(final ChangeSet changeSet,
	                                   final Branch branch) {
		return this.graph.outgoingEdgesOf(changeSet).stream()
		                 .filter(x -> NavigationMarker.SWITCH.equals(x.getNavigationMarker(branch.id())))
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
	 * @see org.mozkito.skeleton.sequel.IEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IEntity#id(long)
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
	
	/**
	 * Update branch edges.
	 *
	 * @param branch
	 *            the branch
	 */
	public void updateBranchEdges(final Branch branch) {
		// final EdgeReversedGraph<ChangeSet, Edge> branchGraph = new EdgeReversedGraph<ChangeSet, Edge>(
		// new DirectedMaskSubgraph<ChangeSet, Edge>(
		// this.graph,
		// new MaskFunctor<ChangeSet, Edge>() {
		//
		// public boolean isEdgeMasked(final Edge edge) {
		// return !edge.inBranch(branch);
		// }
		//
		// public boolean isVertexMasked(final ChangeSet vertex) {
		// return !vertex.getBranchIds()
		// .contains(branch.id());
		// }
		// }));
		// final ChangeSet head = getHead(branch);
		
		// traverse backwards along LANE parents till root and paint FORWARD.
		// slice the trace
		// start at root
		
	}
	
}
