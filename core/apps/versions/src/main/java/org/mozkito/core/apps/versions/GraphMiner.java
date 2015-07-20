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

package org.mozkito.core.apps.versions;

import java.io.File;
import java.util.Map;

import org.mozkito.core.libs.versions.graph.Edge;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.ChangeSetType;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.enums.BranchMarker;
import org.mozkito.core.libs.versions.model.enums.IntegrationType;
import org.mozkito.core.libs.versions.model.enums.ReferenceType;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;

/**
 * The Class GraphMiner.
 *
 * @author Sascha Just
 */
public class GraphMiner extends Task implements Runnable {
	
	/** The clone dir. */
	private final File                                 cloneDir;
	
	/** The graph. */
	private final Graph                                graph;
	
	/** The change sets. */
	private final Map<String, Vertex>                  vertices;
	
	/** The graph dumper. */
	private final IDumper<Graph>                graphDumper;
	
	/** The graph edge dumper. */
	private final IDumper<GraphEdge>            graphEdgeDumper;
	
	/** The integration dumper. */
	private final IDumper<ChangeSetType> integrationDumper;
	
	/**
	 * Instantiates a new graph miner.
	 *
	 * @param depot
	 *            the depot
	 * @param cloneDir
	 *            the clone dir
	 * @param graph
	 *            the graph
	 * @param vertices
	 *            the vertices
	 * @param graphDumper
	 *            the graph dumper
	 * @param graphEdgeDumper
	 *            the graph edge dumper
	 * @param integrationDumper
	 *            the integration dumper
	 */
	public GraphMiner(final Depot depot, final File cloneDir, final Graph graph, final Map<String, Vertex> vertices,
	        final IDumper<Graph> graphDumper, final IDumper<GraphEdge> graphEdgeDumper,
	        final IDumper<ChangeSetType> integrationDumper) {
		super(depot);
		this.cloneDir = cloneDir;
		this.graph = graph;
		this.vertices = vertices;
		this.graphDumper = graphDumper;
		this.graphEdgeDumper = graphEdgeDumper;
		this.integrationDumper = integrationDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		Vertex current;
		ChangeSetType csi;
		String line, currentHash, parentHash;
		int length;
		Edge edge;
		GraphEdge gEdge;
		
		for (final Reference reference : this.graph.getReferences()) {
			
			final Command command = Command.execute("git",
			                                        new String[] {
			                                                "log",
			                                                "--no-abbrev",
			                                                "--format=%H %P",
			                                                ReferenceType.BRANCH.equals(reference.getType())
			                                                                                                ? IntegrationMiner.ORIGIN
			                                                                                                        + reference.getName()
			                                                                                                : TagMiner.PREFIX
			                                                                                                        + reference.getName() },
			                                        this.cloneDir);
			
			while ((line = command.nextOutput()) != null) {
				line = line.trim();
				length = line.length();
				Asserts.greater(length, 39, "There has to be at least one hash in here.");
				currentHash = line.substring(0, 40);
				current = this.vertices.get(currentHash);
				Asserts.notNull(current);
				
				if (line.length() == 40) {
					// found root
				} else {
					Asserts.greater(length, 80, "There has to be a parent at this point.");
					Asserts.containsKey(this.vertices, currentHash,
					                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
					                    currentHash);
					parentHash = line.substring(41, 81);
					Asserts.containsKey(this.vertices, parentHash,
					                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
					                    parentHash);
					
					edge = this.graph.addEdge(this.vertices.get(parentHash), current, BranchMarker.BRANCH_PARENT,
					                          reference);
					if (edge != null) {
						gEdge = new GraphEdge(this.graph.getDepotId(), edge.getSourceId(), edge.getTargetId(),
						                      edge.getBranchMarker().getValue());
						this.graphEdgeDumper.saveLater(gEdge);
						edge.setId(gEdge.getId());
						
						if (line.length() >= 122) {
							csi = new ChangeSetType(current.getId(), IntegrationType.MERGE);
							
						} else {
							csi = new ChangeSetType(current.getId(), IntegrationType.EDIT);
						}
						this.integrationDumper.saveLater(csi);
					}
					
					// hash(40) + space + hash(40) + space + hash(40)
					for (int i = 3; i <= (line.length() + 1) / 41; ++i) {
						parentHash = line.substring((i - 1) * 41, i * 41 - 1);
						Asserts.containsKey(this.vertices,
						                    parentHash,
						                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
						                    parentHash);
						edge = this.graph.addEdge(this.vertices.get(parentHash), current, BranchMarker.MERGE_PARENT,
						                          reference);
						if (edge != null) {
							
							gEdge = new GraphEdge(this.graph.getDepotId(), edge.getSourceId(), edge.getTargetId(),
							                      edge.getBranchMarker().getValue());
							this.graphEdgeDumper.saveLater(gEdge);
							edge.setId(gEdge.getId());
						}
					}
				}
			}
			
		}
		this.graphDumper.saveLater(this.graph);
	}
}
