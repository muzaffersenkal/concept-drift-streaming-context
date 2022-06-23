package conceptdrift;

import conceptdrift.algorithms.ADWIN;
import conceptdrift.utils.ApiService;
import org.apache.flink.api.common.functions.MapFunction;
import conceptdrift.alert.Alert;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class DriftDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

	private static final long serialVersionUID = 1L;

	private static final double SMALL_AMOUNT = 1.00;
	private static final double LARGE_AMOUNT = 500.00;
	private static final long ONE_MINUTE = 60 * 1000;
	private transient ADWIN adwin;
	private transient ApiService api;
	private int index = 0;


	@Override
	public void open(Configuration parameters) {

		adwin = new ADWIN(0.002);
		api = new ApiService();
	}

	@Override
	public void processElement(
			Transaction transaction,
			Context context,
			Collector<Alert> collector) throws Exception {

		long now = System.currentTimeMillis();
		double diff =  (now - transaction.getEventTimestamp())/1000.0;
		index = index +1;
		System.out.println("diff: "+String.valueOf(diff));
		if(adwin.setInput(diff)){
			//Input data into Adwin
			System.out.println("Change Detected: "+String.valueOf(index));

			Alert alert = new Alert();
			alert.setId(transaction.getEventTimestamp());
			alert.setDateTime(transaction.getEventTimestamp());
			collector.collect(alert);
			api.sendDrift("Adwin",now, now);
			// do not forget to reset adwin
			adwin.resetChange();

		}



	}

}