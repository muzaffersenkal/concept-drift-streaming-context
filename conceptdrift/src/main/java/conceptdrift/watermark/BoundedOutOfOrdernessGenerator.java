package conceptdrift.watermark;

import conceptdrift.Transaction;
import conceptdrift.utils.SimpleDateFormatter;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
        * This generator generates watermarks assuming that elements arrive out of order,
        * but only to a certain degree. The latest elements for a certain timestamp t will arrive
        * at most n milliseconds after the earliest elements for timestamp t.
        */
public class BoundedOutOfOrdernessGenerator implements WatermarkGenerator<Transaction> {

    private final long maxOutOfOrderness = 3500; // 3.5 seconds

    private long currentMaxTimestamp;
    public long totalElements=0,totalOOOElements=0;
    private long numberOfGeneratedWatermarks=0;
    long currentWatermark=0;


    @Override
    public void onEvent(Transaction event, long eventTimestamp, WatermarkOutput output) {
        long timestamp = event.getEventTimestamp();
        if (timestamp < currentWatermark) {
            totalOOOElements++;
            System.out.println("\t  An event ingested " + SimpleDateFormatter.toDate(event.getIngestionTimestamp()) + " event timestamp ="+ SimpleDateFormatter.toDate(event.getEventTimestamp()) + " behind the watermark " + SimpleDateFormatter.toDate(currentWatermark));
        }
        totalElements++;
        currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);

    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {

        long nextWatermark = currentMaxTimestamp - maxOutOfOrderness - 1;

        //System.out.println("next watermark"+currentWatermark+"");

        if (nextWatermark > currentWatermark) {
            numberOfGeneratedWatermarks++;
            currentWatermark = nextWatermark;
            System.out.println("Generating a new watermark with timestamp (" + nextWatermark + ")" + SimpleDateFormatter.toDate(nextWatermark));
            System.out.println("Total number of generated watermarks "+numberOfGeneratedWatermarks);
            // emit the watermark as current highest timestamp minus the out-of-orderness bound
            output.emitWatermark(new Watermark(nextWatermark));
        }else{
            System.out.println("Total OOO Arrival "+totalOOOElements+" of total elements "+totalElements +" with percentage "+(double)totalOOOElements/totalElements);


        }

    }

}