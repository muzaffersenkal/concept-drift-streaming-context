package conceptdrift.sources;

import conceptdrift.Transaction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileSourceRaw implements SourceFunction<Transaction> {


    private boolean running=true;
    private String filePath;


    public FileSourceRaw(String path)
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
            while (running && line != null)
            {
                Transaction transaction;

                double amount = Math.random() * 49 + 1;
                String[] data = line.split(",");

                long ingestionTime = Long.parseLong(data[0]) ;
                long eventTimestamp = Long.parseLong(data[1]) ;

                transaction = new Transaction(1, ingestionTime, eventTimestamp, amount);

                sourceContext.collect(transaction);

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
