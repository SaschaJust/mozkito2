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

import org.mozkito.core.libs.versions.Graph;
import org.mozkito.core.libs.versions.Graph.EdgeType;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;

/**
 * The Class GraphMiner.
 *
 * @author Sascha Just
 */
public class GraphMiner implements Runnable {
	
	static final String                  ORIGIN = "origin/";
	
	/** The clone dir. */
	private final File                   cloneDir;
	
	/** The graph. */
	private final Graph                  graph;
	
	/** The change sets. */
	private final Map<String, ChangeSet> changeSets;
	
	/**
	 * Instantiates a new graph miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param graph
	 *            the graph
	 * @param changeSets
	 *            the change sets
	 */
	public GraphMiner(final File cloneDir, final Graph graph, final Map<String, ChangeSet> changeSets) {
		this.cloneDir = cloneDir;
		this.graph = graph;
		this.changeSets = changeSets;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Branch branch : this.graph.getBranches()) {
			
			final Command command = Command.execute("git", new String[] { "log", "--no-abbrev", "--format=%H %P",
			        ORIGIN + branch.getName() }, this.cloneDir);
			
			String line;
			while ((line = command.nextOutput()) != null) {
				final String[] split = line.trim().split("\\s+");
				
				this.changeSets.get(split[0]).addBranchId(branch.id());
				
				if (split.length == 1) {
					// found root
				} else {
					Asserts.greater(split.length, 1, "There has to be a parent at this point.");
					
					Asserts.containsKey(this.changeSets, split[0],
					                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
					                    split[0]);
					Asserts.containsKey(this.changeSets, split[1],
					                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
					                    split[1]);
					
					this.graph.addEdge(this.changeSets.get(split[1]), this.changeSets.get(split[0]),
					                   split.length == 2
					                                    ? EdgeType.FORWARD
					                                    : EdgeType.BRANCH, branch);
					
					for (int i = 2; i < split.length; ++i) {
						this.graph.addEdge(this.changeSets.get(split[i]), this.changeSets.get(split[0]),
						                   EdgeType.MERGE, branch);
					}
				}
			}
		}
	}
}
