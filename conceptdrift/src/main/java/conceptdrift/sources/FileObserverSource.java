package conceptdrift.sources;

import conceptdrift.Transaction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Random;

public class FileObserverSource implements SourceFunction<Transaction> {


    private boolean running=true;
    private String filePath;



    private Random random = new Random();



    public FileObserverSource(String path)
    {
        filePath = path;

    }

    @Override
    public void run(SourceContext<Transaction> sourceContext) throws Exception
    {
        try
        {
            FileInputStream fstream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fstream));
            String line;

            long firstIngestionTime = System.currentTimeMillis();
            long firstTimestamp = 0;
            while (running) {
                line = reader.readLine();
                if (line == null) {

                    Thread.sleep(100);//waiting till the new content
                } else {
                    Transaction se;
                    long eventTimestamp;
                    float temperature;
                    String[] data = line.split(",");

                    eventTimestamp = Long.parseLong(data[0]);
                    // temperature = Math.round(((random.nextGaussian()*5)+20)*100.0)/100.0;
                    temperature = Float.parseFloat(data[1]);
                    if (firstTimestamp == 0) {
                        firstTimestamp = eventTimestamp;
                    }
                    se = new Transaction(1, firstIngestionTime + (eventTimestamp - firstTimestamp), temperature);

                    sourceContext.collect(se);
                    //sourceContext.collectWithTimestamp(se, se.getIngestionTimestamp());


                }
            }
            reader.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();

        }

    }

    @Override
    public void cancel() {
        running = false;
    }
}
