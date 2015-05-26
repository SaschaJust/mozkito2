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

package versions;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.filefilter.IOFileFilter;

import org.mozkito.core.libs.users.adapters.IdentityAdapter;
import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.adapters.BranchAdapter;
import org.mozkito.core.libs.versions.adapters.ChangeSetAdapter;
import org.mozkito.core.libs.versions.adapters.DepotAdapter;
import org.mozkito.core.libs.versions.adapters.GraphAdapter;
import org.mozkito.core.libs.versions.adapters.HandleAdapter;
import org.mozkito.core.libs.versions.adapters.RevisionAdapter;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.SequelDatabase;

import versions.TaskRunner.Task;

/**
 * The entry point of app: mozkito-versions
 *
 * @author Sascha Just
 */
public class Main {
	
	/**
	 * The Class MozkitoHandler.
	 */
	public static class MozkitoHandler implements UncaughtExceptionHandler {
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		public void uncaughtException(final Thread t,
		                              final Throwable e) {
			Logger.fatal(e, "%s: Unhandled exception. Terminated.", t.getName());
		}
		
	}
	
	/**
	 * Creates the options.
	 *
	 * @return the options
	 */
	private static Options createOptions() {
		final Options options = new Options();
		
		Option option = new Option("r", "repository", true, "The URI to the repository");
		option.setArgName("URI");
		option.setRequired(true);
		options.addOption(option);
		
		option = new Option("w", "working-directory", true, "The working directory.");
		option.setArgName("DIR");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("dn", "database-name", true, "The name of the database.");
		option.setArgName("DB_NAME");
		option.setRequired(true);
		options.addOption(option);
		
		option = new Option("du", "database-user", true, "The user to be used to connect to the database.");
		option.setArgName("DB_USER");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("dp", "database-password", true, "The password to be used to connect to the database.");
		option.setArgName("DB_PASSWORD");
		option.setRequired(false);
		options.addOption(option);
		
		option = new Option("tb", "mine-branches", false, "Mine branches. Required for changesets.");
		options.addOption(option);
		
		option = new Option("tc", "mine-changesets", false, "Mine changesets. Required for graph.");
		options.addOption(option);
		
		option = new Option("tg", "mine-graph", false, "Mine graph. Required for integration and visibility.");
		options.addOption(option);
		
		option = new Option("ti", "mine-integration", false, "Mine integration.");
		options.addOption(option);
		
		option = new Option("tv", "mine-visibility", false, "Mine branches. ");
		options.addOption(option);
		
		return options;
	}
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		// create the command line parser
		final CommandLineParser parser = new DefaultParser();
		
		final Options options = createOptions();
		// create the Options
		
		try {
			final CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(120);
				formatter.printHelp("mozkito-versions", options);
				System.exit(0);
			}
			
			final File workDir = new File(line.hasOption("working-dir")
			                                                           ? line.getOptionValue("working-dir")
			                                                           : "/tmp");
			
			final URI uri = new URI(line.hasOption("repository")
			                                                    ? line.getOptionValue("repository")
			                                                    : "file:///tmp/mozkito_bare.git");
			
			final String databaseName = line.hasOption("database-name")
			                                                           ? line.getOptionValue("database-name")
			                                                           : "mozkito-versions";
			
			Logger.info("Establishing database connection and creating pool.");
			final Properties props = new Properties();
			props.setProperty("user", "just");
			props.setProperty("password", "mm3m549DvIn28rg");
			final SequelDatabase database = new SequelDatabase("jdbc:postgresql://localhost" + databaseName, props);
			
			final File baseDir = new File(uri);
			
			final Iterator<File> depotDirs;
			if (baseDir.getName().endsWith(".git")) {
				depotDirs = new ArrayList<File>() {
					
					{
						add(baseDir);
					}
				}.iterator();
			} else {
				depotDirs = org.apache.commons.io.FileUtils.iterateFiles(baseDir, new IOFileFilter() {
					
					@Override
					public boolean accept(final File file) {
						return false;
					}
					
					@Override
					public boolean accept(final File dir,
					                      final String name) {
						return false;
					}
				}, new IOFileFilter() {
					
					@Override
					public boolean accept(final File file) {
						return file.getName().endsWith(".git");
					}
					
					@Override
					public boolean accept(final File dir,
					                      final String name) {
						return false;
					}
				});
			}
			
			File cloneDir;
			
			database.register(Depot.class, new DepotAdapter(database));
			database.register(Identity.class, new IdentityAdapter(database));
			database.register(ChangeSet.class, new ChangeSetAdapter(database));
			database.register(Revision.class, new RevisionAdapter(database));
			database.register(DepotGraph.class, new GraphAdapter(database));
			database.register(Branch.class, new BranchAdapter(database));
			database.register(Handle.class, new HandleAdapter(database));
			database.createScheme();
			
			final ExecutorService es = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() - 2);
			
			while (depotDirs.hasNext()) {
				cloneDir = depotDirs.next();
				final URI depotURI = cloneDir.toURI();
				
				es.execute(new TaskRunner(baseDir, workDir, depotURI, database, new Task[] { Task.BRANCHES,
				        Task.CHANGESETS, Task.GRAPH }));
				es.shutdown();
				System.out.println("-----------------------");
				final boolean ret = es.awaitTermination(30, TimeUnit.DAYS);
				System.out.println("All tasks are finished! Timeout: " + !ret);
				
			}
		} catch (final SQLException | URISyntaxException | InterruptedException e) {
			Logger.error(e);
		} catch (final ParseException e) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(120);
			formatter.printHelp("mozkito-versions", options);
			System.exit(1);
		}
	}
	
}
