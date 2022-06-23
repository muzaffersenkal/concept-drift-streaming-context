package conceptdrift.sources;

import conceptdrift.Transaction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Random;

public class FileSource implements SourceFunction<Transaction> {


    private boolean running=true;
    private String filePath;



    private Random random = new Random();



    public FileSource(String path)
    {
        filePath = path;

    }

    @Override
    public void run(SourceContext<Transaction> sourceContext) throws Exception
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            line = reader.readLine();
            long firstIngestionTime = System.currentTimeMillis();
            long firstTimestamp = 0;
            while (running && line != null)
            {
                Transaction se;
                long eventTimestamp; float temperature;
                String[] data = line.split(",");

                eventTimestamp = Long.parseLong(data[0]);
                // temperature = Math.round(((random.nextGaussian()*5)+20)*100.0)/100.0;
                temperature = Float.parseFloat(data[1]);
                if (firstTimestamp == 0)
                {
                    firstTimestamp = eventTimestamp;
                }
                se = new Transaction(1, firstIngestionTime + (eventTimestamp - firstTimestamp), temperature);

                sourceContext.collect(se);
                //sourceContext.collectWithTimestamp(se, se.getIngestionTimestamp());

                line = reader.readLine();
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
