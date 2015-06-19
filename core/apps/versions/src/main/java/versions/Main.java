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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import org.mozkito.core.apps.versions.MozkitoHandler;
import org.mozkito.core.apps.versions.TaskRunner;
import org.mozkito.core.apps.versions.TaskRunner.Task;
import org.mozkito.core.libs.versions.IdentityCache;
import org.mozkito.core.libs.versions.adapters.BranchAdapter;
import org.mozkito.core.libs.versions.adapters.ChangeSetAdapter;
import org.mozkito.core.libs.versions.adapters.ChangeSetIntegrationAdapter;
import org.mozkito.core.libs.versions.adapters.DepotAdapter;
import org.mozkito.core.libs.versions.adapters.GraphAdapter;
import org.mozkito.core.libs.versions.adapters.HandleAdapter;
import org.mozkito.core.libs.versions.adapters.IdentityAdapter;
import org.mozkito.core.libs.versions.adapters.RenamingAdapter;
import org.mozkito.core.libs.versions.adapters.RevisionAdapter;
import org.mozkito.core.libs.versions.adapters.TagAdapter;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.ChangeSetIntegration;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.core.libs.versions.model.Renaming;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.sequel.DatabaseDumper;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelDatabase.Type;

// TODO: Auto-generated Javadoc
/**
 * The entry point of app: mozkito-versions.
 *
 * @author Sascha Just
 */
public class Main {
	
	/** The Constant EXIT_ERR_SETTINGS. */
	private static final int EXIT_ERR_SETTINGS = 1;
	
	/** The Constant EXIT_HELP. */
	private static final int EXIT_HELP         = 0;
	
	/** The Constant EXIT_ERR_DB_TYPE. */
	private static final int EXIT_ERR_DB_TYPE  = 2;
	
	/** The Constant EXIT_ERR_TASKS. */
	private static final int EXIT_ERR_TASKS    = 4;
	
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
		
		option = new Option("tb", "mine-branches", false, "Mine branches. Required for changesets.");
		options.addOption(option);
		
		option = new Option("tc", "mine-changesets", false, "Mine changesets. Required for graph.");
		options.addOption(option);
		
		option = new Option("te", "mine-endpoints", false, "Mine endpoints, i.e. branch root and head.");
		options.addOption(option);
		
		option = new Option("tg", "mine-graph", false, "Mine graph. Required for integration and visibility.");
		options.addOption(option);
		
		option = new Option("ti", "mine-integration", false, "Mine integration.");
		options.addOption(option);
		
		option = new Option(null, "skip", true, "Skip depots.");
		option.setArgName("repo/manifests");
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
		Logger.consoleLevel(Level.DEBUG);
		Logger.fileLevel(Level.DEBUG);
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		// create the command line parser
		final CommandLineParser parser = new DefaultParser();
		
		final Options options = createOptions();
		// create the Options
		
		try {
			final CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				printHelp(options);
				System.exit(EXIT_HELP);
			}
			
			Type databaseType = null;
			
			try {
				databaseType = Type.valueOf(line.getOptionValue("database-type").trim().toUpperCase());
			} catch (final IllegalArgumentException e) {
				Logger.error("Database type '%s' is not invalid.", line.getOptionValue("database.type"));
				printHelp(options);
				System.exit(EXIT_ERR_DB_TYPE);
			}
			
			final List<Task> tasks = new LinkedList<TaskRunner.Task>();
			
			if (line.hasOption("mine-branches")) {
				tasks.add(Task.BRANCHES);
			}
			
			if (line.hasOption("mine-changesets")) {
				tasks.add(Task.CHANGESETS);
			}
			
			if (line.hasOption("mine-endpoints")) {
				tasks.add(Task.ENDPOINTS);
			}
			
			if (line.hasOption("mine-graph")) {
				tasks.add(Task.GRAPH);
			}
			
			if (line.hasOption("mine-integration")) {
				tasks.add(Task.INTEGRATION);
			}
			
			if (tasks.isEmpty()) {
				if (Logger.logError()) {
					Logger.error("No tasks selected.");
				}
				printHelp(options);
				System.exit(EXIT_ERR_TASKS);
			}
			
			File workDir = new File(line.hasOption("working-directory")
			                                                           ? line.getOptionValue("working-directory")
			                                                           : System.getProperty("java.io.tmpdir"));
			workDir = Files.createTempDirectory(workDir.toPath(), "mozkito2-").toFile();
			final URI uri = new URI(line.hasOption("repository")
			                                                    ? line.getOptionValue("repository")
			                                                    : "file:///tmp/mozkito_bare.git");
			
			final String databaseName = line.hasOption("database-name")
			                                                           ? line.getOptionValue("database-name")
			                                                           : "mozkito-versions";
			
			Logger.info("Establishing database connection and creating pool.");
			
			final SequelDatabase database = new SequelDatabase(
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
			                                                                                      : null, null);
			
			final File baseDir = new File(uri);
			final List<File> skips = new LinkedList<>();
			if (line.hasOption("skip")) {
				final String[] splits = line.getOptionValue("skip").split(",");
				for (final String split : splits) {
					skips.add(new File(baseDir, split));
				}
			}
			final List<File> depotDirs = new LinkedList<File>();
			if (baseDir.getName().endsWith(".git")) {
				depotDirs.add(baseDir);
			} else {
				FileUtils.listFilesAndDirs(baseDir, FalseFileFilter.FALSE, new IOFileFilter() {
					
					public boolean accept(final File file) {
						try {
							if (FileUtils.isSymlink(file)) {
								return false;
							}
						} catch (final IOException e) {
							// ignore
						}
						
						if (file.getName().startsWith(".")) {
							return false;
						}
						
						if (skips.contains(file)) {
							if (Logger.logInfo()) {
								Logger.info("Skipping depot at '%s' due to skip configuration.", file);
							}
							return false;
						}
						
						if (file.isDirectory() && file.getName().endsWith(".git")) {
							depotDirs.add(file);
							return false;
						}
						return true;
					}
					
					public boolean accept(final File dir,
					                      final String name) {
						return true;
					}
				});
			}
			
			database.register(Depot.class, new DepotAdapter(database));
			database.register(Identity.class, new IdentityAdapter(database));
			database.register(ChangeSet.class, new ChangeSetAdapter(database));
			database.register(Revision.class, new RevisionAdapter(database));
			database.register(Graph.class, new GraphAdapter(database));
			database.register(Branch.class, new BranchAdapter(database));
			database.register(Handle.class, new HandleAdapter(database));
			database.register(Renaming.class, new RenamingAdapter(database));
			database.register(ChangeSetIntegration.class, new ChangeSetIntegrationAdapter(database));
			database.register(Tag.class, new TagAdapter(database));
			database.createScheme();
			
			final DatabaseDumper<Identity> identityDumper = new DatabaseDumper<>(database.getAdapter(Identity.class));
			final DatabaseDumper<ChangeSet> changeSetDumper = new DatabaseDumper<>(database.getAdapter(ChangeSet.class));
			final DatabaseDumper<Revision> revisionDumper = new DatabaseDumper<>(database.getAdapter(Revision.class));
			final DatabaseDumper<Branch> branchDumper = new DatabaseDumper<>(database.getAdapter(Branch.class));
			final DatabaseDumper<Handle> handleDumper = new DatabaseDumper<>(database.getAdapter(Handle.class));
			final DatabaseDumper<Graph> graphDumper = new DatabaseDumper<>(database.getAdapter(Graph.class));
			final DatabaseDumper<Depot> depotDumper = new DatabaseDumper<>(database.getAdapter(Depot.class));
			final DatabaseDumper<Renaming> renamingDumper = new DatabaseDumper<>(database.getAdapter(Renaming.class));
			final DatabaseDumper<ChangeSetIntegration> integrationDumper = new DatabaseDumper<>(
			                                                                                    database.getAdapter(ChangeSetIntegration.class));
			final DatabaseDumper<Tag> tagDumper = new DatabaseDumper<Tag>(database.getAdapter(Tag.class));
			
			identityDumper.start();
			changeSetDumper.start();
			revisionDumper.start();
			branchDumper.start();
			handleDumper.start();
			graphDumper.start();
			depotDumper.start();
			renamingDumper.start();
			integrationDumper.start();
			
			final IdentityCache identityCache = new IdentityCache();
			
			System.out.println("Database setup complete.");
			
			final ExecutorService es = Executors.newWorkStealingPool(Math.max(Runtime.getRuntime()
			                                                                         .availableProcessors() - 2, 1));
			
			for (final File cloneDir : depotDirs) {
				final URI depotURI = cloneDir.toURI();
				final TaskRunner runner = new TaskRunner(baseDir, workDir, depotURI, tasks.toArray(new Task[0]),
				                                         identityCache, identityDumper, changeSetDumper,
				                                         revisionDumper, branchDumper, handleDumper, graphDumper,
				                                         depotDumper, renamingDumper, integrationDumper, tagDumper);
				es.execute(runner);
			}
			
			es.shutdown();
			System.out.println("Tasks deployed.");
			
			final boolean ret = es.awaitTermination(30, TimeUnit.DAYS);
			System.out.println("————————————————————————————————————————");
			
			identityDumper.terminate();
			changeSetDumper.terminate();
			revisionDumper.terminate();
			branchDumper.terminate();
			handleDumper.terminate();
			graphDumper.terminate();
			depotDumper.terminate();
			renamingDumper.terminate();
			integrationDumper.terminate();
			tagDumper.terminate();
			
			identityDumper.join();
			changeSetDumper.join();
			revisionDumper.join();
			branchDumper.join();
			handleDumper.join();
			graphDumper.join();
			depotDumper.join();
			renamingDumper.join();
			integrationDumper.join();
			tagDumper.join();
			
			System.out.println("Creating primary keys.");
			database.createPrimaryKeys();
			System.out.println("Creating indexes.");
			database.createIndexes();
			System.out.println("Creating constraints.");
			database.createConstraints();
			System.out.println("Shutting down database.");
			database.close();
			
			System.out.println("All tasks are finished. Timeout: " + !ret);
		} catch (final URISyntaxException | SQLException | InterruptedException | IOException e) {
			Logger.error(e);
		} catch (final ParseException e) {
			printHelp(options);
			System.exit(EXIT_ERR_SETTINGS);
		}
	}
	
	/**
	 * Prints the help.
	 *
	 * @param options
	 *            the options
	 */
	private static void printHelp(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(120);
		formatter.printHelp("mozkito-versions", options);
	}
	
}
