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

package quality;

import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.mozkito.analysis.quality.IntegrationsPerWeek;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.sequel.BulkReader;
import org.mozkito.skeleton.sequel.Database;
import org.mozkito.skeleton.sequel.Database.Type;

/**
 * @author Sascha Just
 *
 */
public class Main {
	
	private static Options createOptions() {
		final Options options = new Options();
		
		Option option;
		option = new Option("dn", "database-name", true, "The name of the database.");
		option.setArgName("DB_NAME");
		option.setRequired(true);
		options.addOption(option);
		
		option = new Option("dt", "database-type", true, "The name of the database.");
		option.setArgName("POSTGRES,TSQL,DERBY");
		option.setRequired(true);
		options.addOption(option);
		
		option = new Option("dh", "database-host", true, "The name of the database.");
		option.setArgName("localhost");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("du", "database-user", true, "The user to be used to connect to the database.");
		option.setArgName("DB_USER");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("dp", "database-password", true, "The password to be used to connect to the database.");
		option.setArgName("DB_PASSWORD");
		option.setRequired(false);
		options.addOption(option);
		
		return options;
	}
	
	/**
	 * @param args
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static void main(final String[] args) throws SQLException, ParseException {
		final Options options = createOptions();
		final CommandLineParser parser = new DefaultParser();
		
		final CommandLine line = parser.parse(options, args);
		
		Type databaseType = null;
		
		try {
			databaseType = Type.valueOf(line.getOptionValue("database-type").trim().toUpperCase());
		} catch (final IllegalArgumentException e) {
			Logger.error("Database type '%s' is not invalid.", line.getOptionValue("database.type"));
			System.exit(1);
		}
		final String databaseName = line.hasOption("database-name")
		                                                           ? line.getOptionValue("database-name")
		                                                           : "mozkito-versions";
		
		Logger.info("Establishing database connection and creating pool.");
		
		final Database database = new Database(
		                                       databaseType,
		                                       databaseName,
		                                       line.hasOption("database-host")
		                                                                      ? line.getOptionValue("database-host")
		                                                                      : null,
		                                       line.hasOption("database-user")
		                                                                      ? line.getOptionValue("database-user")
		                                                                      : null,
		                                       line.hasOption("database-password")
		                                                                          ? line.getOptionValue("database-password")
		                                                                          : null,
		                                       null,
		                                       line.hasOption("database-password")
		                                                                          ? line.getOptionValue("database-args")
		                                                                          : null);
		
		IntegrationsPerWeek ipw = new IntegrationsPerWeek(database.getType());
		final BulkReader<IntegrationsPerWeek> ipwReader = new BulkReader<IntegrationsPerWeek>(ipw.query(), database,
		                                                                                      ipw);
		while ((ipw = ipwReader.read()) != null) {
			System.err.println(ipw);
		}
	}
}
