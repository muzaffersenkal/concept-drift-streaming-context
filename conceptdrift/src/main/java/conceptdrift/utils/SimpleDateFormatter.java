package conceptdrift.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatter {
    public static SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static String toDate(long timestamp) {
        return simpleDate.format(new Date(timestamp));
    }
}