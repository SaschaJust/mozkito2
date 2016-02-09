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

package org.mozkito.libraries.logging.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Locale;

import org.mozkito.libraries.logging.Bus;
import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.Line;

/**
 * The Class LogStream.
 *
 * @author Sascha Just
 */
public class LogStream extends PrintStream {

	/** The Constant LS. */
	private static final char[]   LS        = System.lineSeparator().toCharArray();

	/** The Constant LSS. */
	private static final String   LSS       = System.lineSeparator();

	/** The level. */
	private final Level           level;

	/** The line. */
	private final Line            line;

	/** The stream. */
	private ByteArrayOutputStream stream    = new ByteArrayOutputStream();

	/** The previous. */
	private char                  previous  = '\0';

	/** The trouble. */
	private boolean               trouble;

	/** The closing. */
	private boolean               closing   = false;

	/** The formatter. */
	private Formatter             formatter = null;

	/**
	 * Instantiates a new log stream.
	 *
	 * @param level
	 *            the level
	 */
	public LogStream(final Level level) {
		this(level, Line.LOGS);
	}

	/**
	 * Instantiates a new log stream.
	 *
	 * @param level
	 *            the level
	 * @param line
	 *            the line
	 */
	public LogStream(final Level level, final Line line) {
		super(new OutputStream() {

			@Override
			public void write(final int b) throws IOException {
				// void
			}
		});
		this.level = level;
		this.line = line;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#append(char)
	 */
	@Override
	public LogStream append(final char c) {
		if (LogStream.LS.length == 2) {
			if (this.previous == LogStream.LS[0] && c == LogStream.LS[1]) {
				final String message = this.stream.toString().substring(0, this.stream.size() - 1);
				Bus.notify(this.level, this.line, message);
				this.stream.reset();
			} else {
				this.stream.write(c);
			}
			this.previous = c;
		} else {
			if (c == LogStream.LS[0]) {
				Bus.notify(this.level, this.line, this.stream.toString());
				this.stream.reset();
			} else {
				this.stream.write(c);
			}
			this.previous = c;
		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#append(java.lang.CharSequence)
	 */
	@Override
	public LogStream append(final CharSequence csq) {
		String string = csq.toString();
		if (LogStream.LS.length > 1 && this.previous == LogStream.LS[0] && csq.length() > 0
		        && csq.charAt(0) == LogStream.LS[1]) {
			final String message = this.stream.toString().substring(0, this.stream.size() - 1);
			Bus.notify(this.level, this.line, message);
			this.stream.reset();
			string = string.substring(1);
			this.previous = LogStream.LS[LogStream.LS.length - 1];
		}

		int pIndex = 0, cIndex = 0;

		while ((cIndex = string.indexOf(LogStream.LSS, pIndex)) != -1) {
			Bus.notify(this.level, this.line, this.stream.toString() + string.substring(pIndex, cIndex));
			this.stream.reset();
			pIndex = cIndex + 1;
			this.previous = LogStream.LS[LogStream.LS.length - 1];
		}

		this.stream.reset();
		if (pIndex < string.length()) {
			try {
				string = string.substring(pIndex);
				this.stream.write(string.getBytes());
				this.previous = string.toCharArray()[string.length() - 1];
			} catch (final IOException e) {
				this.trouble = true;
			}
		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public LogStream append(final CharSequence csq,
	                        final int start,
	                        final int end) {
		return append(csq.subSequence(start, end));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#checkError()
	 */
	@Override
	public boolean checkError() {
		return this.trouble;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#clearError()
	 */
	@Override
	protected void clearError() {
		this.trouble = false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#close()
	 */
	@Override
	public void close() {
		synchronized (this) {
			if (!this.closing) {
				this.closing = true;
				try {

					this.stream.close();
				} catch (final IOException x) {
					this.trouble = true;
				}
				this.stream = null;
			}
		}
	}

	/**
	 * Ensure open.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void ensureOpen() throws IOException {
		if (this.stream == null) {
			throw new IOException("Stream closed");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#flush()
	 */
	@Override
	public void flush() {
		Bus.notify(this.level, this.line, this.stream.toString());
		this.stream.reset();
		this.previous = LogStream.LS[LogStream.LS.length - 1];
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#format(java.util.Locale, java.lang.String, java.lang.Object[])
	 */
	@Override
	public PrintStream format(final Locale l,
	                          final String format,
	                          final Object... args) {
		try {
			synchronized (this) {
				ensureOpen();
				if (this.formatter == null || this.formatter.locale() != l) {
					this.formatter = new Formatter(this, l);
				}
				this.formatter.format(l, format, args);
			}
		} catch (final InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (final IOException x) {
			this.trouble = true;
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#format(java.lang.String, java.lang.Object[])
	 */
	@Override
	public PrintStream format(final String format,
	                          final Object... args) {
		try {
			synchronized (this) {
				ensureOpen();
				if (this.formatter == null || this.formatter.locale() != Locale.getDefault()) {
					this.formatter = new Formatter((Appendable) this);
				}
				this.formatter.format(Locale.getDefault(), format, args);
			}
		} catch (final InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (final IOException x) {
			this.trouble = true;
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(boolean)
	 */
	@Override
	public void print(final boolean b) {
		print(String.valueOf(b));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(char)
	 */
	@Override
	public void print(final char c) {
		print(String.valueOf(c));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(char[])
	 */
	@Override
	public void print(final char[] s) {
		print(String.valueOf(s));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(double)
	 */
	@Override
	public void print(final double d) {
		print(String.valueOf(d));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(float)
	 */
	@Override
	public void print(final float f) {
		print(String.valueOf(f));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(int)
	 */
	@Override
	public void print(final int i) {
		print(String.valueOf(i));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(long)
	 */
	@Override
	public void print(final long l) {
		print(String.valueOf(l));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(java.lang.Object)
	 */
	@Override
	public void print(final Object obj) {
		print(String.valueOf(obj));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#print(java.lang.String)
	 */
	@Override
	public void print(final String s) {
		synchronized (this) {
			if (s == null) {
				append("null");
			} else {
				append(s);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println()
	 */
	@Override
	public void println() {
		synchronized (this) {
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(boolean)
	 */
	@Override
	public void println(final boolean x) {
		synchronized (this) {
			append(x
			       ? "true"
			         : "false");
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(char)
	 */
	@Override
	public void println(final char x) {
		synchronized (this) {
			append(x);
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(char[])
	 */
	@Override
	public void println(final char[] x) {
		synchronized (this) {
			print(x);
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(double)
	 */
	@Override
	public void println(final double x) {
		synchronized (this) {
			append(String.valueOf(x));
			flush();
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(float)
	 */
	@Override
	public void println(final float x) {
		synchronized (this) {
			append(String.valueOf(x));
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(int)
	 */
	@Override
	public void println(final int x) {
		synchronized (this) {
			append(String.valueOf(x));
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(long)
	 */
	@Override
	public void println(final long x) {
		synchronized (this) {
			append(String.valueOf(x));
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(java.lang.Object)
	 */
	@Override
	public void println(final Object x) {
		synchronized (this) {
			append(String.valueOf(x));
			flush();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#println(java.lang.String)
	 */
	@Override
	public void println(final String x) {
		append(x);
		flush();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#setError()
	 */
	@Override
	protected void setError() {
		this.trouble = true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LogStream [");
		if (this.level != null) {
			builder.append("level=");
			builder.append(this.level);
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(final byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte[] buf,
	                  final int off,
	                  final int len) {
		try {
			synchronized (this) {
				ensureOpen();
				this.stream.write(buf, off, len);
				this.previous = (char) buf[off + len - 1];
			}
		} catch (final InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (final IOException x) {
			this.trouble = true;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.PrintStream#write(int)
	 */
	@Override
	public void write(final int b) {
		try {
			synchronized (this) {
				ensureOpen();
				this.stream.write(b);
				this.previous = (char) b;
				if (b == '\n') {
					this.stream.flush();
				}
			}
		} catch (final InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (final IOException x) {
			this.trouble = true;
		}
	}

}
