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
//import conceptdrift.sources.FileSource;
import conceptdrift.sources.FileObserverSource;
import conceptdrift.sources.FileSource;
import conceptdrift.sources.FileSourceRaw;
import conceptdrift.watermark.BoundedOutOfOrdernessGenerator;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.io.FileInputFormat;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;


public class DriftDetectJob {
	public static void main(String[] args) throws Exception {

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		ParameterTool parameters = ParameterTool.fromArgs(args);
		String algorithm = parameters.get("algorithm","ADWIN");
		boolean apiAlert = parameters.getBoolean("apiAlert",false);

		env.setParallelism(1);
		/* env.setRuntimeMode(RuntimeExecutionMode.STREAMING); */
		// String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/conceptdrift/Data/output.csv";

		String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/conceptdrift/Data/sudden_drift.csv";

		DataStream<Transaction> transactions = env
				.addSource(new FileSourceRaw(dataURL))
				.name("transactions");

		DataStream<Alert> alerts = transactions
				.keyBy(Transaction::getAccountId)
				.process(new DriftDetector(apiAlert, algorithm))
				.name("drift-detector");

		alerts
			.addSink(new AlertSink())
			.name("send-alerts");

		env.execute("Drift Detection");
	}

}
