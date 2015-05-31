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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Endpoint;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The BranchMiner is used to collect all branches known to the underlying depot. The {@link Branch}es are stored in the
 * provided {@link SequelDatabase}, along with their {@link Endpoint}.
 *
 * @author Sascha Just
 */
public class BranchMiner implements Runnable {
	
	/** The Constant TAG. */
	private static final String          TAG              = "refs/heads/";
	
	/** The clone dir. */
	private final File                   cloneDir;
	
	/** The depot. */
	private final Depot                  depot;
	
	/** The branch head hashes. */
	private final Map<String, Branch>    branchHeadHashes = new HashMap<String, Branch>();
	
	/** The branch dumper. */
	private final DatabaseDumper<Branch> branchDumper;
	
	/**
	 * Instantiates a new branch miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param depot
	 *            the depot
	 * @param branchDumper
	 *            the branch dumper
	 */
	public BranchMiner(final File cloneDir, final Depot depot, final DatabaseDumper<Branch> branchDumper) {
		this.cloneDir = cloneDir;
		this.depot = depot;
		this.branchDumper = branchDumper;
	}
	
	/**
	 * Gets the branch heads.
	 *
	 * @return the branch heads
	 */
	public Map<String, Branch> getBranchHeads() {
		return UnmodifiableMap.unmodifiableMap(this.branchHeadHashes);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		final Command command = Command.execute("git", new String[] { "ls-remote", "--heads" }, this.cloneDir);
		
		String line;
		
		RESULTS: while ((line = command.nextOutput()) != null) {
			if (line.startsWith("From ")) {
				continue RESULTS;
			}
			
			final String headHash = line.substring(0, 40);
			String branchName = line.substring(40).trim();
			Contract.asserts(branchName.startsWith(TAG));
			branchName = branchName.substring(TAG.length());
			
			final Branch branch = new Branch(this.depot, branchName);
			this.branchDumper.saveLater(branch);
			
			Asserts.notNull(this.branchHeadHashes);
			this.branchHeadHashes.put(headHash, branch);
		}
	}
	
}
