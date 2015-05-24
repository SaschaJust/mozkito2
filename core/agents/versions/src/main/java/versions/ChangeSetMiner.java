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
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.users.IdentityCache;
import org.mozkito.core.libs.users.adapters.IdentityAdapter;
import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.versions.ChangeType;
import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.adapters.ChangeSetAdapter;
import org.mozkito.core.libs.versions.adapters.DepotAdapter;
import org.mozkito.core.libs.versions.adapters.RevisionAdapter;
import org.mozkito.core.libs.versions.builders.ChangeSetBuilder;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.datastructures.Tuple;
import org.mozkito.skeleton.exec.CommandExecutor;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * @author Sascha Just
 *
 */
public class ChangeSetMiner implements Runnable {
	
	private static final String          END_TAG     = "<<<#$@#$@<<<";
	private static final String          START_TAG   = ">>>#$@#$@>>>";
	
	private final File                   cloneDir;
	private final SequelDatabase         database;
	private final Depot                  depot;
	private final Map<String, Branch>    branchHeads = new HashMap<String, Branch>();
	private final DepotGraph             graph;
	private final Map<String, ChangeSet> changeSets  = new HashMap<String, ChangeSet>();
	private RevisionAdapter              revisionAdapter;
	private HandleAdapter                handleAdapter;
	
	private final Map<String, Handle>    fileCache   = new HashMap<>();
	
	private DepotAdapter                 depotAdapter;
	
	private ChangeSetAdapter             changeSetAdapter;
	
	private IdentityAdapter              identityAdapter;
	
	/**
	 * @param cloneDir
	 * @param database
	 * @param map
	 * @param graph
	 */
	public ChangeSetMiner(final File cloneDir, final SequelDatabase database, final Depot depot,
	        final DepotGraph graph, final Map<String, Branch> branchHeads) {
		this.cloneDir = cloneDir;
		this.database = database;
		this.depot = depot;
		this.branchHeads.putAll(branchHeads);
		this.graph = graph;
	}
	
	/**
	 * Gets the change sets.
	 *
	 * @return the change sets
	 */
	public Map<String, ChangeSet> getChangeSets() {
		return UnmodifiableMap.unmodifiableMap(this.changeSets);
	}
	
	/**
	 * @param line
	 * @return
	 */
	private Revision parseRevision(final ChangeSet changeSet,
	                               final String line) {
		final ChangeType changeType = ChangeType.from(line.charAt(0));
		Asserts.notNull(changeType);
		final int split = line.indexOf("\t", 5);
		String source, target;
		
		short confidence = 100;
		
		if (ChangeType.RENAMED.equals(changeType) || ChangeType.COPIED.equals(changeType)) {
			confidence = Short.parseShort(line.substring(1, 4));
			Asserts.greater(confidence, 49, "Confidence has to be at least 50%.");
			
			Asserts.greater(split, 5, "Renames/Copies must provide a target.");
			Asserts.greater(line.length(), split + 1, "Renames/Copies must provide a target.");
			
			source = line.substring(5, split).trim();
			target = line.substring(split + 1).trim();
			
		} else {
			source = line.substring(5).trim();
			target = source;
		}
		
		if (!this.fileCache.containsKey(source)) {
			final Handle handle = new Handle(source);
			this.fileCache.put(source, handle);
			this.handleAdapter.save(handle);
		}
		
		if (!this.fileCache.containsKey(target)) {
			final Handle handle = new Handle(target);
			this.fileCache.put(target, handle);
			this.handleAdapter.save(handle);
		}
		
		final Revision revision = new Revision(changeSet, changeType, this.fileCache.get(source),
		                                       this.fileCache.get(target), confidence);
		
		return revision;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this.depotAdapter = new DepotAdapter(this.database);
		this.depotAdapter.createScheme();
		
		this.depotAdapter.save(this.depot);
		Asserts.positive(this.depot.id());
		
		this.changeSetAdapter = new ChangeSetAdapter(this.database);
		this.changeSetAdapter.createScheme();
		
		this.identityAdapter = new IdentityAdapter(this.database);
		this.identityAdapter.createScheme();
		
		this.revisionAdapter = new RevisionAdapter(this.database);
		this.revisionAdapter.createScheme();
		
		this.handleAdapter = new HandleAdapter(this.database);
		this.handleAdapter.createScheme();
		
		final IdentityCache identityCache = new IdentityCache();
		
		/*
		 * hash tree author name author email author timestamp committer name committer email committer timestamp
		 * subject body
		 */
		Tuple<Integer, List<String>> result;
		try {
			result = CommandExecutor.execute("git", new String[] {
			                                         "log",
			                                         "--branches",
			                                         "--remotes",
			                                         "--topo-order",
			                                         "--find-copies",
			                                         "-p",
			                                         "--name-status",
			                                         "--format=" + START_TAG
			                                                 + "%n%H%n%T%n%an%n%ae%n%at%n%cn%n%ce%n%ct%n%s%n%b"
			                                                 + END_TAG + "%n" },
			                                 this.cloneDir, null, new HashMap<String, String>());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		
		ChangeSetBuilder changeSetBuilder = null;
		ChangeSet changeSet = null;
		final StringBuilder bodyBuilder = new StringBuilder();
		final StringBuilder patchBuilder = new StringBuilder();
		Identity identity;
		String idName, idEmail;
		
		final ListIterator<String> it = result.getSecond().listIterator();
		String line = null;
		
		final PreparedStatement nextIdStatement = this.changeSetAdapter.getNextIdStatement();
		final PreparedStatement saveStatement = this.changeSetAdapter.getSaveStatement();
		
		int batch = 0;
		
		while (it.hasNext()) {
			++batch;
			
			line = it.next();
			if (START_TAG.equalsIgnoreCase(line)) {
				line = it.next();
			}
			changeSetBuilder = new ChangeSetBuilder(this.depot.id());
			changeSetBuilder.commitHash(line);
			
			Asserts.hasNext(it, "Awaiting tree hash.");
			changeSetBuilder.treeHash(it.next());
			
			Asserts.hasNext(it, "Awaiting author name.");
			idName = it.next();
			Asserts.hasNext(it, "Awaiting author email.");
			idEmail = it.next();
			identity = identityCache.request(null, idName, idEmail);
			if (identity.id() <= 0) {
				this.identityAdapter.save(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.authorId(identity);
			
			Asserts.hasNext(it);
			changeSetBuilder.authoredOn(Instant.ofEpochSecond(Long.parseLong(it.next())));
			
			Asserts.hasNext(it, "Awaiting committer name.");
			idName = it.next();
			Asserts.hasNext(it, "Awaiting committer email.");
			idEmail = it.next();
			identity = identityCache.request(null, idName, idEmail);
			if (identity.id() <= 0) {
				this.identityAdapter.save(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.committerId(identity);
			
			Asserts.hasNext(it);
			changeSetBuilder.committedOn(Instant.ofEpochSecond(Long.parseLong(it.next())));
			
			Asserts.hasNext(it);
			changeSetBuilder.subject(it.next());
			
			BODY: while (it.hasNext()) {
				line = it.next();
				if (END_TAG.equals(line)) {
					break BODY;
				} else {
					bodyBuilder.append(line).append(System.lineSeparator());
				}
			}
			
			changeSetBuilder.body(bodyBuilder.toString());
			
			changeSet = changeSetBuilder.create();
			Asserts.notNull(changeSet);
			this.changeSetAdapter.save(saveStatement, nextIdStatement, changeSet);
			
			REVISIONS: while (it.hasNext()) {
				line = it.next();
				if (START_TAG.equals(line)) {
					break REVISIONS;
				} else {
					if (line.isEmpty()) {
						continue REVISIONS;
					} else {
						final Revision revision = parseRevision(changeSet, line);
						Asserts.notNull(revision);
						this.revisionAdapter.save(revision);
					}
					bodyBuilder.append(line).append(System.lineSeparator());
				}
			}
			
			if (batch % 1000 == 0) {
				this.database.commit();
			}
			
			if (Logger.logDebug()) {
				Logger.debug(changeSet.getCommitHash());
			}
			this.changeSets.put(changeSet.getCommitHash(), changeSet);
			
			this.graph.addVertex(changeSet);
			if (this.branchHeads.containsKey(changeSet.getCommitHash())) {
				this.graph.addBranchHead(this.branchHeads.get(changeSet.getCommitHash()), changeSet);
			}
		}
		
		this.database.commit();
	}
}
