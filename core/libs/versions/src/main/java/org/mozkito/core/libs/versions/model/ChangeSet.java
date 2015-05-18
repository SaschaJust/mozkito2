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

package org.mozkito.core.libs.versions.model;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class ChangeSet.
 *
 * @author Sascha Just
 */
public class ChangeSet implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long  serialVersionUID = 230724292878651772L;
	
	/** The application time. */
	private final Instant      authoredTime;
	
	/** The author id. */
	private int                authorId;
	
	/** The branch id. */
	private final Set<Integer> branchIds        = new HashSet<Integer>();
	
	/** The commit hash. */
	private String             commitHash;
	
	/**
	 * The committer id.
	 *
	 * &quot;You may be wondering what the difference is between author and committer. The author is the person who
	 * originally wrote the patch, whereas the committer is the person who last applied the patch. So, if you send in a
	 * patch to a project and one of the core members applies the patch, both of you get credit — you as the author and
	 * the core member as the committer.&quot;
	 *
	 * from: <a href="http://git-scm.com/book/ch2-3.html">FREE online Pro Git book</a>
	 * */
	private int                committerId;
	
	/** The commit time. */
	private Instant            commitTime;
	
	/** The depot id. */
	private int                depotId;
	
	/** The id. */
	private long               id;
	
	/** The merged branch ids. */
	private Set<Integer>       mergedBranchIds  = new HashSet<Integer>();
	
	/** The original id. */
	private String             originalId;
	
	/** The parent hashes. */
	private Set<String>        parentHashes     = new HashSet<String>();
	
	/** The patch hash. */
	private String             patchHash;
	
	/** The tree hash. */
	private String             treeHash;
	
	/**
	 * Instantiates a new change set.
	 *
	 * @param depotId
	 *            the depot id
	 * @param commitHash
	 *            the commit hash
	 * @param parentHashes
	 *            the parent hashes
	 * @param treeHash
	 *            the tree hash
	 * @param patchHash
	 *            the patch hash
	 * @param authoredTime
	 *            the authored time
	 * @param authorId
	 *            the author id
	 * @param commitTime
	 *            the commit time
	 * @param committerId
	 *            the committer id
	 */
	public ChangeSet(final int depotId, final String commitHash, final Set<String> parentHashes, final String treeHash,
	        final String patchHash, final Instant authoredTime, final int authorId, final Instant commitTime,
	        final int committerId) {
		super();
		this.depotId = depotId;
		this.commitHash = commitHash;
		this.parentHashes = parentHashes;
		this.treeHash = treeHash;
		this.patchHash = patchHash;
		this.authoredTime = authoredTime;
		this.authorId = authorId;
		this.commitTime = commitTime;
		this.committerId = committerId;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ChangeSet other = (ChangeSet) obj;
		if (this.commitHash == null) {
			if (other.commitHash != null) {
				return false;
			}
		} else if (!this.commitHash.equals(other.commitHash)) {
			return false;
		}
		if (this.depotId != other.depotId) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the author id.
	 *
	 * @return the authorId
	 */
	public final int getAuthorId() {
		return this.authorId;
	}
	
	/**
	 * Gets the commit hash.
	 *
	 * @return the commitHash
	 */
	public final String getCommitHash() {
		return this.commitHash;
	}
	
	/**
	 * Gets the committer id.
	 *
	 * @return the committerId
	 */
	public final int getCommitterId() {
		return this.committerId;
	}
	
	/**
	 * Gets the commit time.
	 *
	 * @return the commitTime
	 */
	public final Instant getCommitTime() {
		return this.commitTime;
	}
	
	/**
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final int getDepotId() {
		return this.depotId;
	}
	
	/**
	 * Gets the merged branch ids.
	 *
	 * @return the mergedBranchIds
	 */
	public final Set<Integer> getMergedBranchIds() {
		return this.mergedBranchIds;
	}
	
	/**
	 * Gets the original id.
	 *
	 * @return the originalId
	 */
	public final String getOriginalId() {
		return this.originalId;
	}
	
	/**
	 * Gets the parent hashes.
	 *
	 * @return the parentHashes
	 */
	public final Set<String> getParentHashes() {
		return this.parentHashes;
	}
	
	/**
	 * Gets the patch hash.
	 *
	 * @return the patchHash
	 */
	public final String getPatchHash() {
		return this.patchHash;
	}
	
	/**
	 * Gets the tree hash.
	 *
	 * @return the treeHash
	 */
	public final String getTreeHash() {
		return this.treeHash;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.commitHash == null
		                                                  ? 0
		                                                  : this.commitHash.hashCode());
		result = prime * result + this.depotId;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	@Override
	public Object id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(java.lang.Object)
	 */
	@Override
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isLong(id);
		
		this.id = (long) id;
	}
	
	/**
	 * Sets the author id.
	 *
	 * @param authorId
	 *            the authorId to set
	 */
	public final void setAuthorId(final int authorId) {
		this.authorId = authorId;
	}
	
	/**
	 * Sets the commit hash.
	 *
	 * @param commitHash
	 *            the commitHash to set
	 */
	public final void setCommitHash(final String commitHash) {
		this.commitHash = commitHash;
	}
	
	/**
	 * Sets the committer id.
	 *
	 * @param committerId
	 *            the committerId to set
	 */
	public final void setCommitterId(final int committerId) {
		this.committerId = committerId;
	}
	
	/**
	 * Sets the commit time.
	 *
	 * @param commitTime
	 *            the commitTime to set
	 */
	public final void setCommitTime(final Instant commitTime) {
		this.commitTime = commitTime;
	}
	
	/**
	 * Sets the depot id.
	 *
	 * @param depotId
	 *            the depotId to set
	 */
	public final void setDepotId(final int depotId) {
		this.depotId = depotId;
	}
	
	/**
	 * Sets the merged branch ids.
	 *
	 * @param mergedBranchIds
	 *            the mergedBranchIds to set
	 */
	public final void setMergedBranchIds(final Set<Integer> mergedBranchIds) {
		this.mergedBranchIds = mergedBranchIds;
	}
	
	/**
	 * Sets the original id.
	 *
	 * @param originalId
	 *            the originalId to set
	 */
	public final void setOriginalId(final String originalId) {
		this.originalId = originalId;
	}
	
	/**
	 * Sets the parent hashes.
	 *
	 * @param parentHashes
	 *            the parentHashes to set
	 */
	public final void setParentHashes(final Set<String> parentHashes) {
		this.parentHashes = parentHashes;
	}
	
	/**
	 * Sets the patch hash.
	 *
	 * @param patchHash
	 *            the patchHash to set
	 */
	public final void setPatchHash(final String patchHash) {
		this.patchHash = patchHash;
	}
	
	/**
	 * Sets the tree hash.
	 *
	 * @param treeHash
	 *            the treeHash to set
	 */
	public final void setTreeHash(final String treeHash) {
		this.treeHash = treeHash;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		final StringBuilder builder = new StringBuilder();
		builder.append("ChangeSet [id=");
		builder.append(this.id);
		builder.append(", depotId=");
		builder.append(this.depotId);
		builder.append(", commitHash=");
		builder.append(this.commitHash);
		builder.append(", authorId=");
		builder.append(this.authorId);
		builder.append(", authoredTime=");
		builder.append(this.authoredTime);
		builder.append(", committerId=");
		builder.append(this.committerId);
		builder.append(", commitTime=");
		builder.append(this.commitTime);
		builder.append(", originalId=");
		builder.append(this.originalId);
		builder.append(", parentHashes=");
		builder.append(this.parentHashes != null
		                                        ? toString(this.parentHashes, maxLen)
		                                        : null);
		builder.append(", patchHash=");
		builder.append(this.patchHash);
		builder.append(", treeHash=");
		builder.append(this.treeHash);
		builder.append(", mergedBranchIds=");
		builder.append(this.mergedBranchIds != null
		                                           ? toString(this.mergedBranchIds, maxLen)
		                                           : null);
		builder.append(", branchIds=");
		builder.append(this.branchIds != null
		                                     ? toString(this.branchIds, maxLen)
		                                     : null);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * To string.
	 *
	 * @param collection
	 *            the collection
	 * @param maxLen
	 *            the max len
	 * @return the string
	 */
	private String toString(final Collection<?> collection,
	                        final int maxLen) {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
}
