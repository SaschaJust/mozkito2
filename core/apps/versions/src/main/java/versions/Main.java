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
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.mozkito.skeleton.commons.URIUtils;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

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
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SQLException
	 *             the SQL exception
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ParseException
	 *             the parse exception
	 */
	public static void main(final String[] args) throws IOException,
	                                            SQLException,
	                                            URISyntaxException,
	                                            InterruptedException,
	                                            ParseException {
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
			
			final File cloneDir = new File(workDir, "mozkito_test");
			final URI uri = new URI(line.hasOption("repository")
			                                                    ? line.getOptionValue("repository")
			                                                    : "file:///tmp/mozkito_bare.git");
			
			final String databaseName = line.hasOption("database-name")
			                                                           ? line.getOptionValue("database-name")
			                                                           : "mozkito-versions";
			
			Logger.info("Establishing database connection and creating pool.");
			final SequelDatabase database = new SequelDatabase("jdbc:derby:" + databaseName + ";create=true",
			                                                   new Properties());
			
			Logger.info("Cloning depot.");
			final Depot depot = new Depot("test", uri);
			
			final Command command = Command.execute("git",
			                                        new String[] { "clone", "-b", "master", "-n", "-q",
			                                                URIUtils.uri2String(uri), cloneDir.getAbsolutePath() },
			                                        workDir);
			command.waitFor();
			
			if (command.exitValue() != 0) {
				String resLine;
				if (Logger.logError()) {
					while ((resLine = command.nextErrput()) != null) {
						Logger.error.println(resLine);
					}
				}
				System.exit(command.exitValue());
			}
			
			Map<String, Branch> branchHeads;
			if (line.hasOption("mine-branches")) {
				Logger.info("Spawning BranchMiner.");
				
				final BranchMiner branchMiner = new BranchMiner(cloneDir, database, depot);
				final Thread bmThread = new Thread(branchMiner);
				bmThread.start();
				bmThread.join();
				branchHeads = branchMiner.getBranchHeads();
			} else {
				// TODO load from DB
				branchHeads = new HashMap<String, Branch>();
			}
			
			final DepotGraph graph = new DepotGraph(depot);
			final GraphAdapter graphAdapter = new GraphAdapter(database);
			graphAdapter.createScheme();
			Map<String, ChangeSet> changeSets = new HashMap<String, ChangeSet>();
			
			if (line.hasOption("mine-changesets")) {
				Logger.info("Spawning ChangeSetMiner.");
				final ChangeSetMiner changeSetMiner = new ChangeSetMiner(cloneDir, database, depot, graph, branchHeads);
				final Thread csmThread = new Thread(changeSetMiner);
				csmThread.start();
				csmThread.join();
				changeSets = changeSetMiner.getChangeSets();
			} else {
				// TODO load changesets and add to graph and the hashmap
			}
			
			if (line.hasOption("mine-graph")) {
				Logger.info("Spawning GraphBuilder.");
				final GraphMiner graphMiner = new GraphMiner(cloneDir, database, graph, changeSets);
				final Thread gmThread = new Thread(graphMiner);
				gmThread.start();
				gmThread.join();
				graphAdapter.save(graph);
				database.commit();
			}
			
			if (line.hasOption("mine-integration")) {
				Logger.info("Spawning IntegrationBuilder.");
				final ChangeSet invest = changeSets.get("d14d00ee51abf5f939651070e8eef4b3d9873aa0");
				
				final Branch monitored = graph.getBranches().stream().filter(b -> "master".equals(b.getName()))
				                              .findFirst().get();
				final Iterator<ChangeSet> monitoredChangeSets = graph.getChangeSets(monitored);
				
				while (monitoredChangeSets.hasNext()) {
					Logger.info(monitoredChangeSets.next().getCommitHash());
				}
				
				final List<ChangeSet> path = graph.getIntegrationPath(invest, monitored);
				for (final ChangeSet changeSet : path) {
					Logger.info(changeSet.toString());
				}
			}
		} catch (final ParseException e) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(120);
			formatter.printHelp("mozkito-versions", options);
			System.exit(1);
		}
	}
	
	public static void main2() {
		final String databaseName = "mozkito-test";
		final File workDir = new File("/tmp");
		final File baseDir = new File("/tmp");
		
		final Iterator<File> depotDirs = org.apache.commons.io.FileUtils.iterateFiles(baseDir, new IOFileFilter() {
			
			@Override
			public boolean accept(final File file) {
				return true;
			}
			
			@Override
			public boolean accept(final File dir,
			                      final String name) {
				return true;
			}
		}, new IOFileFilter() {
			
			@Override
			public boolean accept(final File file) {
				return true;
			}
			
			@Override
			public boolean accept(final File dir,
			                      final String name) {
				return true;
			}
		});
		
		File cloneDir;
		try {
			final SequelDatabase database = new SequelDatabase("", null);
			database.register(Depot.class, new DepotAdapter(database));
			database.register(Identity.class, new IdentityAdapter(database));
			database.register(ChangeSet.class, new ChangeSetAdapter(database));
			database.register(Revision.class, new RevisionAdapter(database));
			database.register(DepotGraph.class, new GraphAdapter(database));
			database.register(Branch.class, new BranchAdapter(database));
			database.register(Handle.class, new HandleAdapter(database));
			database.createScheme();
			
			final ISequelAdapter<Depot> adapter = database.getAdapter(Depot.class);
			
			final ExecutorService es = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() - 2);
			
			while (depotDirs.hasNext()) {
				cloneDir = depotDirs.next();
				
				final URI cloneURI = cloneDir.toURI();
				final String depotName = cloneDir.getName();
				final Depot depot = new Depot(depotName, cloneURI);
				adapter.save(depot);
				es.execute(new TaskRunner(workDir, cloneURI, database, new DepotGraph(depot)));
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
