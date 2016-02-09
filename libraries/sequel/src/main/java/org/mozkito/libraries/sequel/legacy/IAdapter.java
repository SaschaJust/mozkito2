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

package org.mozkito.libraries.sequel.legacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.mozkito.libraries.sequel.IEntity;

/**
 * The Interface ISequelAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public interface IAdapter<T extends IEntity> {
	
	/**
	 * Voidclose.
	 */
	void close();
	
	/**
	 * Creates the.
	 *
	 * @param result
	 *            the result
	 * @return the t
	 */
	T create(ResultSet result);
	
	/**
	 * Creates the constraints.
	 *
	 * @param connection
	 *            the connection
	 */
	void createConstraints(Connection connection);
	
	/**
	 * Creates the foreign keys.
	 *
	 * @param connection
	 *            the connection
	 */
	void createForeignKeys(Connection connection);
	
	/**
	 * Creates the indexes.
	 *
	 * @param connection
	 *            the connection
	 */
	void createIndexes(Connection connection);
	
	/**
	 * Creates the primary keys.
	 *
	 * @param connection
	 *            the connection
	 */
	void createPrimaryKeys(Connection connection);
	
	/**
	 * Creates the scheme.
	 *
	 * @param connection
	 *            the connection
	 */
	void createScheme(Connection connection);
	
	/**
	 * Delete.
	 *
	 * @param connection
	 *            the connection
	 * @param object
	 *            the object
	 */
	void delete(Connection connection,
	            T object);
	
	/**
	 * Execute.
	 *
	 * @param statement
	 *            the statement
	 * @throws SQLException
	 *             the SQL exception
	 */
	void execute(final PreparedStatement statement) throws SQLException;
	
	/**
	 * Load.
	 *
	 * @param connection
	 *            the connection
	 * @return the iterator
	 */
	Iterator<T> load(Connection connection);
	
	/**
	 * Load.
	 *
	 * @param connection
	 *            the connection
	 * @param ids
	 *            the ids
	 * @return the t
	 */
	List<T> load(Connection connection,
	             long... ids);
	
	/**
	 * Load.
	 *
	 * @param connection
	 *            the connection
	 * @param id
	 *            the id
	 * @return the t
	 */
	T load(Connection connection,
	       long id);
	
	/**
	 * Next id.
	 *
	 * @return the object
	 */
	long nextId();
	
	/**
	 * Prepare save statement.
	 *
	 * @param connection
	 *            the connection
	 * @return the prepared statement
	 */
	PreparedStatement prepareSaveStatement(Connection connection);
	
	/**
	 * Save.
	 *
	 * @param connection
	 *            the connection
	 * @param objects
	 *            the objects
	 */
	void save(Connection connection,
	          @SuppressWarnings ("unchecked") T... objects);
	
	/**
	 * Save.
	 *
	 * @param saveStatement
	 *            the save statement
	 * @param id
	 *            the id
	 * @param entity
	 *            the entity
	 */
	void save(PreparedStatement saveStatement,
	          long id,
	          T entity);
	
	/**
	 * Insert query.
	 *
	 * @return the string
	 */
	String saveStatement();
	
	/**
	 * Update.
	 *
	 * @param connection
	 *            the connection
	 * @param objects
	 *            the objects
	 */
	void update(Connection connection,
	            @SuppressWarnings ("unchecked") T... objects);
	
}
