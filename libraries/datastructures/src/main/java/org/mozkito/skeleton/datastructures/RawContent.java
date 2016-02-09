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

package org.mozkito.skeleton.datastructures;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.Instant;

import org.mozkito.skeleton.commons.JavaUtils;

/**
 * The Class RawContent.
 * 
 * @author Sascha Just <sascha.just@st.cs.uni-saarland.de>
 */
public class RawContent implements Comparable<RawContent> {
	
	/** The md5. */
	private String        md5;
	
	/** The fetch time. */
	private final Instant fetchTime;
	
	/** The format. */
	private final String  format;
	
	/** The content. */
	private final String  content;
	
	/** The uri. */
	private final URI     uri;
	
	/**
	 * Instantiates a new raw content.
	 * 
	 * @param uri
	 *            the uri
	 * @param md5
	 *            not null
	 * @param fetchTime
	 *            not null
	 * @param format
	 *            not null
	 * @param content
	 *            not null, 0 &lt; <code>content.length</code>
	 */
	public RawContent(final URI uri, final byte[] md5, final Instant fetchTime, final String format,
	        final String content) {
		super();
		try {
			this.md5 = JavaUtils.byteArrayToHexString(md5);
		} catch (final UnsupportedEncodingException e) {
			// ignore
			assert false;
			this.md5 = null;
		}
		
		this.uri = uri;
		this.fetchTime = fetchTime;
		this.format = format;
		this.content = content;
	}
	
	/**
	 * Instantiates a new raw content.
	 * 
	 * @param uri
	 *            the uri
	 * @param md5
	 *            the md5
	 * @param fetchTime
	 *            the fetch time
	 * @param format
	 *            the format
	 * @param content
	 *            the content
	 */
	public RawContent(final URI uri, final String md5, final Instant fetchTime, final String format,
	        final String content) {
		super();
		
		this.uri = uri;
		this.md5 = md5;
		this.fetchTime = fetchTime;
		this.format = format;
		this.content = content;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final RawContent arg0) {
		if (arg0 == null) {
			return 1;
		}
		return this.fetchTime.compareTo(arg0.getFetchTime());
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
		final RawContent other = (RawContent) obj;
		if (this.md5 == null) {
			if (other.md5 != null) {
				return false;
			}
		} else if (!this.md5.equals(other.md5)) {
			return false;
		}
		if (this.uri == null) {
			if (other.uri != null) {
				return false;
			}
		} else if (!this.uri.equals(other.uri)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * Gets the fetch time.
	 * 
	 * @return the fetchTime
	 */
	public Instant getFetchTime() {
		return this.fetchTime;
	}
	
	/**
	 * Gets the format.
	 * 
	 * @return the format
	 */
	public String getFormat() {
		return this.format;
	}
	
	/**
	 * Gets the md5.
	 * 
	 * @return the md5
	 */
	public String getMd5() {
		return this.md5;
	}
	
	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public long getSize() {
		return this.content.length();
	}
	
	/**
	 * Gets the uri.
	 * 
	 * @return the uri
	 */
	public URI getUri() {
		return this.uri;
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
		result = prime * result + (this.md5 == null
		                                           ? 0
		                                           : this.md5.hashCode());
		result = prime * result + (this.uri == null
		                                           ? 0
		                                           : this.uri.hashCode());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("RawReport [md5=");
		builder.append(getMd5());
		builder.append(", fetchTime=");
		builder.append(this.fetchTime);
		builder.append(", format=");
		builder.append(this.format);
		// builder.append(", content=");
		// builder.append(StringEscapeUtils.escapeJava(StringEscapeUtils.unescapeHtml(this.content.length() > 10
		// ? this.content.substring(0,
		// 10)
		// : this.content)));
		builder.append("]");
		return builder.toString();
	}
	
}
