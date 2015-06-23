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

import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.DatabaseDumper;

/**
 * @author Sascha Just
 *
 */
public class ConvergenceMiner extends Task implements Runnable {
	
	private final Graph                           graph;
	private final DatabaseDumper<ConvergenceEdge> convergenceDumper;
	
	/**
	 * Instantiates a new integration miner.
	 *
	 * @param graph
	 *            the graph
	 * @param convergenceDumper
	 *            the convergence dumper
	 */
	public ConvergenceMiner(final Graph graph, final DatabaseDumper<ConvergenceEdge> convergenceDumper) {
		super(graph.getDepot());
		this.graph = graph;
		this.convergenceDumper = convergenceDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Branch branch : this.graph.getBranches()) {
			Logger.info("Processing branch '%s'.", branch.getName());
			this.graph.computeConvergence(branch);
		}
		
		for (final ConvergenceEdge edge : this.graph.getConvergence()) {
			this.convergenceDumper.saveLater(edge);
		}
	}
}
