package conceptdrift.watermark;

import conceptdrift.Transaction;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

/**
        * This generator generates watermarks assuming that elements arrive out of order,
        * but only to a certain degree. The latest elements for a certain timestamp t will arrive
        * at most n milliseconds after the earliest elements for timestamp t.
        */
public class BoundedOutOfOrdernessGenerator implements WatermarkGenerator<Transaction> {

    private final long maxOutOfOrderness = 3500; // 3.5 seconds

    private long currentMaxTimestamp;

    @Override
    public void onEvent(Transaction event, long eventTimestamp, WatermarkOutput output) {
        currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // emit the watermark as current highest timestamp minus the out-of-orderness bound
        output.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderness - 1));
    }

}