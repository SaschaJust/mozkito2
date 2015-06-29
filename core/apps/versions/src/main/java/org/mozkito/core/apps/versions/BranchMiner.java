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

import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.DatabaseDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.datastructures.BidirectionalMultiMap;
import org.mozkito.skeleton.exec.Command;

/**
 * The BranchMiner is used to collect all branches known to the underlying depot. The {@link Reference}es are stored in
 * the provided {@link Database}, along with their {@link Head} and {@link Root}s.
 *
 * @author Sascha Just
 */
public class BranchMiner extends Task implements Runnable {
	
	/** The Constant TAG. */
	private static final String                            TAG              = "refs/heads/";
	
	/** The clone dir. */
	private final File                                     cloneDir;
	
	/** The branch head hashes. */
	private final BidirectionalMultiMap<String, Reference> branchHeadHashes = new BidirectionalMultiMap<>();
	
	/** The branch dumper. */
	private final DatabaseDumper<Reference>                referenceDumper;
	
	private final Map<String, Vertex>                      vertices;
	
	/**
	 * Instantiates a new branch miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param depot
	 *            the depot
	 * @param changeSets
	 *            the change sets
	 * @param referenceDumper
	 *            the branch dumper
	 */
	public BranchMiner(final File cloneDir, final Depot depot, final Map<String, Vertex> changeSets,
	        final DatabaseDumper<Reference> referenceDumper) {
		super(depot);
		this.cloneDir = cloneDir;
		this.referenceDumper = referenceDumper;
		this.vertices = changeSets;
	}
	
	/**
	 * Gets the branch heads.
	 *
	 * @return the branch heads
	 */
	public BidirectionalMultiMap<String, Reference> getBranchRefs() {
		return this.branchHeadHashes;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		final Command command = Command.execute("git", new String[] { "ls-remote", "--heads" }, this.cloneDir);
		
		String line;
		Vertex head;
		
		RESULTS: while ((line = command.nextOutput()) != null) {
			if (line.startsWith("From ")) {
				continue RESULTS;
			}
			
			final String headHash = line.substring(0, 40);
			String branchName = line.substring(40).trim();
			Contract.asserts(branchName.startsWith(TAG));
			branchName = branchName.substring(TAG.length());
			head = this.vertices.get(headHash);
			
			final Branch branch = new Branch(this.depot, branchName, head.getId());
			this.referenceDumper.saveLater(branch);
			
			Asserts.notNull(this.branchHeadHashes);
			this.branchHeadHashes.put(headHash, branch);
		}
	}
	
}
