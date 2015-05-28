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
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.users.IdentityCache;
import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.versions.ChangeType;
import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.builders.ChangeSetBuilder;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class ChangeSetMiner.
 *
 * @author Sascha Just
 */
public class ChangeSetMiner implements Runnable {
	
	/** The Constant END_TAG. */
	private static final String             END_TAG               = "<<<#$@#$@<<<";
	
	/** The Constant START_TAG. */
	private static final String             START_TAG             = ">>>#$@#$@>>>";
	
	/** The Constant BIN_CHANGE_INDICATOR. */
	private static final char               BIN_CHANGE_INDICATOR  = '-';
	
	/** The raw old mode offset. */
	private static int                      RAW_OLD_MODE_OFFSET   = 1;
	
	/** The raw new mode offset. */
	private static int                      RAW_NEW_MODE_OFFSET   = 8;
	
	/** The raw old hash offset. */
	private static int                      RAW_OLD_HASH_OFFSET   = 15;
	
	/** The raw new hash offset. */
	private static int                      RAW_NEW_HASH_OFFSET   = 56;
	
	/** The raw changetype offset. */
	private static int                      RAW_CHANGETYPE_OFFSET = 97;
	
	/** The clone dir. */
	private final File                      cloneDir;
	
	/** The database. */
	private final SequelDatabase            database;
	
	/** The depot. */
	private final Depot                     depot;
	/** The graph. */
	private final DepotGraph                graph;
	
	/** The change sets. */
	private final Map<String, ChangeSet>    changeSets            = new HashMap<String, ChangeSet>();
	
	/** The file cache. */
	private final Map<String, Handle>       fileCache             = new HashMap<>();
	
	/** The change set dumper. */
	private final DatabaseDumper<ChangeSet> changeSetDumper;
	
	/** The revision dumper. */
	private final DatabaseDumper<Revision>  revisionDumper;
	
	/** The handle dumper. */
	private final DatabaseDumper<Handle>    handleDumper;
	
	/** The identity dumper. */
	private final DatabaseDumper<Identity>  identityDumper;
	
	/** The counter. */
	private long                            counter               = 0;
	
	private String                          line;
	
	/**
	 * Instantiates a new change set miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param database
	 *            the database
	 * @param depot
	 *            the depot
	 * @param graph
	 *            the graph
	 */
	public ChangeSetMiner(final File cloneDir, final SequelDatabase database, final Depot depot, final DepotGraph graph) {
		this.cloneDir = cloneDir;
		this.database = database;
		this.depot = depot;
		this.graph = graph;
		
		this.identityDumper = new DatabaseDumper<Identity>(database.getAdapter(Identity.class));
		this.identityDumper.start();
		this.changeSetDumper = new DatabaseDumper<ChangeSet>(database.getAdapter(ChangeSet.class));
		this.changeSetDumper.start();
		this.revisionDumper = new DatabaseDumper<Revision>(database.getAdapter(Revision.class));
		this.revisionDumper.start();
		this.handleDumper = new DatabaseDumper<Handle>(database.getAdapter(Handle.class));
		this.handleDumper.start();
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
	 * Parses the in out revision.
	 *
	 * @param changeSet
	 *            the change set
	 * @param line
	 *            the line
	 * @param revisions
	 *            the revisions
	 * @param i
	 *            the i
	 */
	private void parseInOutRevision(final ChangeSet changeSet,
	                                final String line,
	                                final List<Revision> revisions,
	                                final int i) {
		Requires.notNull(changeSet);
		Requires.notNull(line);
		Requires.notEmpty(line);
		Requires.notNull(revisions);
		
		// format: ([1-9][0-9]*|-)\t([1-9][0-9]*|-)\tPATH_NAME
		final int linesInOffset = 0;
		final int linesOutOffset = line.indexOf('\t', linesInOffset) + 1;
		final int nameOffset = line.indexOf('\t', linesOutOffset) + 1;
		
		int lineIn, lineOut;
		
		final String lineInString = line.substring(linesInOffset, linesOutOffset - 1);
		final String lineOutString = line.substring(linesOutOffset, nameOffset - 1);
		
		if (lineInString.charAt(0) == BIN_CHANGE_INDICATOR) {
			lineIn = -1;
		} else {
			lineIn = Integer.parseInt(lineInString);
		}
		
		if (lineOutString.charAt(0) == BIN_CHANGE_INDICATOR) {
			// binary files should not be shown as line based edited at some point
			Asserts.equalTo(-1, lineIn);
			lineOut = -1;
		} else {
			lineOut = Integer.parseInt(lineOutString);
		}
		
		final Revision revision = revisions.get(i);
		
		// the revision has to be created in the previous step
		Asserts.notNull(revision);
		
		revision.setLinesIn(lineIn);
		revision.setLinesOut(lineOut);
	}
	
	/**
	 * Parses the raw revision.
	 *
	 * @param changeSet
	 *            the change set
	 * @param line
	 *            the line
	 * @param revisions
	 *            the revisions
	 * @return the revision
	 */
	private Revision parseRawRevision(final ChangeSet changeSet,
	                                  final String line,
	                                  final List<Revision> revisions) {
		Requires.notNull(changeSet);
		Requires.notNull(line);
		Requires.notEmpty(line);
		Requires.notNull(revisions);
		Requires.charAt(line, 0, ':');
		
		final ChangeType changeType = ChangeType.from(line.charAt(RAW_CHANGETYPE_OFFSET));
		Asserts.notNull(changeType, "Character '%s' does not represent a valid change type. Line: %s",
		                String.valueOf(line.charAt(0)), line);
		String source, target;
		
		short confidence = 100;
		
		if (ChangeType.RENAMED.equals(changeType) || ChangeType.COPIED.equals(changeType)) {
			confidence = Short.parseShort(line.substring(RAW_CHANGETYPE_OFFSET + 1, RAW_CHANGETYPE_OFFSET + 4));
			// determine offset of the old file name
			// i.e. skip 'R100\t'.length() chars from the RXXX/CXXX change type indicator
			final int oldNameOffset = RAW_CHANGETYPE_OFFSET + 5;
			final int split = line.indexOf("\t", oldNameOffset);
			Asserts.greater(confidence, 49, "Confidence has to be at least 50%.");
			Asserts.greater(split, oldNameOffset, "Renames/Copies must provide a target.");
			Asserts.greater(line.length(), split + 1, "Renames/Copies must provide a target.");
			
			source = line.substring(oldNameOffset, split).trim();
			target = line.substring(split + 1).trim();
		} else {
			final int nameOffSet = RAW_CHANGETYPE_OFFSET + 2;
			source = line.substring(nameOffSet).trim();
			target = source;
		}
		
		final int oldMode = Integer.parseInt(line.substring(RAW_OLD_MODE_OFFSET, RAW_NEW_MODE_OFFSET - 1));
		final int newMode = Integer.parseInt(line.substring(RAW_NEW_MODE_OFFSET, RAW_OLD_HASH_OFFSET - 1));
		final String oldHash = line.substring(RAW_OLD_HASH_OFFSET, RAW_NEW_HASH_OFFSET - 1);
		final String newHash = line.substring(RAW_NEW_HASH_OFFSET, RAW_CHANGETYPE_OFFSET - 1);
		
		if (!this.fileCache.containsKey(source)) {
			final Handle handle = new Handle(this.depot, source);
			this.handleDumper.saveLater(handle);
			Asserts.positive(handle.id());
			this.fileCache.put(source, handle);
		}
		
		if (!this.fileCache.containsKey(target)) {
			final Handle handle = new Handle(this.depot, target);
			this.handleDumper.saveLater(handle);
			Asserts.positive(handle.id());
			this.fileCache.put(target, handle);
		}
		
		final Revision revision = new Revision(this.depot, changeSet, changeType, this.fileCache.get(source),
		                                       this.fileCache.get(target), confidence, oldMode, newMode, oldHash,
		                                       newHash);
		
		Asserts.positive(revision.getSourceId());
		Asserts.positive(revision.getTargetId());
		Asserts.equalTo(revision.getSourceId(), this.fileCache.get(source).id(),
		                "Current value '%s' vs stored in revision '%s'", revision.getSourceId(),
		                this.fileCache.get(source).id());
		Asserts.equalTo(revision.getTargetId(), this.fileCache.get(target).id(),
		                "Current value '%s' vs stored in revision '%s'", revision.getSourceId(),
		                this.fileCache.get(source).id());
		
		revisions.add(revision);
		
		return revision;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Asserts.positive(this.depot.id());
		
		final IdentityCache identityCache = new IdentityCache();
		
		/*
		 * hash tree author name author email author timestamp committer name committer email committer timestamp
		 * subject body
		 */
		
		Command.execute("git", new String[] { "config", "diff.renameLimit", "999999" }, this.cloneDir).waitFor();
		
		final Command command = Command.execute("git", new String[] { "log", "--branches", "--remotes", "--topo-order",
		        "--find-copies", "--raw", "--numstat", "--no-abbrev",
		        "--format=" + START_TAG + "%n%H%n%T%n%an%n%ae%n%at%n%cn%n%ce%n%ct%n%s%n%b%n" + END_TAG }, this.cloneDir);
		
		ChangeSetBuilder changeSetBuilder = null;
		ChangeSet changeSet = null;
		final List<Revision> revisions = new LinkedList<>();
		
		final StringBuilder bodyBuilder = new StringBuilder();
		
		Identity identity;
		String idName, idEmail;
		
		this.line = null;
		
		int batch = 0;
		int i = 0;
		
		while ((this.line = command.nextOutput()) != null) {
			++batch;
			
			if (START_TAG.equalsIgnoreCase(this.line)) {
				this.line = command.nextOutput();
			}
			++this.counter;
			Asserts.notNull(this.line, "Awaiting commit hash.");
			changeSetBuilder = new ChangeSetBuilder(this.depot.id());
			changeSetBuilder.commitHash(this.line);
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting tree hash.");
			changeSetBuilder.treeHash(this.line);
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting author name.");
			idName = this.line;
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting author email.");
			idEmail = this.line;
			identity = identityCache.request(null, idName.isEmpty()
			                                                       ? null
			                                                       : idName, idEmail.isEmpty()
			                                                                                  ? null
			                                                                                  : idEmail);
			
			if (identity.id() <= 0) {
				this.identityDumper.saveLater(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.authorId(identity);
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting authored timestamp.");
			if (!this.line.isEmpty()) {
				changeSetBuilder.authoredOn(Instant.ofEpochSecond(Long.parseLong(this.line)));
			}
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting committer name.");
			idName = this.line;
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting committer email.");
			idEmail = this.line;
			identity = identityCache.request(null, idName, idEmail);
			if (identity.id() <= 0) {
				this.identityDumper.saveLater(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.committerId(identity);
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting commit timestamp.");
			if (!this.line.isEmpty()) {
				changeSetBuilder.committedOn(Instant.ofEpochSecond(Long.parseLong(this.line)));
			}
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting subject.");
			changeSetBuilder.subject(this.line.trim());
			
			BODY: while ((this.line = command.nextOutput()) != null) {
				this.line = this.line.trim();
				if (END_TAG.equals(this.line)) {
					break BODY;
				} else {
					bodyBuilder.append(this.line).append(System.lineSeparator());
				}
			}
			
			changeSetBuilder.body(bodyBuilder.toString().trim());
			
			changeSet = changeSetBuilder.create();
			Asserts.notNull(changeSet);
			
			this.changeSetDumper.saveLater(changeSet);
			
			// raw format (which gives us file mode before after and hash before/after) and numstat (which give us lines
			// in/lines out) are separate blocsk
			REVISIONS: while ((this.line = command.nextOutput()) != null) {
				if (START_TAG.equals(this.line)) {
					break REVISIONS;
				} else if (this.line.startsWith(":")) {
					final Revision revision = parseRawRevision(changeSet, this.line, revisions);
					Asserts.notNull(revision);
				} else {
					if (this.line.isEmpty()) {
						continue REVISIONS;
					} else {
						parseInOutRevision(changeSet, this.line, revisions, i);
						++i;
					}
				}
			}
			
			for (final Revision revision : revisions) {
				this.revisionDumper.saveLater(revision);
			}
			
			revisions.clear();
			i = 0;
			
			if (batch % 1000 == 0) {
				this.database.commit();
			}
			
			this.changeSets.put(changeSet.getCommitHash(), changeSet);
			
			this.graph.addVertex(changeSet);
		}
		
		try {
			this.changeSetDumper.interrupt();
			this.changeSetDumper.join();
			this.revisionDumper.interrupt();
			this.revisionDumper.join();
			this.handleDumper.interrupt();
			this.handleDumper.join();
			this.identityDumper.interrupt();
			this.identityDumper.join();
		} catch (final InterruptedException e) {
			if (Logger.logWarn()) {
				Logger.warn(e);
			}
		}
		
		this.database.commit();
		command.waitFor();
		
		if (Logger.logInfo()) {
			Logger.info("Processed '%s' changesets.", this.counter);
		}
	}
}
