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
