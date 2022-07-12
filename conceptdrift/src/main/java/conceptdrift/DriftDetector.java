package conceptdrift;

import conceptdrift.algorithms.*;
import conceptdrift.utils.ApiService;
import org.apache.flink.api.common.functions.MapFunction;
import conceptdrift.alert.Alert;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class DriftDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

	private boolean apiAlert = false;
	private static final long serialVersionUID = 1L;
	private static final double SMALL_AMOUNT = 1.00;
	private static final double LARGE_AMOUNT = 500.00;
	private static final long ONE_MINUTE = 60 * 1000;
	private transient ADWIN adwin;
	private transient GeometricMovingAverageDM geo;
	private transient ApiService api;
	private int index = 0;
	public transient AbstractDetectorAlgorithm detector;
	public String algorithm;

	public DriftDetector(boolean apiAlert, String algorithm) {
		this.apiAlert = apiAlert;
		this.algorithm = algorithm;
	}


	@Override
	public void open(Configuration parameters) {
		api = new ApiService();
		if (algorithm.equals("ADWIN")){
			detector = new ADWIN();
		} else if (algorithm.equals("GMADM")){
			detector = new GeometricMovingAverageDM();
		} else if (algorithm.equals("CUSUM")){
			detector = new CusumDM();
		} else if (algorithm.equals("PageHinkley")){
			detector = new CusumDM();
		} else if (algorithm.equals("DDM")){
			detector = new DDM();
		}

		//System.out.println(parameters);
	}

	@Override
	public void processElement(
			Transaction transaction,
			Context context,
			Collector<Alert> collector) throws Exception {

		long now = System.currentTimeMillis();
		double latency =  transaction.getLatency();
		index = index + 1;
		detector.input(latency);
		if(detector.getChange()){
			//Input data into Adwin
			System.out.println("Change Detected: "+String.valueOf(index) +" " + transaction.toString());
			Alert alert = new Alert();
			alert.setId(index);
			alert.setDateTime(transaction.getEventTimestamp());
			collector.collect(alert);
			if(apiAlert){
				api.sendDrift(detector.name,transaction.getEventTimestamp(), transaction.getIngestionTimestamp());
			}
			// do not forget to reset adwin
			detector.resetLearning();
		}

		/*
		geo.input(diff);
		if(geo.getChange()) {
			//Input data into Adwin
			System.out.println("Change Detected: " + String.valueOf(index));

			Alert alert = new Alert();
			alert.setId(transaction.getEventTimestamp());
			alert.setDateTime(transaction.getEventTimestamp());
			collector.collect(alert);
			api.sendDrift("Adwin",now, now);
			// do not forget to reset adwin
			geo.resetLearning();

		}
		*/



	}

}