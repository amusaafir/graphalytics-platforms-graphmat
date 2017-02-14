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
package nl.tudelft.graphalytics.graphmat.algorithms.lcc;

import java.util.List;

import org.apache.commons.configuration.Configuration;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import nl.tudelft.graphalytics.graphmat.GraphMatJob;

public class LocalClusteringCoefficientJob extends GraphMatJob {

        private String isDirected;

	public LocalClusteringCoefficientJob(Configuration config, String graphPath, String isDirected, Long2LongMap vertexTranslation, String jobId) {
		super(config, graphPath, vertexTranslation, jobId);
                this.isDirected = isDirected;
	}
		
	@Override
	protected String getExecutable() {
		return "lcc";
	}

	@Override
	protected void addJobArguments(List<String> args) {
		args.add(jobId);
		args.add(isDirected);
	}
}
