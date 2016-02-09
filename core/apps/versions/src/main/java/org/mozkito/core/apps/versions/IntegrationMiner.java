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
 
package org.mozkito.core.apps.versions;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import org.mozkito.core.libs.versions.graph.Edge;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Label;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.libraries.sequel.IDumper;

/**
 * The Class IntegrationMiner.
 *
 * @author Sascha Just
 */
public class IntegrationMiner extends Task implements Runnable {
	
	static final String                      ORIGIN = "origin/";
	
	/** The integration type dumper. */
	private final IDumper<BranchEdge> branchEdgeDumper;
	
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
	public IntegrationMiner(final Depot depot, final Graph graph, final IDumper<BranchEdge> branchEdgeDumper) {
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
				
				try {
					bEdge = new BranchEdge(edge.getId(), entry.getKey(), label.navigationMarker, label.integrationMarker);
					this.branchEdgeDumper.saveLater(bEdge);
				} catch (RuntimeException e) {
					Optional<Reference> oRef = graph.getReferences().stream().filter(x -> entry.getKey() == x.getId()).findFirst();
					if (!oRef.isPresent()) {
						throw new RuntimeException(String.format("Invalid reference id '%s'. Your data is corrupted.", entry.getKey()), e);
					} else {
						throw new RuntimeException(String.format("Can not create branch edge '%s' using navigation marker '%s' and integration marker '%s'.", 
						edge.toString(oRef.get()), label.navigationMarker, label.integrationMarker), e);
					}
				}
			}
		}
		
	}
	
}
