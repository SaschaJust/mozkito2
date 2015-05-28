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
import java.util.Map;
import java.util.Map.Entry;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Endpoint;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class EndPointMiner.
 *
 * @author Sascha Just
 */
public class EndPointMiner implements Runnable {
	
	/** The clone dir. */
	private final File                     cloneDir;
	
	/** The branch heads. */
	private final Map<String, Branch>      branchHeads;
	
	/** The change sets. */
	private final Map<String, ChangeSet>   changeSets;
	
	/** The dumper. */
	private final DatabaseDumper<Endpoint> dumper;
	
	/** The depot. */
	private final Depot                    depot;
	
	private final DepotGraph               graph;
	
	/**
	 * Instantiates a new end point miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param database
	 *            the database
	 * @param depot
	 *            the depot
	 * @param heads
	 *            the heads
	 * @param changeSets
	 *            the change sets
	 * @param graph
	 *            the graph
	 */
	public EndPointMiner(final File cloneDir, final SequelDatabase database, final Depot depot,
	        final Map<String, Branch> heads, final Map<String, ChangeSet> changeSets, final DepotGraph graph) {
		this.cloneDir = cloneDir;
		this.branchHeads = heads;
		this.changeSets = changeSets;
		this.depot = depot;
		this.graph = graph;
		this.dumper = new DatabaseDumper<Endpoint>(database.getAdapter(Endpoint.class));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final Entry<String, Branch> entry : this.branchHeads.entrySet()) {
			final Command command = Command.execute("git", new String[] { "ls-remote", "--heads" }, this.cloneDir);
			final String root = command.nextOutput();
			this.dumper.saveLater(new Endpoint(this.depot, entry.getValue(), this.changeSets.get(entry.getKey()),
			                                   this.changeSets.get(root)));
			this.graph.addEndPoint(entry.getValue(),
			                       new Endpoint(this.depot, entry.getValue(), this.changeSets.get(entry.getKey()),
			                                    this.changeSets.get(root)));
		}
	}
	
}
