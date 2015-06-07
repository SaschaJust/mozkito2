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

/**
 * The Class Entry.
 */
public class Entry {
	
	/** The similarity. */
	private final short similarity;
	
	/** The from. */
	private final long  from;
	
	/** The to. */
	private final long  to;
	
	private final long  where;
	
	/**
	 * Instantiates a new entry.
	 *
	 * @param similarity
	 *            the similarity
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param where
	 *            the where
	 */
	public Entry(final short similarity, final long from, final long to, final long where) {
		super();
		this.similarity = similarity;
		this.from = from;
		this.to = to;
		this.where = where;
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
		final Entry other = (Entry) obj;
		if (this.from != other.from) {
			return false;
		}
		if (this.to != other.to) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the from
	 */
	public final long getFrom() {
		return this.from;
	}
	
	/**
	 * @return the similarity
	 */
	public final short getSimilarity() {
		return this.similarity;
	}
	
	/**
	 * @return the to
	 */
	public final long getTo() {
		return this.to;
	}
	
	/**
	 * @return the when
	 */
	public final long getWhere() {
		return this.where;
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
		result = prime * result + (int) (this.from ^ this.from >>> 32);
		result = prime * result + (int) (this.to ^ this.to >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Entry [similarity=");
		builder.append(this.similarity);
		builder.append(", from=");
		builder.append(this.from);
		builder.append(", to=");
		builder.append(this.to);
		builder.append(", where=");
		builder.append(this.where);
		builder.append("]");
		return builder.toString();
	}
}
