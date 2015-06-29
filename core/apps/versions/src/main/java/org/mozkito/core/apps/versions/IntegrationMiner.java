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

import java.util.Collection;
import java.util.Map.Entry;

import org.mozkito.core.libs.versions.graph.Edge;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Label;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.libraries.sequel.DatabaseDumper;

/**
 * The Class IntegrationMiner.
 *
 * @author Sascha Just
 */
public class IntegrationMiner extends Task implements Runnable {
	
	static final String                      ORIGIN = "origin/";
	
	/** The integration type dumper. */
	private final DatabaseDumper<BranchEdge> branchEdgeDumper;
	
	private final Graph                      graph;
	
	/**
	 * Instantiates a new integration miner.
	 *
	 * @param depot
	 *            the depot
	 * @param graph
	 *            the graph
	 * @param branchEdgeDumper
	 *            the branch edge dumper
	 */
	public IntegrationMiner(final Depot depot, final Graph graph, final DatabaseDumper<BranchEdge> branchEdgeDumper) {
		super(depot);
		this.graph = graph;
		this.branchEdgeDumper = branchEdgeDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Reference reference : this.graph.getReferences()) {
			this.graph.computeNavigationGraph(reference);
			this.graph.computeIntegrationGraph(reference);
		}
		
		BranchEdge bEdge;
		
		final Collection<Edge> edges = this.graph.getEdges();
		
		Label label;
		for (final Edge edge : edges) {
			for (final Entry<Long, Label> entry : edge.getLabels().entrySet()) {
				label = entry.getValue();
				bEdge = new BranchEdge(edge.getId(), entry.getKey(), label.navigationMarker, label.integrationMarker);
				this.branchEdgeDumper.saveLater(bEdge);
			}
		}
		
	}
	
}
