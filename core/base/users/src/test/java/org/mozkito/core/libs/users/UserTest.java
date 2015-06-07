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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.mozkito.core.libs.users.adapters.UserAdapter;
import org.mozkito.core.libs.users.adapters.UserAdapter.UserIterator;
import org.mozkito.core.libs.users.model.User;
import org.mozkito.core.libs.versions.adapters.IdentityAdapter;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.skeleton.io.FileUtils;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelDatabase.Type;

/**
 * @author Sascha Just
 *
 */
public class UserTest {
	
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
		this.databaseName = Files.createTempDirectory(UserTest.class.getSimpleName() + ".db");
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
	 * Test user.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Ignore
	public void testUser() throws Exception {
		final User user = new User();
		
		Identity identity = new Identity("sascha.just@mozkito.org", "Sascha Just");
		user.addIdentity(identity);
		this.adapter.save(identity);
		
		identity = new Identity("sascha.just2@mozkito.org", "Sascha Just2");
		user.addIdentity(identity);
		this.adapter.save(identity);
		
		identity = new Identity("sascha.just3@mozkito.org", "Sascha Just3");
		user.addIdentity(identity);
		this.adapter.save(identity);
		
		final UserAdapter uAdapter = new UserAdapter(this.database);
		uAdapter.createScheme();
		
		uAdapter.save(user);
		
		final User user2 = new User();
		
		identity = new Identity("kim.herzig@mozkito.org", "Kim Herzig");
		user2.addIdentity(identity);
		this.adapter.save(identity);
		
		identity = new Identity("kim.herzig2@mozkito.org", "Kim Herzig2");
		user2.addIdentity(identity);
		this.adapter.save(identity);
		
		uAdapter.save(user2);
		
		final UserIterator iterator = (UserIterator) uAdapter.load();
		assertTrue(iterator.hasNext());
		
		final User uc1 = iterator.next();
		if (uc1.getIdentities().stream().anyMatch(x -> "kim.herzig2@mozkito.org".equals(x.getEmail()))) {
			assertThat(user2.getIdentities(), equalTo(uc1.getIdentities()));
		} else {
			assertThat(user.getIdentities(), equalTo(uc1.getIdentities()));
		}
		
		assertTrue(iterator.hasNext());
		final User uc2 = iterator.next();
		if (uc2.getIdentities().stream().anyMatch(x -> "kim.herzig2@mozkito.org".equals(x.getEmail()))) {
			assertThat(user2.getIdentities(), equalTo(uc2.getIdentities()));
		} else {
			assertThat(user.getIdentities(), equalTo(uc2.getIdentities()));
		}
	}
	
}
