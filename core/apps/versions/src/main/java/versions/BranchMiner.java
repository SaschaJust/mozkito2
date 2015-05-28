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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.BranchHead;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The BranchMiner is used to collect all branches known to the underlying depot. The {@link Branch}es are stored in the
 * provided {@link SequelDatabase}, along with their {@link BranchHead}.
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
		Asserts.notNull(this.database);
		final ISequelAdapter<Branch> branchAdapter = this.database.getAdapter(Branch.class);
		
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
			branchAdapter.save(branch);
			
			Asserts.notNull(this.branchHeadHashes);
			this.branchHeadHashes.put(headHash, branch);
		}
		
		this.database.commit();
	}
	
}
