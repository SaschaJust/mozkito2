/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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
