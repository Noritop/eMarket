package if4030.kafka;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.Grouped;

public class Serialized {

    public static Grouped<String, String> with(Serde<Integer> integer, Serde<Long> long1) {
        return null;
    }

}
