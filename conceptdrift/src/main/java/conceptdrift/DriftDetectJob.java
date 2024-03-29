/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conceptdrift;

import conceptdrift.alert.Alert;
import conceptdrift.alert.AlertSink;

import conceptdrift.sources.FileSourceRaw;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;



public class DriftDetectJob {
	public static void main(String[] args) throws Exception {

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		ParameterTool parameters = ParameterTool.fromArgs(args);
		String algorithm = parameters.get("algorithm","ADWIN");
		String dataURL = parameters.get("input","/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/Data/data_drift_20.csv");
		String outputFolder = parameters.get("outputFolder","/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/Result/");

		env.setParallelism(1);
		/* env.setRuntimeMode(RuntimeExecutionMode.STREAMING); */


		DataStream<Transaction> transactions = env
				.addSource(new FileSourceRaw(dataURL))
				.name("transactions");

		DataStream<Alert> alerts = transactions
				.keyBy(Transaction::getAccountId)
				.process(new DriftDetector( algorithm, outputFolder, false))
				.name("drift-detector");

		alerts
			.addSink(new AlertSink())
			.name("send-alerts");

		env.execute("Drift Detection");
	}

}
