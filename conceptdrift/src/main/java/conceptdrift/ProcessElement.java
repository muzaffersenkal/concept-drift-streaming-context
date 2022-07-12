package conceptdrift;

import org.apache.flink.api.common.functions.MapFunction;
import conceptdrift.alert.Alert;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Time;

public class ProcessElement extends ProcessWindowFunction<Transaction, Tuple3<TimeWindow, Long, Long>, Long, TimeWindow> {

    private static final long serialVersionUID = 1L;

    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;
    private static long TOTAL = 0;



/*	@Override
	public void processElement(
			Transaction transaction,
			Context context,
			Collector<Alert> collector) throws Exception {

		Alert alert = new Alert();
		alert.setId(transaction.getAccountId());
		alert.setDateTime(transaction.getTimestamp());

		collector.collect(alert);
	}
*/

    @Override
    public void process(Long aLong, ProcessWindowFunction<Transaction, Tuple3<TimeWindow, Long, Long>, Long, TimeWindow>.Context context, Iterable<Transaction> iterable, Collector<Tuple3<TimeWindow, Long, Long>> collector) throws Exception {
        long count = 0L;
        for (Transaction e : iterable) {
            count++;
            // System.out.println(e.getLatency());
        }
        TOTAL = TOTAL + count;
        // System.out.println("Num elements in Window (started="+context.window().getStart()+", end="+context.window().getEnd()+") is "+count+" TOTAL="+TOTAL);
        collector.collect(new Tuple3<>(context.window(), Long.valueOf(count), context.currentWatermark() - context.window().getEnd()));
    }
}