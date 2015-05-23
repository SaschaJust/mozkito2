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

package versions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.DepotGraph.EdgeType;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.datastructures.Tuple;
import org.mozkito.skeleton.exec.CommandExecutor;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * @author Sascha Just
 *
 */
public class GraphMiner implements Runnable {
	
	private final File                   cloneDir;
	private final SequelDatabase         database;
	private final DepotGraph             graph;
	private final Map<String, ChangeSet> changeSets;
	
	/**
	 * Instantiates a new graph miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param database
	 *            the database
	 * @param graph
	 *            the graph
	 * @param changeSets
	 */
	public GraphMiner(final File cloneDir, final SequelDatabase database, final DepotGraph graph,
	        final Map<String, ChangeSet> changeSets) {
		this.cloneDir = cloneDir;
		this.database = database;
		this.graph = graph;
		this.changeSets = changeSets;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			for (final Branch branch : this.graph.getBranches()) {
				final Tuple<Integer, List<String>> result = CommandExecutor.execute("git", new String[] { "rev-list",
				                                                                            "--branches=" + branch,
				                                                                            "--remotes", "--parents" },
				                                                                    this.cloneDir, null,
				                                                                    new HashMap<String, String>());
				for (final String line : result.getSecond()) {
					final String[] split = line.trim().split("\\s+");
					
					if (split.length == 1) {
						// found root
					} else {
						Asserts.greater(split.length, 1, "There has to be a parent at this point.");
						
						Asserts.containsKey(this.changeSets,
						                    split[0],
						                    "ChangeSet '%s' is not known to the graph and hasn't been seen during mining.",
						                    split[0]);
						Asserts.containsKey(this.changeSets,
						                    split[1],
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
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
