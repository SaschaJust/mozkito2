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

package sequel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.mozkito.core.libs.users.adapters.IdentityAdapter;
import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.skeleton.logging.Bus;
import org.mozkito.skeleton.logging.Level;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.logging.consumer.LogConsumer;
import org.mozkito.skeleton.logging.consumer.appender.TerminalAppender;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelDatabase.Type;

/**
 * The Class Main.
 *
 * @author Sascha Just
 */
public class Main {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final LogConsumer consumer = new LogConsumer(Bus.provider);
		consumer.register(new TerminalAppender(Level.INFO));
		
		try {
			final Identity identity = new Identity("sjust", "sascha.just@mozkito.org", "Sascha Just");
			final SequelDatabase database = new SequelDatabase(Type.DERBY, "simpsons", null, null, null, null);
			final ISequelAdapter<Identity> adapter = new IdentityAdapter(database);
			adapter.createScheme();
			
			adapter.save(identity);
			
			final Iterator<Identity> iterator = adapter.load();
			while (iterator.hasNext()) {
				Logger.error(iterator.next().toString());
			}
		} catch (final SQLException e) {
			Logger.error(e);
		}
		
	}
	
	/**
	 * Main2.
	 *
	 * @param args
	 *            the args
	 */
	public static void main2(final String[] args) {
		final LogConsumer consumer = new LogConsumer(Bus.provider);
		consumer.register(new TerminalAppender(Level.INFO));
		
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:derby:simpsons;create=true");
		// config.setUsername("bart");
		// config.setPassword("51mp50n");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		final HikariDataSource ds = new HikariDataSource(config);
		
		try {
			final Connection connection = ds.getConnection();
			final Statement statement = connection.createStatement();
			statement.execute("DROP TABLE test");
			statement.execute("CREATE TABLE test (id varchar(12), value varchar(12))");
			
			final PreparedStatement statement2 = connection.prepareStatement("INSERT INTO test (id, value) VALUES (?, ?)");
			statement2.setString(1, "bart");
			statement2.setString(2, "simpson");
			statement2.executeUpdate();
			
			connection.commit();
			
			final ResultSet result = statement.executeQuery("SELECT * FROM test");
			while (result.next()) {
				Logger.info.println(result.getString(1));
				Logger.info.println(result.getString(2));
			}
			
			ds.shutdown();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
