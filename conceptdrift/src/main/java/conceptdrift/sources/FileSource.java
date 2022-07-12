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
                long eventTimestamp; float amount;
                long ingestionTime = System.currentTimeMillis();
                String[] data = line.split(",");

                eventTimestamp = Long.parseLong(data[0]);
                amount = Float.parseFloat(data[1]);
                if (firstTimestamp == 0)
                {
                    firstTimestamp = eventTimestamp;
                }
                se = new Transaction(1, ingestionTime, firstIngestionTime + (eventTimestamp - firstTimestamp), amount);

                sourceContext.collect(se);

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
