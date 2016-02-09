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

package org.mozkito.libraries.sequel.bulk;

import java.sql.Connection;

import org.mozkito.libraries.sequel.IEntity;

/**
 * The Interface IAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public interface IAdapter<T extends IEntity> {
	
	/**
	 * Close.
	 */
	public abstract void close();
	
	/**
	 * Creates the constraints.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createConstraints(Connection connection);
	
	/**
	 * Creates the foreign keys.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createForeignKeys(Connection connection);
	
	/**
	 * Creates the indexes.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createIndexes(Connection connection);
	
	/**
	 * Creates the primary keys.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createPrimaryKeys(Connection connection);
	
	/**
	 * Creates the scheme.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createScheme(Connection connection);
	
	/**
	 * Flush.
	 */
	public abstract void flush();
	
	/**
	 * Inits the.
	 *
	 * @param connection
	 *            the connection
	 */
	public void init(final Connection connection);
	
	/**
	 * Next id.
	 *
	 * @return the long
	 */
	public abstract long nextId();
	
	/**
	 * Save.
	 *
	 * @param entity
	 *            the entity
	 */
	public abstract void save(T entity);
	
}
