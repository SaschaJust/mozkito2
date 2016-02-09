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

import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.model.Convergence;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.IDumper;

/**
 * @author Sascha Just
 *
 */
public class ConvergenceMiner extends Task implements Runnable {
	
	private final Graph                           graph;
	private final IDumper<Convergence> convergenceDumper;
	
	/**
	 * Instantiates a new integration miner.
	 *
	 * @param depot
	 *            the depot
	 * @param graph
	 *            the graph
	 * @param convergenceDumper
	 *            the convergence dumper
	 */
	public ConvergenceMiner(final Depot depot, final Graph graph,
	        final IDumper<Convergence> convergenceDumper) {
		super(depot);
		this.graph = graph;
		this.convergenceDumper = convergenceDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Reference reference : this.graph.getReferences()) {
			Logger.info("Processing branch '%s'.", reference.getName());
			this.graph.computeConvergence(reference);
		}
		
		for (final Convergence edge : this.graph.getConvergence()) {
			this.convergenceDumper.saveLater(edge);
		}
	}
}
