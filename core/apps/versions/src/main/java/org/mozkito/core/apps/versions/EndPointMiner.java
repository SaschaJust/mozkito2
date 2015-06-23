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
import java.util.Map.Entry;

import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;

/**
 * The Class EndPointMiner.
 *
 * @author Sascha Just
 */
public class EndPointMiner extends Task implements Runnable {
	
	/** The clone dir. */
	private final File                   cloneDir;
	
	/** The branch heads. */
	private final Map<String, Branch>    branchHeads;
	
	/** The change sets. */
	private final Map<String, ChangeSet> changeSets;
	
	/** The graph. */
	private final Graph                  graph;
	
	/** The head dumper. */
	private final DatabaseDumper<Head>   headDumper;
	
	/** The root dumper. */
	private final DatabaseDumper<Root>   rootDumper;
	
	/**
	 * Instantiates a new end point miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param heads
	 *            the heads
	 * @param changeSets
	 *            the change sets
	 * @param graph
	 *            the graph
	 * @param headDumper
	 *            the head dumper
	 * @param rootDumper
	 *            the root dumper
	 */
	public EndPointMiner(final File cloneDir, final Map<String, Branch> heads, final Map<String, ChangeSet> changeSets,
	        final Graph graph, final DatabaseDumper<Head> headDumper, final DatabaseDumper<Root> rootDumper) {
		super(graph.getDepot());
		this.cloneDir = cloneDir;
		this.branchHeads = heads;
		this.changeSets = changeSets;
		this.graph = graph;
		this.headDumper = headDumper;
		this.rootDumper = rootDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Entry<String, Branch> entry : this.branchHeads.entrySet()) {
			final Command command = Command.execute("git", new String[] { "log", "--max-parents=0", "--format=%H",
			        GraphMiner.ORIGIN + entry.getValue().getName() }, this.cloneDir);
			String root;
			
			Asserts.containsKey(this.changeSets, entry.getKey());
			this.headDumper.saveLater(this.graph.addHead(entry.getValue(), this.changeSets.get(entry.getKey())));
			
			while ((root = command.nextOutput()) != null) {
				Asserts.containsKey(this.changeSets, root);
				this.rootDumper.saveLater(this.graph.addRoot(entry.getValue(), this.changeSets.get(root)));
			}
		}
	}
	
}
