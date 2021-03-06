/*
 * Copyright 2015 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package science.atlarge.graphalytics.graphmat;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.longs.Long2LongMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all jobs in the GraphMat benchmark suite. Configures and executes a GraphMat job using the parameters
 * and executable specified by the subclass for a specific algorithm.
 *
 * @author Yong Guo
 * @author Tim Hegeman
 */
public abstract class GraphmatJob {
	private static final Logger LOG = LogManager.getLogger(GraphmatJob.class);

	protected final Configuration config;
	protected final String graphPath;
	protected final Long2LongMap vertexTranslation;
	protected String outputPath;
	protected String jobId;

	public GraphmatJob(Configuration config, String graphPath, Long2LongMap vertexTranslation, String jobId) {
		this.config = config;
		this.graphPath = graphPath;
		this.outputPath = null;
		this.vertexTranslation = vertexTranslation;
		this.jobId = jobId;
	}

	public void setOutputPath(String file) {
		this.outputPath = file;
	}

	abstract protected String getExecutable();
	abstract protected void addJobArguments(List<String> args);

	public void execute() throws IOException, InterruptedException {
		List<String> args = new ArrayList<>();
		args.add(graphPath);
		addJobArguments(args);

		if (outputPath != null) {
			args.add(outputPath);
		}

		String cmdFormat = (config.getBoolean(GraphmatPlatform.ENABLE_SLURM_KEY)) ?
				config.getString(GraphmatPlatform.RUN_COMMAND_FORMAT_KEY, "%s %s") :
				config.getString(GraphmatPlatform.RUN_COMMAND_NO_SLURM_FORMAT_KEY, "%s %s");

		GraphmatPlatform.runCommand(cmdFormat, GraphmatPlatform.BINARY_DIRECTORY + "/" + getExecutable(), args);
	}
}
