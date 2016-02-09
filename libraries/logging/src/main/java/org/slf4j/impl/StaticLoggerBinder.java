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

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import org.mozkito.libraries.logging.slf4j.MozkitoLoggerFactory;

/**
 * @author Sascha Just
 *
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
	
	private static StaticLoggerBinder me = new StaticLoggerBinder();
	
	/**
	 * Gets the singleton.
	 *
	 * @return the singleton
	 */
	public static StaticLoggerBinder getSingleton() {
		return me;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.spi.LoggerFactoryBinder#getLoggerFactory()
	 */
	public ILoggerFactory getLoggerFactory() {
		return new MozkitoLoggerFactory();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.slf4j.spi.LoggerFactoryBinder#getLoggerFactoryClassStr()
	 */
	public String getLoggerFactoryClassStr() {
		return MozkitoLoggerFactory.class.getCanonicalName();
	}
}
