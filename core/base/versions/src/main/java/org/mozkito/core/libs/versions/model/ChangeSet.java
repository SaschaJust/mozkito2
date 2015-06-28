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
import java.util.Set;

import org.apache.commons.collections4.collection.UnmodifiableCollection;

import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class ChangeSet.
 *
 * @author Sascha Just
 */
public class ChangeSet implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 230724292878651772L;
	
	/** The application time. */
	private final Instant     authoredTime;
	
	/** The author id. */
	private final long        authorId;
	
	/** The commit hash. */
	private final String      commitHash;
	
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
	private final long        committerId;
	
	/** The commit time. */
	private final Instant     commitTime;
	
	/** The branch ids. */
	private final Set<Long>   branchIds        = new HashSet<>();
	
	/** The id. */
	private long              id;
	
	/** The tree hash. */
	private final String      treeHash;
	
	/** The subject. */
	private final String      subject;
	
	/** The body. */
	private final String      body;
	
	/**
	 * Instantiates a new change set.
	 *
	 * @param commitHash
	 *            the commit hash
	 * @param treeHash
	 *            the tree hash
	 * @param authoredTime
	 *            the authored time
	 * @param authorId
	 *            the author id
	 * @param commitTime
	 *            the commit time
	 * @param committerId
	 *            the committer id
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 */
	public ChangeSet(final String commitHash, final String treeHash, final Instant authoredTime, final long authorId,
	        final Instant commitTime, final long committerId, final String subject, final String body) {
		super();
		Requires.notNull(commitHash);
		Requires.length(commitHash, 40);
		Requires.notNull(treeHash);
		Requires.length(treeHash, 40);
		Requires.notNull(authoredTime);
		Requires.positive(authorId);
		Requires.notNull(commitTime);
		Requires.positive(committerId);
		Requires.notNull(subject);
		Requires.notNull(body);
		
		this.commitHash = commitHash;
		this.treeHash = treeHash;
		this.authoredTime = authoredTime;
		this.authorId = authorId;
		this.commitTime = commitTime;
		this.committerId = committerId;
		this.subject = subject;
		this.body = body;
		
		Asserts.notNull(this.branchIds);
	}
	
	/**
	 * Adds the branch id.
	 *
	 * @param branchId
	 *            the branch id
	 * @return true, if successful
	 */
	public boolean addBranchId(final long branchId) {
		Requires.positive(branchId);
		
		return this.branchIds.add(branchId);
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
		return true;
	}
	
	/**
	 * Gets the authored time.
	 *
	 * @return the authoredTime
	 */
	public final Instant getAuthoredTime() {
		return this.authoredTime;
	}
	
	/**
	 * Gets the author id.
	 *
	 * @return the authorId
	 */
	public final long getAuthorId() {
		return this.authorId;
	}
	
	/**
	 * @return the body
	 */
	public final String getBody() {
		return this.body;
	}
	
	/**
	 * Gets the branch ids.
	 *
	 * @return the branch ids
	 */
	public Collection<Long> getBranchIds() {
		return UnmodifiableCollection.unmodifiableCollection(this.branchIds);
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
	public final long getCommitterId() {
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
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	@Override
	public long getId() {
		return this.id;
	}
	
	/**
	 * @return the subject
	 */
	public final String getSubject() {
		return this.subject;
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
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ChangeSet [id=");
		builder.append(this.id);
		builder.append(", commitHash=");
		builder.append(this.commitHash);
		builder.append(", subject=");
		builder.append(this.subject);
		builder.append(", authorId=");
		builder.append(this.authorId);
		builder.append(", authoredTime=");
		builder.append(this.authoredTime);
		builder.append(", committerId=");
		builder.append(this.committerId);
		builder.append(", commitTime=");
		builder.append(this.commitTime);
		builder.append(", treeHash=");
		builder.append(this.treeHash);
		builder.append(", body=");
		builder.append(this.body);
		builder.append("]");
		return builder.toString();
	}
	
}
