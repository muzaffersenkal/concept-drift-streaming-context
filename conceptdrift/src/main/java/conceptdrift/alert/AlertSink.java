package conceptdrift.alert;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import conceptdrift.alert.Alert;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PublicEvolving
public class AlertSink implements SinkFunction<Alert> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(org.apache.flink.walkthrough.common.sink.AlertSink.class);

    public AlertSink() {
    }

    public void invoke(Alert value, Context context) {
        LOG.info(value.toString());
    }
}
