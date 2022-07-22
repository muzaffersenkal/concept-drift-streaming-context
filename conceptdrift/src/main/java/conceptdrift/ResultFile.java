package conceptdrift;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ResultFile {
    PrintWriter out = null;


    public void writeToFile(Transaction transaction, int index, String algorithm, String type){
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("Data/result/result_"+ type +"_"+algorithm+".csv", true)));
            out.println(String.valueOf(transaction.getIngestionTimestamp())+","+String.valueOf(transaction.getEventTimestamp())+","+String.valueOf(index)+","+algorithm);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
