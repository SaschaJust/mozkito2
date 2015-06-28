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

package org.mozkito.core.libs.versions.builders;

import java.time.Instant;

import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.libraries.sequel.IBuilder;

/**
 * The Class ChangeSetBuilder.
 *
 * @author Sascha Just
 */
public class ChangeSetBuilder implements IBuilder<ChangeSet> {
	
	/** The application time. */
	private Instant authoredTime;
	
	/** The author id. */
	private long    authorId;
	
	/** The commit hash. */
	private String  commitHash;
	
	/** The committer id. */
	private long    committerId;
	
	/** The commit time. */
	private Instant commitTime;
	
	/** The tree hash. */
	private String  treeHash;
	
	/** The subject. */
	private String  subject;
	
	/** The body. */
	private String  body;
	
	/**
	 * Authored on.
	 *
	 * @param authoredOn
	 *            the authored on
	 * @return the change set builder
	 */
	public ChangeSetBuilder authoredOn(final Instant authoredOn) {
		this.authoredTime = authoredOn;
		return this;
	}
	
	/**
	 * Author id.
	 *
	 * @param identity
	 *            the identity
	 */
	public void authorId(final Identity identity) {
		this.authorId = identity.getId();
	}
	
	/**
	 * Body.
	 *
	 * @param body
	 *            the body
	 * @return the change set builder
	 */
	public ChangeSetBuilder body(final String body) {
		this.body = body;
		return this;
	}
	
	/**
	 * Commit hash.
	 *
	 * @param commitHash
	 *            the commit hash
	 * @return the change set builder
	 */
	public ChangeSetBuilder commitHash(final String commitHash) {
		this.commitHash = commitHash;
		return this;
	}
	
	/**
	 * Committed on.
	 *
	 * @param committedOn
	 *            the committed on
	 * @return the change set builder
	 */
	public ChangeSetBuilder committedOn(final Instant committedOn) {
		this.commitTime = committedOn;
		return this;
	}
	
	/**
	 * Committer id.
	 *
	 * @param identity
	 *            the identity
	 */
	public void committerId(final Identity identity) {
		this.committerId = identity.getId();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IBuilder#create()
	 */
	public ChangeSet create() {
		if (this.commitTime == null) {
			this.commitTime = Instant.EPOCH;
		}
		
		if (this.authoredTime == null) {
			this.authoredTime = this.commitTime;
		}
		
		return new ChangeSet(this.commitHash, this.treeHash, this.authoredTime, this.authorId, this.commitTime,
		                     this.committerId, this.subject, this.body);
	}
	
	/**
	 * Subject.
	 *
	 * @param subject
	 *            the subject
	 * @return the change set builder
	 */
	public ChangeSetBuilder subject(final String subject) {
		this.subject = subject;
		return this;
	}
	
	/**
	 * Tree hash.
	 *
	 * @param treeHash
	 *            the tree hash
	 * @return the change set builder
	 */
	public ChangeSetBuilder treeHash(final String treeHash) {
		this.treeHash = treeHash;
		return this;
	}
	
}
