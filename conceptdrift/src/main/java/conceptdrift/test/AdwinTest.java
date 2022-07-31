package conceptdrift.test;


import conceptdrift.Transaction;
import conceptdrift.algorithms.ADWIN;
import conceptdrift.algorithms.CusumDM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class AdwinTest {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) throws IOException {

        ADWIN2 adwin=new ADWIN2(0.002); // Init Adwin with delta=.01
        CusumDM cusum = new CusumDM();
        int counter = 0;
        int counterLine = 0;
        String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/conceptdrift/Data/data_drift_20.csv";

        BufferedReader reader = new BufferedReader(new FileReader(dataURL));
        String line;
        line = reader.readLine();
        while (line != null)
        {
            counterLine = counterLine + 1;
            String[] data = line.split(",");

            double latency = Double.parseDouble(df.format(Double.parseDouble(data[2])));
            // adwin.input(latency);
            //if(cusum.getChange()){
            if(adwin.setInput(latency, 0.002)) {
                //Input data into Adwin
                long bufferSize = adwin.getWidth();
                System.out.println("Change Detected: "+latency);
                System.out.println("Change Line: "+counterLine);
                counter = counter + 1;
                adwin.resetChange();
            }


            line = reader.readLine();
        }
        reader.close();


        System.out.println("Count:"+counter);
    }


}
