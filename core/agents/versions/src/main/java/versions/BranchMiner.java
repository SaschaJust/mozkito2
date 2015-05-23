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

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.adapters.BranchAdapter;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.datastructures.Tuple;
import org.mozkito.skeleton.exec.CommandExecutor;
import org.mozkito.skeleton.sequel.SequelDatabase;

// TODO: Auto-generated Javadoc
/**
 * The Class BranchMiner.
 *
 * @author Sascha Just
 */
public class BranchMiner implements Runnable {
	
	/** The Constant TAG. */
	private static final String       TAG              = "refs/heads/";
	
	/** The clone dir. */
	private final File                cloneDir;
	
	/** The database. */
	private final SequelDatabase      database;
	
	/** The depot. */
	private final Depot               depot;
	
	/** The branch head hashes. */
	private final Map<String, Branch> branchHeadHashes = new HashMap<String, Branch>();
	
	/**
	 * Instantiates a new branch miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param database
	 *            the database
	 * @param depot
	 *            the depot
	 */
	public BranchMiner(final File cloneDir, final SequelDatabase database, final Depot depot) {
		this.cloneDir = cloneDir;
		this.database = database;
		this.depot = depot;
	}
	
	/**
	 * Gets the branc heads.
	 *
	 * @return the branc heads
	 */
	public Map<String, Branch> getBrancHeads() {
		return UnmodifiableMap.unmodifiableMap(this.branchHeadHashes);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			final BranchAdapter branchAdapter = new BranchAdapter(this.database);
			branchAdapter.createScheme();
			
			final Tuple<Integer, List<String>> result = CommandExecutor.execute("git", new String[] { "ls-remote",
			        "--heads" }, this.cloneDir, null, new HashMap<String, String>());
			for (final String line : result.getSecond()) {
				final String headHash = line.substring(0, 40);
				String branchName = line.substring(40).trim();
				Contract.asserts(branchName.startsWith(TAG));
				branchName = branchName.substring(TAG.length());
				
				final Branch branch = new Branch(this.depot, branchName);
				branchAdapter.save(branch);
				this.branchHeadHashes.put(headHash, branch);
			}
			
			this.database.commit();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
