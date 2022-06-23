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

import conceptdrift.sources.FileSource;
import conceptdrift.watermark.BoundedOutOfOrdernessGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;


public class WatermarkJob {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setParallelism(1);
		// DataStream< String > data =  env.socketTextStream("localhost", 9090);
		String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/conceptdrift/Data/sampledata.txt";

		DataStream<Transaction> transactions = env
				.addSource(new FileSource(dataURL))
				.name("transactions");

		WatermarkStrategy<Transaction> ws = (ctx -> new BoundedOutOfOrdernessGenerator());

		ws = ws.withTimestampAssigner((r, ts) -> r.getEventTimestamp());

		DataStream<Transaction> withTimestampsAndWatermarks = transactions.assignTimestampsAndWatermarks(ws);

		withTimestampsAndWatermarks
				.keyBy(Transaction::getAccountId)
				.window(TumblingEventTimeWindows.of(Time.seconds(10)))
				.process(new ProcessElement())
				.writeAsText("test.csv", FileSystem.WriteMode.OVERWRITE);



		/* DataStream<Alert> alerts = transactions
				.keyBy(Transaction::getAccountId)
				.process(new DriftDetector())
				.name("drift-detector");



		alerts
			.addSink(new AlertSink())
			.name("send-alerts");


		transactions.keyBy(Transaction::getAccountId).map(new MapFunction<Transaction, Tuple2<Long, Long>>() {
			@Override
			public Tuple2<Long, Long> map(Transaction transaction) throws Exception {
				return new Tuple2<Long, Long>(transaction.getIngestionTimestamp(), transaction.getTimestamp());
			}
		}).writeAsCsv("Data/output.csv", FileSystem.WriteMode.OVERWRITE);*/

		env.execute("Drift Detection");
	}
}
