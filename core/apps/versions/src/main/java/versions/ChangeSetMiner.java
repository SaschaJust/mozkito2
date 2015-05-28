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
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.users.IdentityCache;
import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.versions.ChangeType;
import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.adapters.ChangeSetAdapter;
import org.mozkito.core.libs.versions.builders.ChangeSetBuilder;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangeSetMiner.
 *
 * @author Sascha Just
 */
public class ChangeSetMiner implements Runnable {
	
	/** The Constant END_TAG. */
	private static final String            END_TAG               = "<<<#$@#$@<<<";
	
	/** The Constant START_TAG. */
	private static final String            START_TAG             = ">>>#$@#$@>>>";
	
	/** The Constant BIN_CHANGE_INDICATOR. */
	private static final char              BIN_CHANGE_INDICATOR  = '-';
	
	/** The raw old mode offset. */
	private static int                     RAW_OLD_MODE_OFFSET   = 1;
	
	/** The raw new mode offset. */
	private static int                     RAW_NEW_MODE_OFFSET   = 8;
	
	/** The raw old hash offset. */
	private static int                     RAW_OLD_HASH_OFFSET   = 15;
	
	/** The raw new hash offset. */
	private static int                     RAW_NEW_HASH_OFFSET   = 56;
	
	/** The raw changetype offset. */
	private static int                     RAW_CHANGETYPE_OFFSET = 97;
	
	/** The clone dir. */
	private final File                     cloneDir;
	
	/** The database. */
	private final SequelDatabase           database;
	
	/** The depot. */
	private final Depot                    depot;
	
	/** The branch heads. */
	private final Map<String, Branch>      branchHeads           = new HashMap<String, Branch>();
	
	/** The graph. */
	private final DepotGraph               graph;
	
	/** The change sets. */
	private final Map<String, ChangeSet>   changeSets            = new HashMap<String, ChangeSet>();
	
	/** The file cache. */
	private final Map<String, Handle>      fileCache             = new HashMap<>();
	
	/** The revision adapter. */
	private final ISequelAdapter<Revision> revisionAdapter;
	
	/** The handle adapter. */
	private final ISequelAdapter<Handle>   handleAdapter;
	
	/** The change set adapter. */
	private final ChangeSetAdapter         changeSetAdapter;
	
	/** The identity adapter. */
	private final ISequelAdapter<Identity> identityAdapter;
	
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
	 * @param branchHeads
	 *            the branch heads
	 */
	public ChangeSetMiner(final File cloneDir, final SequelDatabase database, final Depot depot,
	        final DepotGraph graph, final Map<String, Branch> branchHeads) {
		this.cloneDir = cloneDir;
		this.database = database;
		this.depot = depot;
		this.branchHeads.putAll(branchHeads);
		this.graph = graph;
		this.revisionAdapter = database.getAdapter(Revision.class);
		this.identityAdapter = database.getAdapter(Identity.class);
		this.changeSetAdapter = new ChangeSetAdapter(database);
		this.handleAdapter = database.getAdapter(Handle.class);
		
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
	 */
	private void parseInOutRevision(final ChangeSet changeSet,
	                                final String line,
	                                final Map<Long, Revision> revisions) {
		Requires.notNull(changeSet);
		Requires.notNull(line);
		Requires.notEmpty(line);
		Requires.notNull(revisions);
		
		// format: ([1-9][0-9]*|-)\t([1-9][0-9]*|-)\tPATH_NAME
		final int linesInOffset = 0;
		final int linesOutOffset = line.indexOf('\t', linesInOffset) + 1;
		final int nameOffset = line.indexOf('\t', linesOutOffset) + 1;
		
		int lineIn, lineOut;
		String filename;
		
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
		
		final int filenameEnd = line.indexOf(" => ", nameOffset);
		if (filenameEnd > 0) {
			final int lbi = line.indexOf('{');
			final int rbi = line.indexOf('}');
			
			if (lbi > 0 && rbi > 0) {
				// this is to remove this ugly renaming indicator
				filename = line.substring(nameOffset, lbi) + line.substring(filenameEnd + 4, rbi)
				        + (rbi + 1 < line.length()
				                                  ? line.substring(rbi + 1)
				                                  : "");
			} else {
				filename = line.substring(nameOffset, filenameEnd);
			}
			filename = filename.replaceAll("//", "/").trim();
		} else {
			filename = line.substring(nameOffset).trim();
		}
		
		// we must have seen this file before during parsing
		Asserts.containsKey(this.fileCache,
		                    filename,
		                    "File %s is not known to the file cache, but must have been seen in the raw format parsing step.",
		                    filename);
		final Long handleId = this.fileCache.get(filename).id();
		final Revision revision = revisions.get(handleId);
		
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
	                                  final Map<Long, Revision> revisions) {
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
			this.fileCache.put(source, handle);
			this.handleAdapter.save(handle);
		}
		
		if (!this.fileCache.containsKey(target)) {
			final Handle handle = new Handle(this.depot, target);
			this.fileCache.put(target, handle);
			this.handleAdapter.save(handle);
		}
		
		final Revision revision = new Revision(this.depot, changeSet, changeType, this.fileCache.get(source),
		                                       this.fileCache.get(target), confidence, oldMode, newMode, oldHash,
		                                       newHash);
		
		revisions.put(revision.getSourceId(), revision);
		revisions.put(revision.getTargetId(), revision);
		
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
		final StringBuilder bodyBuilder = new StringBuilder();
		
		Identity identity;
		String idName, idEmail;
		
		String line = null;
		
		final PreparedStatement nextIdStatement = this.changeSetAdapter.getNextIdStatement();
		final PreparedStatement saveStatement = this.changeSetAdapter.getSaveStatement();
		
		int batch = 0;
		
		while ((line = command.nextOutput()) != null) {
			++batch;
			
			if (START_TAG.equalsIgnoreCase(line)) {
				line = command.nextOutput();
			}
			Asserts.notNull(line, "Awaiting commit hash.");
			changeSetBuilder = new ChangeSetBuilder(this.depot.id());
			changeSetBuilder.commitHash(line);
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting tree hash.");
			changeSetBuilder.treeHash(line);
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting author name.");
			idName = line;
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting author email.");
			idEmail = line;
			identity = identityCache.request(null, idName.isEmpty()
			                                                       ? null
			                                                       : idName, idEmail.isEmpty()
			                                                                                  ? null
			                                                                                  : idEmail);
			
			if (identity.id() <= 0) {
				this.identityAdapter.save(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.authorId(identity);
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting authored timestamp.");
			if (!line.isEmpty()) {
				changeSetBuilder.authoredOn(Instant.ofEpochSecond(Long.parseLong(line)));
			}
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting committer name.");
			idName = line;
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting committer email.");
			idEmail = line;
			identity = identityCache.request(null, idName, idEmail);
			if (identity.id() <= 0) {
				this.identityAdapter.save(identity);
			}
			Asserts.positive(identity.id());
			changeSetBuilder.committerId(identity);
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting commit timestamp.");
			changeSetBuilder.committedOn(Instant.ofEpochSecond(Long.parseLong(line)));
			
			line = command.nextOutput();
			Asserts.notNull(line, "Awaiting subject.");
			changeSetBuilder.subject(line.trim());
			
			BODY: while ((line = command.nextOutput()) != null) {
				line = line.trim();
				if (END_TAG.equals(line)) {
					break BODY;
				} else {
					bodyBuilder.append(line).append(System.lineSeparator());
				}
			}
			
			changeSetBuilder.body(bodyBuilder.toString().trim());
			
			changeSet = changeSetBuilder.create();
			Asserts.notNull(changeSet);
			this.changeSetAdapter.save(saveStatement, nextIdStatement, changeSet);
			
			final Map<Long, Revision> revisions = new HashMap<>();
			
			// raw format (which gives us file mode before after and hash before/after) and numstat (which give us lines
			// in/lines out) are separate blocsk
			REVISIONS: while ((line = command.nextOutput()) != null) {
				if (START_TAG.equals(line)) {
					break REVISIONS;
				} else if (line.startsWith(":")) {
					final Revision revision = parseRawRevision(changeSet, line, revisions);
					Asserts.notNull(revision);
				} else {
					if (line.isEmpty()) {
						continue REVISIONS;
					} else {
						parseInOutRevision(changeSet, line, revisions);
					}
				}
			}
			
			for (final Revision revision : revisions.values()) {
				this.revisionAdapter.save(revision);
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
		command.waitFor();
	}
}
