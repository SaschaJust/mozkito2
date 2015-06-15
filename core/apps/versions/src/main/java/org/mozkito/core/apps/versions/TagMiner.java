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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.IdentityCache;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.core.libs.versions.model.Roots;
import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The BranchMiner is used to collect all branches known to the underlying depot. The {@link Branch}es are stored in the
 * provided {@link SequelDatabase}, along with their {@link Head} and {@link Roots}.
 *
 * @author Sascha Just
 */
public class TagMiner implements Runnable {
	
	/** The Constant TAG. */
	private static final String          TAG     = "refs/tags/";
	private static final String          POINTER = "^{}";
	
	/** The clone dir. */
	private final File                   cloneDir;
	
	/** The depot. */
	private final Depot                  depot;
	
	/** The branch head hashes. */
	private final Map<String, Tag>       tags    = new HashMap<String, Tag>();
	
	/** The branch dumper. */
	private final DatabaseDumper<Tag>    tagDumper;
	private final IdentityCache          identityCache;
	private final Map<String, ChangeSet> changeSets;
	
	/**
	 * Instantiates a new branch miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param depot
	 *            the depot
	 * @param changeSets
	 *            the change sets
	 * @param identityCache
	 *            the identity cache
	 * @param tagDumper
	 *            the tag dumper
	 */
	public TagMiner(final File cloneDir, final Depot depot, final Map<String, ChangeSet> changeSets,
	        final IdentityCache identityCache, final DatabaseDumper<Tag> tagDumper) {
		this.cloneDir = cloneDir;
		this.depot = depot;
		this.changeSets = changeSets;
		this.identityCache = identityCache;
		this.tagDumper = tagDumper;
	}
	
	/**
	 * Gets the branch heads.
	 *
	 * @return the branch heads
	 */
	public Map<String, Tag> getBranchHeads() {
		return UnmodifiableMap.unmodifiableMap(this.tags);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		final Command command = Command.execute("git", new String[] { "ls-remote", "--tags" }, this.cloneDir);
		Command command2;
		
		String line, line2, substr;
		Tag tag;
		String targetHash;
		String name, email, offset;
		StringBuilder messageBuilder;
		Identity identity;
		Instant timestamp;
		
		RESULTS: while ((line = command.nextOutput()) != null) {
			if (line.startsWith("From ")) {
				continue RESULTS;
			}
			
			final String headHash = line.substring(0, 40);
			String tagName = line.substring(40).trim();
			Contract.asserts(tagName.startsWith(TAG));
			tagName = tagName.substring(TAG.length());
			
			if (tagName.endsWith(POINTER)) {
				// previous entry was a tag object
				tagName = tagName.substring(0, tagName.length() - POINTER.length());
				Asserts.containsKey(this.tags, tagName);
				// nothing to do
			} else {
				command2 = Command.execute("git", new String[] { "cat-file", "-p", headHash }, this.cloneDir);
				line2 = command2.nextOutput();
				substr = line2.substring(0, 6);
				if ("object".equals(substr)) {
					// tag object
					targetHash = line2.substring(7);
					
					line2 = command2.nextOutput();
					// skip: type commmit
					line2 = command2.nextOutput();
					// skip: tag NAME
					line2 = command2.nextOutput();
					
					String[] split = line2.substring(7).split("[<>]");
					name = split[0].trim();
					email = split[1].trim();
					identity = this.identityCache.request(email, name);
					split = split[2].trim().split("\\s");
					timestamp = Instant.ofEpochSecond(Long.parseLong(split[0]));
					offset = split[1].trim();
					if ("-".equals(offset.substring(0, 1))) {
						timestamp.plusSeconds(3600 * Integer.parseInt(offset.substring(1, 3))
						        + Integer.parseInt(offset.substring(3)));
					} else {
						timestamp.minusSeconds(3600 * Integer.parseInt(offset.substring(1, 3))
						        + Integer.parseInt(offset.substring(3)));
					}
					
					command2.nextOutput(); // skip empty line
					messageBuilder = new StringBuilder();
					
					while ((line2 = command2.nextOutput()) != null) {
						messageBuilder.append(line2);
					}
					tag = new Tag(this.depot, this.changeSets.get(targetHash), tagName, headHash,
					              messageBuilder.toString(), identity, timestamp);
				} else {
					Asserts.equalTo("tree", substr.substring(0, 4));
					// commit object
					Asserts.containsKey(this.changeSets, headHash);
					tag = new Tag(this.depot, this.changeSets.get(headHash), tagName);
				}
				this.tagDumper.saveLater(tag);
				Asserts.notNull(this.tags);
				this.tags.put(tagName, tag);
			}
		}
	}
	
}
