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

import java.io.File;
import java.util.Map;

import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.IDumper;
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
	private final IDumper<Reference>                referenceDumper;
	
	/** The vertices. */
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
	        final IDumper<Reference> referenceDumper) {
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
			
			if (head == null) {
				Logger.warn("Illegal branch reference. The commit %s referenced by %s is not known to git.", headHash,
				            branchName);
				continue;
			}
			
			Asserts.notNull(head, "Could not find commit %s referenced by %s.", headHash, branchName);
			final Branch branch = new Branch(this.depot, branchName, head.getId());
			this.referenceDumper.saveLater(branch);
			
			Asserts.notNull(this.branchHeadHashes);
			this.branchHeadHashes.put(headHash, branch);
		}
	}
	
}
