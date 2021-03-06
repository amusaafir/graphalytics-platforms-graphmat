#!/bin/sh
#
# Copyright 2015 Delft University of Technology
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


# Ensure the configuration file exists
rootdir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))/../../
config="${rootdir}/config/"
if [ ! -f "$config/platform.properties" ]; then
	echo "Missing mandatory configuration file: $config/platform.properties" >&2
	exit 1
fi


# Construct the classpath
GRAPHMAT_HOME=$(grep -E "^platform.graphmat.home[	 ]*[:=]" $config/platform.properties | sed 's/platform.graphmat.home[\t ]*[:=][\t ]*\([^\t ]*\).*/\1/g' | head -n 1)
if [ -z GRAPHMAT_HOME ]; then
    echo "Error: home directory for Graphmat not specified."
    echo "Define the environment variable \$GRAPHMAT_HOME or modify platform.graphmat.home in $config/platform.properties"
    exit 1
fi
GRANULA_ENABLED=$(grep -E "^benchmark.run.granula.enabled[	 ]*[:=]" $config/granula.properties | sed 's/benchmark.run.granula.enabled[\t ]*[:=][\t ]*\([^\t ]*\).*/\1/g' | head -n 1)


# Build binaries
#module unload openmpi
#module unload openmpi/gcc
#module load intel/compiler
#module load intel-mpi
#module load intel/mkl

mkdir -p bin/standard
(cd bin/standard && cmake -DCMAKE_BUILD_TYPE=Release ../../src/main/c -DGRAPHMAT_HOME=$GRAPHMAT_HOME && make all VERBOSE=1)

if [ "$GRANULA_ENABLED" = "true" ] ; then
 mkdir -p bin/granula
 (cd bin/granula && cmake -DCMAKE_BUILD_TYPE=Release -DGRANULA=1 ../../src/main/c -DGRAPHMAT_HOME=$GRAPHMAT_HOME && make all VERBOSE=1)
fi

if [ $? -ne 0 ]
then
    echo "Compilation failed"
    exit 1
fi
