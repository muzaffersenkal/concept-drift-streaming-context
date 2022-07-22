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

        ADWIN2 adwin=new ADWIN2(.1); // Init Adwin with delta=.01
        CusumDM cusum = new CusumDM();
        int counter = 0;
        String dataURL  = "/Users/muzaffersenkal/Desktop/Dissertation/FlinkProject/conceptdrift/Data/gradual_new_drift.csv";

        BufferedReader reader = new BufferedReader(new FileReader(dataURL));
        String line;
        line = reader.readLine();
        while (line != null)
        {

            String[] data = line.split(",");

            double latency = Double.parseDouble(df.format(Double.parseDouble(data[2])));
            // adwin.input(latency);
            //if(cusum.getChange()){
            if(adwin.setInput(latency)) {
                //Input data into Adwin
                System.out.println("Change Detected: "+latency);
                counter = counter + 1;
                adwin.resetChange();
            }


            line = reader.readLine();
        }
        reader.close();


        System.out.println("Count:"+counter);
    }


}
