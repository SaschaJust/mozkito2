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
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;

/**
 * The TagMiner is used to collect all tags known to the underlying depot. There are two types of tags. Lightweight tags
 * and tag objects.
 *
 * There format for lightwights will be:
 * 
 * <pre>
 * >>>#$@#$@>>>
 * commit
 * 5dd2b0dafbd2c9c3284d93d981044e7cdc33a7c1
 * 
 * 
 * 
 * 
 * 
 * 
 * improved LogIterator to call log only when necessary
 * 
 * <<<#$@#$@<<<
 * </pre>
 * 
 * There format for objects will be: >>>#$@#$@>>> tag 0367e364911aefd27cd71f0192bc222a66c21547
 * 36aede6eaa0d051bd4a813dfbb954b7ac7ebac1e commit test Sascha Just <sascha.just@own-hero.net> 1434313830 +0100 testing
 * stuff
 * 
 * <<<#$@#$@<<< </pre>
 *
 * @author Sascha Just
 */
public class TagMiner extends Task implements Runnable {
	
	/** The Constant TAG. */
	private static final String          TAG       = "refs/tags/";
	
	/** The Constant END_TAG. */
	private static final String          END_TAG   = "<<<#$@#$@<<<";
	
	/** The Constant START_TAG. */
	private static final String          START_TAG = ">>>#$@#$@>>>";
	
	/** The Constant LS. */
	private static final String          LS        = System.lineSeparator();
	
	/** The clone dir. */
	private final File                   cloneDir;
	
	/** The branch head hashes. */
	private final Map<String, Tag>       tags      = new HashMap<String, Tag>();
	
	/** The branch dumper. */
	private final DatabaseDumper<Tag>    tagDumper;
	
	/** The identity cache. */
	private final IdentityCache          identityCache;
	
	/** The change sets. */
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
		super(depot);
		this.cloneDir = cloneDir;
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
		final Command command = Command.execute("git", new String[] {
		        "for-each-ref",
		        "--format=" + START_TAG + LS + "%(refname)" + LS + "%(objecttype)" + LS + "%(objectname)" + LS
		                + "%(object)" + LS + "%(type)" + LS + "%(tag)" + LS + "%(taggername)" + LS + "%(taggeremail)"
		                + LS + "%(taggerdate:raw)" + LS + "%(contents:subject)" + LS + "%(contents:body)" + LS
		                + END_TAG, "refs/tags" }, this.cloneDir);
		
		String line;
		Tag tag;
		String targetHash;
		String tagName, tagType, tagHash;
		String name, email;
		StringBuilder messageBuilder;
		Instant timestamp;
		
		RESULTS: while ((line = command.nextOutput()) != null) {
			Asserts.equalTo(START_TAG, line);
			line = command.nextOutput();
			tagName = line.substring(TAG.length());
			line = command.nextOutput();
			tagType = line;
			line = command.nextOutput();
			
			switch (tagType) {
				case "commit":
					targetHash = line;
					tag = new Tag(this.depot, this.changeSets.get(targetHash), tagName);
					
					this.tagDumper.saveLater(tag);
					this.tags.put(tagName, tag);
					while ((line = command.nextOutput()) != null && !END_TAG.equals(line)) {
						// skip
					}
					continue RESULTS;
				case "tag":
					tagHash = line;
					line = command.nextOutput();
					targetHash = line;
					line = command.nextOutput();
					if (!"commit".equals(line)) {
						// skip non commit tags
						while ((line = command.nextOutput()) != null && !END_TAG.equals(line)) {
							// keep going
						}
						continue RESULTS;
					}
					line = command.nextOutput();
					
					// this might not hold
					// @formatter:off
					
					/* 
					 * refs/tags/chromeos-3.18-experiment
                     * tag
                     * 5a4a5d52f29d08d923ce8d232b0b497da674dd2c
                     * b2776bf7149bddd1f4161f14f79520f17fc1d71d
                     * commit
                     * v3.18
                     * Linus Torvalds
                     * <torvalds@linux-foundation.org>
                     * 1417990873 -0800
                     * Linux 3.18
                     * 
					 */
					
					// @formatter:on
					// Asserts.equalTo(tagName, line);
					line = command.nextOutput();
					name = line;
					line = command.nextOutput();
					email = line.length() > 2
					                         ? line.substring(1, line.length() - 1)
					                         : "";
					line = command.nextOutput();
					timestamp = Instant.ofEpochSecond(Long.parseLong(line.substring(0, line.indexOf(' '))));
					messageBuilder = new StringBuilder();
					while ((line = command.nextOutput()) != null && !END_TAG.equals(line)) {
						messageBuilder.append(line).append(LS);
					}
					tag = new Tag(this.depot, this.changeSets.get(targetHash), name, tagHash,
					              messageBuilder.toString(), this.identityCache.request(email, name), timestamp);
					
					this.tagDumper.saveLater(tag);
					this.tags.put(tagName, tag);
					break;
			}
		}
	}
	
}
