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

package org.mozkito.libraries.logging.slf4j;

import org.slf4j.helpers.MarkerIgnoringBase;

import org.mozkito.libraries.logging.Logger;

/**
 * The Class MozkitoLoggerAdapter.
 *
 * @author Sascha Just
 */
public class MozkitoLoggerAdapter extends MarkerIgnoringBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 493348570530630750L;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	public void debug(final String arg0) {
		Logger.debug(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	public void debug(final String arg0,
	                  final Object arg1) {
		Logger.debug(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	public void debug(final String arg0,
	                  final Object... arg1) {
		Logger.debug(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void debug(final String arg0,
	                  final Object arg1,
	                  final Object arg2) {
		Logger.debug(arg0, arg1, arg2);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	public void debug(final String arg0,
	                  final Throwable arg1) {
		Logger.debug(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	public void error(final String arg0) {
		Logger.error(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	public void error(final String arg0,
	                  final Object arg1) {
		Logger.error(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	public void error(final String arg0,
	                  final Object... arg1) {
		Logger.error(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void error(final String arg0,
	                  final Object arg1,
	                  final Object arg2) {
		Logger.error(arg0, arg1, arg2);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	public void error(final String arg0,
	                  final Throwable arg1) {
		Logger.error(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	public void info(final String arg0) {
		Logger.info(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	public void info(final String arg0,
	                 final Object arg1) {
		Logger.info(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	public void info(final String arg0,
	                 final Object... arg1) {
		Logger.info(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void info(final String arg0,
	                 final Object arg1,
	                 final Object arg2) {
		Logger.info(arg0, arg1, arg2);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	public void info(final String arg0,
	                 final Throwable arg1) {
		Logger.info(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return Logger.logDebug();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return Logger.logError();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return Logger.logInfo();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return Logger.logTrace();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return Logger.logWarn();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	public void trace(final String arg0) {
		Logger.trace(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	public void trace(final String arg0,
	                  final Object arg1) {
		Logger.trace(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	public void trace(final String arg0,
	                  final Object... arg1) {
		Logger.trace(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void trace(final String arg0,
	                  final Object arg1,
	                  final Object arg2) {
		Logger.trace(arg0, arg1, arg2);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	public void trace(final String arg0,
	                  final Throwable arg1) {
		Logger.trace(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	public void warn(final String arg0) {
		Logger.warn(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	public void warn(final String arg0,
	                 final Object arg1) {
		Logger.warn(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	public void warn(final String arg0,
	                 final Object... arg1) {
		Logger.warn(arg0, arg1);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void warn(final String arg0,
	                 final Object arg1,
	                 final Object arg2) {
		Logger.warn(arg0, arg1, arg2);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	public void warn(final String arg0,
	                 final Throwable arg1) {
		Logger.warn(arg0, arg1);
	}
	
}
