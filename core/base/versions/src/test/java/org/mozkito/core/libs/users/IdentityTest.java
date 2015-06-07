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

package org.mozkito.core.libs.users;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.mozkito.core.libs.versions.adapters.IdentityAdapter;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.skeleton.io.FileUtils;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelDatabase.Type;

/**
 * The Class IdentityTest.
 *
 * @author Sascha Just
 */
public class IdentityTest {
	
	/** The database name. */
	private Path                     databaseName;
	
	/** The database. */
	private SequelDatabase           database;
	
	/** The adapter. */
	private ISequelAdapter<Identity> adapter;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		this.databaseName = Files.createTempDirectory(IdentityTest.class.getSimpleName() + ".db");
		FileUtils.deleteDirectory(this.databaseName);
		this.database = new SequelDatabase(Type.DERBY, this.databaseName + ";create=true", null, null, null, null);
		this.adapter = new IdentityAdapter(this.database);
		this.adapter.createScheme();
	}
	
	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		this.database.close();
		FileUtils.deleteDirectory(this.databaseName);
	}
	
	/**
	 * Test load. .
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Ignore
	public void testLoad() throws Exception {
		final Identity identity = new Identity("sascha.just@mozkito.org", "Sascha Just");
		this.adapter.save(identity);
		
		final Iterator<Identity> iterator = this.adapter.load();
		assertTrue(iterator.hasNext());
		final Identity loaded = iterator.next();
		
		assertThat(loaded, equalTo(identity));
	}
	
	/**
	 * Test save.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	public final void testSave() throws SQLException {
		final Identity identity = new Identity("sascha.just@mozkito.org", "Sascha Just");
		this.adapter.save(identity);
	}
	
}
