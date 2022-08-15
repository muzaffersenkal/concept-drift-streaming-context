package conceptdrift;

import conceptdrift.algorithms.*;
import conceptdrift.test.ADWIN2;
import conceptdrift.utils.ApiService;
import conceptdrift.alert.Alert;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class DriftDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

	private boolean apiAlert = false;
	private boolean saveResult = false;
	private static final long serialVersionUID = 1L;
	private transient ApiService api;
	private int index = 0;
	public transient AbstractDetectorAlgorithm detector;
	public String algorithm;
	public ResultFile resultFile;

	public DriftDetector(String algorithm) {
		this.algorithm = algorithm;
	}

	public DriftDetector(boolean apiAlert, String algorithm) {
		this.apiAlert = apiAlert;
		this.algorithm = algorithm;
	}

	public DriftDetector(boolean apiAlert, String algorithm, boolean saveResult) {
		this.apiAlert = apiAlert;
		this.algorithm = algorithm;
		this.saveResult = saveResult;
	}


	@Override
	public void open(Configuration parameters) {
		api = new ApiService();
		if (algorithm.equals("ADWIN")){
				detector = new ADWIN(0.002);
		} else if (algorithm.equals("GMADM")){
			detector = new GeometricMovingAverageDM();
		} else if (algorithm.equals("CUSUM")){
			detector = new CusumDM();
		} else if (algorithm.equals("PageHinkley")){
			detector = new PageHinkleyDM();
		} else if (algorithm.equals("DDM")){
			detector = new DDM();
		}

		resultFile = new ResultFile();

		//System.out.println(parameters);
	}

	@Override
	public void processElement(
			Transaction transaction,
			Context context,
			Collector<Alert> collector) throws Exception {

		double latency =  transaction.getLatency();
		index = index + 1;
		detector.input(latency);
		if(detector.getChange()){
			System.out.println("Change Detected: " + String.valueOf(index) +" " + transaction.toString());
			Alert alert = new Alert();
			alert.setId(index);
			alert.setDateTime(transaction.getEventTimestamp());
			collector.collect(alert);
			if(apiAlert){
				api.sendDrift(detector.name,transaction.getEventTimestamp(), transaction.getIngestionTimestamp());
			}

			if(saveResult){
				resultFile.writeToFile(transaction, index, algorithm, "sudden");
			}
			// do not forget to reset detector
			detector.resetLearning();
		}



	}

}