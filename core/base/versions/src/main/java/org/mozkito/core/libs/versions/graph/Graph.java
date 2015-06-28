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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.map.MultiValueMap;
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

import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.core.libs.versions.model.enums.BranchMarker;
import org.mozkito.core.libs.versions.model.enums.IntegrationMarker;
import org.mozkito.core.libs.versions.model.enums.NavigationMarker;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class DepotGraph represents a multi-layer view on the Git repository. Once built, it does not require the
 * repository to be present The graph can be persisted to a {@link Database}.
 *
 * @author Sascha Just
 */
public class Graph implements IEntity {
	
	/**
	 * The Class Pointer.
	 */
	static class Pointer {
		
		/** The head. */
		Vertex        head;
		
		/** The parent. */
		Vertex        parent;
		
		/** The merges. */
		Stack<Vertex> merges = new Stack<>();
		
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
		public Pointer(final Vertex head, final MaskSubgraph<Vertex, Edge> subGraph, final Graph graph) {
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
			builder.append(this.head.getHash());
			builder.append(", parent=");
			builder.append(this.parent != null
			                                  ? this.parent.getHash()
			                                  : "");
			builder.append(", mergeParents=");
			builder.append(this.merges.stream().map(c -> c.getHash()).collect(Collectors.joining(",")));
			return builder.toString();
		}
		
	}
	
	/**
	 * The Class VertexIterator.
	 *
	 * @author Sascha Just
	 */
	public class VertexIterator implements Iterator<Vertex> {
		
		/** The iterator. */
		private final GraphIterator<Vertex, Edge> iterator;
		
		/**
		 * Instantiates a new change set iterator.
		 *
		 * @param graphIterator
		 *            the graph iterator
		 */
		public VertexIterator(final GraphIterator<Vertex, Edge> graphIterator) {
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
		public Vertex next() {
			return this.iterator.next();
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
	private final org.jgrapht.DirectedGraph<Vertex, Edge> graph;
	
	/** The depot. */
	private final long                                    depotId;
	
	/** The id. */
	private long                                          id;
	
	/** The endpoints. */
	private final Map<Reference, Head>                    heads       = new HashMap<>();
	
	/** The roots. */
	private final Map<Reference, Set<Root>>               roots       = new HashMap<>();
	
	/** The vertices. */
	private final Map<String, Vertex>                     vertices    = new HashMap<>();
	
	/** The branch heads. */
	private final MultiValueMap<String, Reference>        references  = new MultiValueMap<>();
	
	/** The convergence. */
	private final List<ConvergenceEdge>                   convergence = new LinkedList<ConvergenceEdge>();
	
	/**
	 * Instantiates a new depot graph.
	 *
	 * @param depot
	 *            the depot
	 */
	public Graph(final Depot depot) {
		Requires.notNull(depot);
		
		this.depotId = depot.getId();
		this.graph = new DefaultDirectedGraph<Vertex, Edge>(Edge.class);
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
	 * @return true, if successful
	 */
	public Edge addEdge(final Vertex parent,
	                    final Vertex child,
	                    final BranchMarker type) {
		Requires.notNull(parent);
		Requires.notNull(child);
		Requires.notNull(type);
		
		final Edge edge = new Edge(this, parent, child, type);
		
		if (!this.graph.containsEdge(edge)) {
			this.graph.addEdge(parent, child, edge);
			return edge;
		}
		
		return null;
	}
	
	/**
	 * Adds the head.
	 *
	 * @param reference
	 *            the branch
	 * @param changeSet
	 *            the change set
	 * @return the head
	 */
	public Head addHead(final Reference reference,
	                    final Vertex changeSet) {
		this.heads.put(reference, new Head(reference.getId(), changeSet.getId()));
		return this.heads.get(reference);
	}
	
	/**
	 * Adds the refs.
	 *
	 * @param references
	 *            the references
	 */
	public void addRefs(final MultiMap<String, Reference> references) {
		this.references.putAll(references);
	}
	
	/**
	 * Adds the root.
	 *
	 * @param reference
	 *            the branch
	 * @param changeSet
	 *            the change set
	 * @return the sets the
	 */
	public Root addRoot(final Reference reference,
	                    final Vertex changeSet) {
		if (!this.roots.containsKey(reference)) {
			this.roots.put(reference, new HashSet<Root>());
		}
		final Root root = new Root(reference.getId(), changeSet.getId());
		this.roots.get(reference).add(root);
		
		return root;
	}
	
	/**
	 * Adds the vertex.
	 *
	 * @param changeSet
	 *            the change set
	 * @return true, if successful
	 */
	public boolean addVertex(final Vertex changeSet) {
		Requires.notNull(changeSet);
		Asserts.notNull(this.graph);
		
		return this.graph.addVertex(changeSet);
	}
	
	/**
	 * Sets the change sets.
	 *
	 * @param vertices
	 *            the change sets
	 */
	public void addVertices(final Map<String, Vertex> vertices) {
		this.vertices.putAll(vertices);
	}
	
	/**
	 * Compute integration points.
	 *
	 * @param reference
	 *            the branch
	 */
	public void computeConvergence(final Reference reference) {
		Requires.notNull(reference);
		final Instant start = Instant.now();
		final List<Vertex> baseLine = new LinkedList<>();
		final Set<Vertex> blackList = new HashSet<>();
		
		// fetch the tier1 graph for that branch (a projection of all vertices and edges in the particular branch
		// space).
		final EdgeReversedGraph<Vertex, Edge> branchGraph = new EdgeReversedGraph<Vertex, Edge>(
		                                                                                        new DirectedMaskSubgraph<Vertex, Edge>(
		                                                                                                                               this.graph,
		                                                                                                                               new MaskFunctor<Vertex, Edge>() {
			                                                                                                                               
			                                                                                                                               public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                               return !edge.inBranch(reference)
				                                                                                                                                       || blackList.contains(edge.parent);
			                                                                                                                               }
			                                                                                                                               
			                                                                                                                               public boolean isVertexMasked(final Vertex vertex) {
				                                                                                                                               return blackList.contains(vertex);
			                                                                                                                               }
		                                                                                                                               }));
		
		final DirectedGraph<Vertex, Edge> baseLineGraph = new EdgeReversedGraph<Vertex, Edge>(
		                                                                                      new DirectedMaskSubgraph<Vertex, Edge>(
		                                                                                                                             this.graph,
		                                                                                                                             new MaskFunctor<Vertex, Edge>() {
			                                                                                                                             
			                                                                                                                             public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                             if (BranchMarker.MERGE_PARENT.equals(edge.getBranchMarker())
				                                                                                                                                     || !edge.inBranch(reference)) {
					                                                                                                                             return true;
				                                                                                                                             }
				                                                                                                                             return false;
			                                                                                                                             }
			                                                                                                                             
			                                                                                                                             public boolean isVertexMasked(final Vertex vertex) {
				                                                                                                                             return false;
			                                                                                                                             }
			                                                                                                                             
		                                                                                                                             }));
		
		final Vertex head = getHead(reference);
		
		final DepthFirstIterator<Vertex, Edge> baseLineIterator = new DepthFirstIterator<Vertex, Edge>(baseLineGraph,
		                                                                                               head);
		
		while (baseLineIterator.hasNext()) {
			final Vertex tmp = baseLineIterator.next();
			baseLine.add(0, tmp);
		}
		
		DepthFirstIterator<Vertex, Edge> revIterator = null;
		Vertex pointer = null;
		
		for (final Vertex current : baseLine) {
			revIterator = new DepthFirstIterator<Vertex, Edge>(branchGraph, current);
			
			while (revIterator.hasNext()) {
				pointer = revIterator.next();
				this.convergence.add(new ConvergenceEdge(reference.getId(), pointer.getId(), current.getId()));
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
	 * @param reference
	 *            the branch
	 */
	public void computeIntegrationGraph(final Reference reference) {
		Requires.notNull(reference);
		final Instant start = Instant.now();
		final List<Vertex> baseLine = new LinkedList<>();
		final Set<Vertex> blackList = new HashSet<Vertex>();
		
		final EdgeReversedGraph<Vertex, Edge> branchGraph = new EdgeReversedGraph<Vertex, Edge>(
		                                                                                        new DirectedMaskSubgraph<Vertex, Edge>(
		                                                                                                                               this.graph,
		                                                                                                                               new MaskFunctor<Vertex, Edge>() {
			                                                                                                                               
			                                                                                                                               public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                               return !edge.inBranch(reference)
				                                                                                                                                       || blackList.contains(edge.parent);
			                                                                                                                               }
			                                                                                                                               
			                                                                                                                               public boolean isVertexMasked(final Vertex vertex) {
				                                                                                                                               return blackList.contains(vertex);
			                                                                                                                               }
		                                                                                                                               }));
		
		final DirectedGraph<Vertex, Edge> baseLane = new EdgeReversedGraph<Vertex, Edge>(
		                                                                                 new DirectedMaskSubgraph<Vertex, Edge>(
		                                                                                                                        this.graph,
		                                                                                                                        new MaskFunctor<Vertex, Edge>() {
			                                                                                                                        
			                                                                                                                        public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                        if (BranchMarker.MERGE_PARENT.equals(edge.getBranchMarker())
				                                                                                                                                || !edge.inBranch(reference)) {
					                                                                                                                        return true;
				                                                                                                                        }
				                                                                                                                        return false;
			                                                                                                                        }
			                                                                                                                        
			                                                                                                                        public boolean isVertexMasked(final Vertex vertex) {
				                                                                                                                        return false;
			                                                                                                                        }
			                                                                                                                        
		                                                                                                                        }));
		
		final Vertex head = getHead(reference);
		final DepthFirstIterator<Vertex, Edge> baseLineIterator = new DepthFirstIterator<Vertex, Edge>(baseLane, head);
		Set<Edge> outgoingEdges;
		List<Vertex> pointers = new LinkedList<>();
		
		while (baseLineIterator.hasNext()) {
			final Vertex tmp = baseLineIterator.next();
			baseLine.add(0, tmp);
		}
		
		for (final Vertex current : baseLine) {
			pointers.add(current);
			
			while (!pointers.isEmpty()) {
				final Vertex pointer = pointers.remove(0);
				
				outgoingEdges = branchGraph.outgoingEdgesOf(pointer);
				
				for (final Edge edge : outgoingEdges) {
					if (!blackList.contains(edge.parent)) { // redundant?
						edge.addIntegration(reference, IntegrationMarker.INTEGRATE);
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
	 * @param reference
	 *            the branch
	 */
	public void computeNavigationGraph(final Reference reference) {
		final Instant start = Instant.now();
		
		final Vertex head = getHead(reference);
		final EdgeReversedGraph<Vertex, Edge> branchGraph = new EdgeReversedGraph<Vertex, Edge>(
		                                                                                        new DirectedMaskSubgraph<Vertex, Edge>(
		                                                                                                                               this.graph,
		                                                                                                                               new MaskFunctor<Vertex, Edge>() {
			                                                                                                                               
			                                                                                                                               public boolean isEdgeMasked(final Edge edge) {
				                                                                                                                               return !edge.inBranch(reference);
			                                                                                                                               }
			                                                                                                                               
			                                                                                                                               public boolean isVertexMasked(final Vertex vertex) {
				                                                                                                                               return false;
			                                                                                                                               }
			                                                                                                                               
		                                                                                                                               }));
		// used for graph traversal for every lane switch
		final List<Vertex> pointers = new LinkedList<>();
		
		// used to store edges we already traversed
		final Set<Edge> blackList = new HashSet<>();
		
		Vertex current;
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
					if (BranchMarker.BRANCH_PARENT.equals(outgoingEdge.getBranchMarker())) {
						// check whether the target vertex already has an incoming forward edge. then this must be a
						// switch edge
						if (branchGraph.incomingEdgesOf(outgoingEdge.parent)
						               .stream()
						               .anyMatch(e -> NavigationMarker.FORWARD.equals(e.getNavigationMarker(reference)))) {
							outgoingEdge.addNavigation(reference, NavigationMarker.SWITCH);
						} else {
							// there can only be one forward edge. if we found it, go deeper that lane
							outgoingEdge.addNavigation(reference, NavigationMarker.FORWARD);
							current = outgoingEdge.parent;
						}
					} else {
						// this is a merge edge, thus it has to be a switch
						Asserts.equalTo(BranchMarker.MERGE_PARENT, outgoingEdge.getBranchMarker());
						outgoingEdge.addNavigation(reference, NavigationMarker.SWITCH);
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
	public Set<Reference> getBranches() {
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
	public Vertex getBranchParent(final Vertex changeSet,
	                              final MaskSubgraph<Vertex, Edge> graph) {
		final Optional<Edge> edge = graph.incomingEdgesOf(changeSet).stream().filter(x -> !x.isMerge()).findFirst();
		if (edge.isPresent()) {
			return edge.get().parent;
		} else {
			return null;
		}
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
	public final long getDepotId() {
		return this.depotId;
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
		return this.graph.edgeSet().parallelStream().filter(e -> e.parent.getId() == from && e.child.getId() == to)
		                 .findAny().get();
	}
	
	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	public Set<Edge> getEdges() {
		return this.graph.edgeSet();
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
	 * In this case, {@link #getFirstCommitToBranch(Reference)} returns C, since this was the first original commit to
	 * this branch and A originates from master.
	 * 
	 * @param reference
	 *            the branch
	 * @return the first commit to branch
	 */
	public Vertex getFirstCommitToBranch(final Reference reference) {
		Requires.notNull(reference);
		
		return null;
	}
	
	/**
	 * Gets the head.
	 *
	 * @param reference
	 *            the branch
	 * @return the head
	 */
	@SuppressWarnings ("unchecked")
	public Vertex getHead(final Reference reference) {
		Requires.notNull(reference);
		for (final Entry<String, Object> entry : this.references.entrySet()) {
			if (((Collection<Reference>) entry.getValue()).contains(reference)) {
				return this.vertices.get(entry.getKey());
			}
		}
		
		throw new IllegalArgumentException("Reference " + reference + " unknown.");
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
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Gets the integration path.
	 *
	 * @param vertex
	 *            the change set
	 * @param reference
	 *            the branch
	 * @return the integration path
	 */
	public List<Vertex> getIntegrationPath(final Vertex vertex,
	                                       final Reference reference) {
		final DirectedMaskSubgraph<Vertex, Edge> integrationGraph = integrationGraph(reference);
		
		final List<Edge> path = DijkstraShortestPath.findPathBetween(integrationGraph, vertex, getHead(reference));
		final List<Vertex> entries = new ArrayList<Vertex>(path.size() + 1);
		
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
	public List<Vertex> getMergeParents(final Vertex changeSet,
	                                    final MaskSubgraph<Vertex, Edge> graph) {
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
	public Set<Edge> getParents(final Vertex changeSet,
	                            final MaskSubgraph<Vertex, Edge> graph) {
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
	 * @param reference
	 *            the branch
	 * @return the roots
	 */
	public Set<Root> getRoots(final Reference reference) {
		return UnmodifiableSet.unmodifiableSet(this.roots.get(reference));
	}
	
	/**
	 * Gets the spin offs.
	 *
	 * @param changeSet
	 *            the change set
	 * @param reference
	 *            the branch
	 * @return the spin offs
	 */
	public List<Vertex> getSwitches(final Vertex changeSet,
	                                final Reference reference) {
		return this.graph.outgoingEdgesOf(changeSet).stream()
		                 .filter(x -> NavigationMarker.SWITCH.equals(x.getNavigationMarker(reference.getId())))
		                 .map(x -> x.parent).collect(Collectors.toList());
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param id
	 *            the id
	 * @return the vertex
	 */
	public Vertex getVertex(final long id) {
		return this.graph.vertexSet().stream().filter(x -> x.getId() == id).findAny().get();
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param hash
	 *            the hash
	 * @return the vertex
	 */
	public Vertex getVertex(final String hash) {
		return this.vertices.get(hash);
	}
	
	/**
	 * Gets the change sets.
	 *
	 * @param reference
	 *            the branch
	 * @return the change sets
	 */
	public Iterator<Vertex> getVertexs(final Reference reference) {
		final DirectedMaskSubgraph<Vertex, Edge> branchGraph = tier1Graph(reference);
		final EdgeReversedGraph<Vertex, Edge> reversedGraph = new EdgeReversedGraph<Vertex, Edge>(branchGraph);
		
		final Vertex head = getHead(reference);
		final DepthFirstIterator<Vertex, Edge> iterator = new DepthFirstIterator<Vertex, Edge>(reversedGraph, head);
		return UnmodifiableIterator.unmodifiableIterator(new VertexIterator(iterator));
		
	}
	
	/**
	 * Integration graph.
	 *
	 * @param reference
	 *            the branch
	 * @return the directed mask subgraph
	 */
	private DirectedMaskSubgraph<Vertex, Edge> integrationGraph(final Reference reference) {
		return new DirectedMaskSubgraph<Vertex, Edge>(this.graph, new MaskFunctor<Vertex, Edge>() {
			
			public boolean isEdgeMasked(final Edge arg0) {
				return !arg0.integrates(reference);
			}
			
			public boolean isVertexMasked(final Vertex arg0) {
				return false;
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
	/**
	 * Skip forwards.
	 *
	 * @param changeSet
	 *            the change set
	 * @return the change set
	 */
	public Vertex skipForwards(final Vertex changeSet) {
		Set<Edge> incomingEdges = this.graph.incomingEdgesOf(changeSet);
		
		while (incomingEdges.size() == 1) {
			incomingEdges = this.graph.incomingEdgesOf(incomingEdges.iterator().next().parent);
		}
		
		return incomingEdges.iterator().next().child;
	}
	
	/**
	 * Gets the branch graph.
	 *
	 * @param reference
	 *            the branch
	 * @return the branch graph
	 */
	private DirectedMaskSubgraph<Vertex, Edge> tier1Graph(final Reference reference) {
		return new DirectedMaskSubgraph<Vertex, Edge>(this.graph, new MaskFunctor<Vertex, Edge>() {
			
			public boolean isEdgeMasked(final Edge arg0) {
				return !arg0.inBranch(reference);
			}
			
			public boolean isVertexMasked(final Vertex arg0) {
				return false;
			}
		});
	}
	
	/**
	 * Update branch edges.
	 *
	 * @param reference
	 *            the branch
	 */
	public void updateBranchEdges(final Reference reference) {
		// final EdgeReversedGraph<Vertex, Edge> branchGraph = new EdgeReversedGraph<Vertex, Edge>(
		// new DirectedMaskSubgraph<Vertex, Edge>(
		// this.graph,
		// new MaskFunctor<Vertex, Edge>() {
		//
		// public boolean isEdgeMasked(final Edge edge) {
		// return !edge.inBranch(branch);
		// }
		//
		// public boolean isVertexMasked(final Vertex vertex) {
		// return !vertex.getBranchIds()
		// .contains(branch.id());
		// }
		// }));
		// final Vertex head = getHead(branch);
		
		// traverse backwards along LANE parents till root and paint FORWARD.
		// slice the trace
		// start at root
		
	}
	
}
