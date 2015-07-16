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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.UnmodifiableMap;

import org.mozkito.core.libs.versions.FileCache;
import org.mozkito.core.libs.versions.IdentityCache;
import org.mozkito.core.libs.versions.builders.ChangeSetBuilder;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.core.libs.versions.model.Renaming;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.core.libs.versions.model.SignOff;
import org.mozkito.core.libs.versions.model.enums.ChangeType;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.DatabaseDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.exec.Command;

/**
 * The Class ChangeSetMiner.
 *
 * @author Sascha Just
 */
public class ChangeSetMiner extends Task implements Runnable {
	
	/** The Constant END_TAG. */
	private static final String             END_TAG               = "<<<#$@#$@<<<";
	
	/** The Constant START_TAG. */
	private static final String             START_TAG             = ">>>#$@#$@>>>";
	
	/** The Constant BIN_CHANGE_INDICATOR. */
	private static final char               BIN_CHANGE_INDICATOR  = '-';
	
	/** The Constant SIGNED_OFF_TAG. */
	private static final String             SIGNED_OFF_TAG        = "Signed-off-by: ";
	
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
	
	/** The graph. */
	private final Graph                     graph;
	
	/** The change sets. */
	private final Map<String, Vertex>       vertexes              = new HashMap<String, Vertex>();
	
	/** The change set dumper. */
	private final DatabaseDumper<ChangeSet> changeSetDumper;
	
	/** The revision dumper. */
	private final DatabaseDumper<Revision>  revisionDumper;
	
	/** The identity dumper. */
	private final DatabaseDumper<Identity>  identityDumper;
	
	/** The counter. */
	private long                            counter               = 0;
	
	/** The line. */
	private String                          line;
	
	/** The renaming dumper. */
	private final DatabaseDumper<Renaming>  renamingDumper;
	
	/** The identity cache. */
	private final IdentityCache             identityCache;
	
	/** The file cache. */
	private final FileCache                 fileCache;
	
	/** The signed off dumper. */
	private final DatabaseDumper<SignOff> signedOffDumper;
	
	/**
	 * Instantiates a new change set miner.
	 *
	 * @param cloneDir
	 *            the clone dir
	 * @param depot
	 *            the depot
	 * @param graph
	 *            the graph
	 * @param identityCache
	 *            the identity cache
	 * @param fileCache
	 *            the file cache
	 * @param identityDumper
	 *            the identity dumper
	 * @param changeSetDumper
	 *            the change set dumper
	 * @param revisionDumper
	 *            the revision dumper
	 * @param handleDumper
	 *            the handle dumper
	 * @param renamingDumper
	 *            the renaming dumper
	 * @param signedOffDumper
	 *            the signed off dumper
	 */
	public ChangeSetMiner(final File cloneDir, final Depot depot, final Graph graph, final IdentityCache identityCache,
	        final FileCache fileCache, final DatabaseDumper<Identity> identityDumper,
	        final DatabaseDumper<ChangeSet> changeSetDumper, final DatabaseDumper<Revision> revisionDumper,
	        final DatabaseDumper<Handle> handleDumper, final DatabaseDumper<Renaming> renamingDumper,
	        final DatabaseDumper<SignOff> signedOffDumper) {
		super(depot);
		this.cloneDir = cloneDir;
		this.graph = graph;
		this.fileCache = fileCache;
		this.identityCache = identityCache;
		
		this.identityDumper = identityDumper;
		this.changeSetDumper = changeSetDumper;
		this.revisionDumper = revisionDumper;
		this.renamingDumper = renamingDumper;
		this.signedOffDumper = signedOffDumper;
	}
	
	/**
	 * Gets the change sets.
	 *
	 * @return the change sets
	 */
	public Map<String, Vertex> getVertexes() {
		return UnmodifiableMap.unmodifiableMap(this.vertexes);
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
		
		Handle from = this.fileCache.get(source);
		Handle to = this.fileCache.get(target);
		
		switch (changeType) {
			case ADDED:
				from = this.fileCache.add(source);
				to = from;
				break;
			case DELETED:
				from = this.fileCache.delete(source);
				to = from;
				break;
			case MODIFIED:
				from = this.fileCache.modify(source);
				to = from;
				break;
			case RENAMED:
				from = this.fileCache.get(source);
				if (from == null) {
					// sanitize broken repository
					from = this.fileCache.add(source);
				}
				to = this.fileCache.rename(confidence, source, target, changeSet);
				break;
			case COPIED:
				from = this.fileCache.get(source);
				if (from == null) {
					// sanitize broken repository
					from = this.fileCache.add(source);
				}
				to = this.fileCache.copy(confidence, source, target, changeSet);
				break;
			default:
				if (from == null) {
					from = this.fileCache.add(source);
					to = from;
				}
				break;
		}
		
		final Revision revision = new Revision(changeSet, changeType, from, to, confidence, oldMode, newMode, oldHash,
		                                       newHash);
		
		Asserts.positive(revision.getSourceId());
		Asserts.positive(revision.getTargetId());
		
		revisions.add(revision);
		
		return revision;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Asserts.positive(this.depot.getId());
		
		/*
		 * hash tree author name author email author timestamp committer name committer email committer timestamp
		 * subject body
		 */
		
		Command.execute("git", new String[] { "config", "diff.renameLimit", "999999" }, this.cloneDir).waitFor();
		
		final Command command = Command.execute("git", new String[] { "log", "--branches", "--remotes", "--tags",
		        "--topo-order", "--find-copies", "--raw", "--numstat", "--no-abbrev", "--reverse",
		        "--format=" + START_TAG + "%n%H%n%T%n%an%n%ae%n%at%n%cn%n%ce%n%ct%n%s%n%b%n" + END_TAG }, this.cloneDir);
		
		ChangeSetBuilder changeSetBuilder = null;
		ChangeSet changeSet = null;
		Vertex vertex = null;
		final List<Revision> revisions = new LinkedList<>();
		
		StringBuilder bodyBuilder = new StringBuilder();
		final List<Identity> signers = new LinkedList<Identity>();
		
		Identity identity;
		String idName, idEmail;
		
		this.line = null;
		
		int i = 0;
		
		while ((this.line = command.nextOutput()) != null) {
			signers.clear();
			if (START_TAG.equalsIgnoreCase(this.line)) {
				this.line = command.nextOutput();
			}
			++this.counter;
			Asserts.notNull(this.line, "Awaiting commit hash.");
			changeSetBuilder = new ChangeSetBuilder();
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
			identity = this.identityCache.request(idEmail.isEmpty()
			                                                       ? null
			                                                       : idEmail, idName.isEmpty()
			                                                                                  ? null
			                                                                                  : idName);
			
			if (identity.getId() <= 0) {
				this.identityDumper.saveLater(identity);
			}
			Asserts.positive(identity.getId());
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
			identity = this.identityCache.request(idEmail, idName);
			if (identity.getId() <= 0) {
				this.identityDumper.saveLater(identity);
			}
			Asserts.positive(identity.getId());
			changeSetBuilder.committerId(identity);
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting commit timestamp.");
			if (!this.line.isEmpty()) {
				changeSetBuilder.committedOn(Instant.ofEpochSecond(Long.parseLong(this.line)));
			}
			
			this.line = command.nextOutput();
			Asserts.notNull(this.line, "Awaiting subject.");
			changeSetBuilder.subject(this.line.trim());
			
			bodyBuilder = new StringBuilder();
			
			BODY: while ((this.line = command.nextOutput()) != null) {
				this.line = this.line.trim();
				if (END_TAG.equals(this.line)) {
					break BODY;
				} else {
					if (this.line.startsWith(SIGNED_OFF_TAG)) {
						final int index = this.line.indexOf('<');
						if (index >= 0) {
							identity = this.identityCache.request(this.line.substring(index + 1, this.line.indexOf('>')),
							                                      this.line.substring(SIGNED_OFF_TAG.length(), index)
							                                               .trim());
						} else {
							identity = this.identityCache.request(null, this.line.substring(SIGNED_OFF_TAG.length())
							                                                     .trim());
						}
						if (identity.getId() <= 0) {
							this.identityDumper.saveLater(identity);
						}
						signers.add(identity);
					} else {
						bodyBuilder.append(this.line).append(System.lineSeparator());
					}
				}
			}
			
			assert changeSetBuilder != null; // stupid eclipse warning workaround
			changeSetBuilder.body(bodyBuilder.toString().trim());
			
			changeSet = changeSetBuilder.create();
			Asserts.notNull(changeSet);
			
			this.changeSetDumper.saveLater(changeSet);
			for (final Identity signer : signers) {
				this.signedOffDumper.saveLater(new SignOff(changeSet.getId(), signer.getId()));
			}
			
			this.fileCache.beginTransaction();
			
			// raw format (which gives us file mode before after and hash before/after) and numstat (which give us lines
			// in/lines out) are separate block
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
			
			this.fileCache.commit();
			
			for (final Revision revision : revisions) {
				this.revisionDumper.saveLater(revision);
			}
			
			revisions.clear();
			i = 0;
			
			vertex = new Vertex(changeSet);
			this.vertexes.put(vertex.getHash(), vertex);
			this.graph.addVertex(vertex);
		}
		
		command.waitFor();
		
		for (final Renaming renaming : this.fileCache.getRenamings()) {
			this.renamingDumper.saveLater(renaming);
		}
		
		if (Logger.logInfo()) {
			Logger.info("Processed '%s' changesets.", this.counter);
		}
	}
}
