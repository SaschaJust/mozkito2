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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.commons.URIUtils;
import org.mozkito.skeleton.datastructures.Tuple;
import org.mozkito.skeleton.exec.CommandExecutor;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * @author Sascha Just
 *
 */
public class Main {
	
	/**
	 * @return
	 */
	private static Options createOptions() {
		final Options options = new Options();
		
		Option option = new Option("r", "repository", true, "The URI to the repository");
		option.setArgName("URI");
		option.setRequired(true);
		option.setType(URI.class);
		options.addOption(option);
		
		option = new Option("w", "working-directory", true, "The working directory.");
		option.setArgName("DIR");
		option.setRequired(false);
		option.setType(File.class);
		options.addOption(option);
		
		option = new Option("d", "database-name", true, "The name of the database.");
		option.setArgName("DB_NAME");
		option.setRequired(true);
		option.setType(String.class);
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
	 */
	public static void main(final String[] args) throws IOException,
	                                            SQLException,
	                                            URISyntaxException,
	                                            InterruptedException,
	                                            ParseException {
		// create the command line parser
		final CommandLineParser parser = new DefaultParser();
		
		final Options options = createOptions();
		// create the Options
		
		try {
			final CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("mozkito-versions", options);
				System.exit(0);
			}
		} catch (final ParseException e) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("mozkito-versions", options);
			System.exit(1);
		}
		
		CommandExecutor.redirectErrorStream = false;
		final File workDir = new File("/tmp");
		final File cloneDir = new File("/tmp/mozkito_test");
		final URI uri = new URI("file:///tmp/mozkito_bare.git");
		
		final String databaseName = "test";
		
		Logger.info("Establishing database connection and creating pool.");
		final SequelDatabase database = new SequelDatabase("jdbc:derby:" + databaseName + ";create=true",
		                                                   new Properties());
		
		Logger.info("Cloning depot.");
		final Depot depot = new Depot("test", uri);
		
		final Tuple<Integer, List<String>> result = CommandExecutor.execute("git",
		                                                                    new String[] { "clone", "-b", "master",
		                                                                            "-n", "-q",
		                                                                            URIUtils.uri2String(uri),
		                                                                            "/tmp/mozkito_test" }, workDir,
		                                                                    null,
		                                                                    new HashMap<String, String>());
		if (result.getFirst() != 0) {
			if (Logger.logError()) {
				for (final String resLine : result.getSecond()) {
					Logger.error.println(resLine);
				}
			}
			System.exit(result.getFirst());
		}
		
		Logger.info("Spawning BranchMiner.");
		
		final BranchMiner branchMiner = new BranchMiner(cloneDir, database, depot);
		final Thread bmThread = new Thread(branchMiner);
		bmThread.start();
		bmThread.join();
		
		final DepotGraph graph = new DepotGraph(depot);
		
		Logger.info("Spawning ChangeSetMiner.");
		final ChangeSetMiner changeSetMiner = new ChangeSetMiner(cloneDir, database, depot, graph,
		                                                         branchMiner.getBrancHeads());
		final Thread csmThread = new Thread(changeSetMiner);
		csmThread.start();
		csmThread.join();
		
		Logger.info("Spawning GraphBuilder.");
		final GraphMiner graphMiner = new GraphMiner(cloneDir, database, graph, changeSetMiner.getChangeSets());
		final Thread gmThread = new Thread(graphMiner);
		gmThread.start();
		gmThread.join();
		
		Logger.info("Analyzing.");
		final ChangeSet invest = changeSetMiner.getChangeSets().get("3b3d15c97064284857c7a7b68a4c891b7164159e");
		final Branch monitored = graph.getBranches().stream().filter(b -> "master".equals(b.getName())).findFirst()
		                              .get();
		final List<ChangeSet> path = graph.getIntegrationPath(invest, monitored);
		for (final ChangeSet changeSet : path) {
			Logger.info(changeSet.toString());
		}
	}
}
