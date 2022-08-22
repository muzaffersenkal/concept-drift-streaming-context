package conceptdrift.test;


import conceptdrift.Transaction;
import conceptdrift.algorithms.CusumDM;
import conceptdrift.algorithms.PageHinkleyDM;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTest {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static PrintWriter out = null;
    public String outputFolder;

    public static List generateThresholds(){
        List<Integer> thresholds = new ArrayList<Integer>();
        thresholds.add(5);
        thresholds.add(10);
        thresholds.add(20);
        thresholds.add(30);
        thresholds.add(40);
        thresholds.add(50);
        return thresholds;

    }

    public static void writeToFile(Transaction transaction, int index, String algorithm, int threshold){
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/Result/parameter/result_test_"+algorithm+".csv", true)));
            out.println(String.valueOf(transaction.getIngestionTimestamp())+","+String.valueOf(transaction.getEventTimestamp())+","+String.valueOf(index)+","+algorithm);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<Integer> thresholds = generateThresholds();
        String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/Data/data_drift_20.csv";

        for (int i = 0; i < thresholds.size(); i++) {
            System.out.println(thresholds.get(i));
            PageHinkleyDM detector=new PageHinkleyDM(thresholds.get(i));
            //CusumDM detector=new CusumDM(thresholds.get(i));
            int counter = 0;
            int counterLine = 0;

            BufferedReader reader = new BufferedReader(new FileReader(dataURL));
            String line;
            line = reader.readLine();
            while (line != null)
            {
                counterLine = counterLine + 1;

                // double latency = Double.parseDouble(df.format(Double.parseDouble(data[2])));

                Transaction transaction;

                double amount = Math.random() * 49 + 1;
                String[] data = line.split(",");

                long ingestionTime = Long.parseLong(data[0]) ;
                long eventTimestamp = Long.parseLong(data[1]) ;

                transaction = new Transaction(1, ingestionTime, eventTimestamp, amount);

                detector.input(transaction.getLatency());
                if(detector.getChange()) {
                    //System.out.println("Change Detected: "+latency);
                    //System.out.println("Change Line: "+counterLine);
                    counter = counter + 1;
                    detector.resetLearning();
                    writeToFile(transaction,counterLine,detector.name+"_"+String.valueOf(thresholds.get(i)),thresholds.get(i));
                }


                line = reader.readLine();
            }
            reader.close();


            System.out.println("Count:"+counter);
        }

    }




}