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

package org.mozkito.skeleton.logging.slf4j;

import org.slf4j.helpers.MarkerIgnoringBase;

import org.mozkito.skeleton.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class MozkitoLoggerAdapter.
 *
 * @author Sascha Just
 */
public class MozkitoLoggerAdapter extends MarkerIgnoringBase {
	
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
