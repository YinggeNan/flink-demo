package flink.chaptor05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author yingge
 * @Date 2022/6/1 0:43
 */
public class TransformSimpleAggTest {
    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // read from elements
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart1", 2100L),
                new Event("Bob", "./prod?id=1", 2200L),
                new Event("Bob", "./prod?id=2", 3200L),
                new Event("Bob", "./cart2", 4000L));
        // aggregate after key grouping, extract the last visit data for one user
        // max(timestamp) means get the biggest timestamp for all user data has been arrived,
        // and use record that has the biggest timestamp to emit to next operator
        // pay attention that we only can use filed name for pojo parameter
        // if the parameter is tuple type, we can use index
        elements.keyBy(new KeySelector<Event, String>() {
            @Override
            public String getKey(Event value) throws Exception {
                return value.user;
            }
        }).max("timestamp").print("max:");
        elements.keyBy(data->data.user).maxBy("timestamp").print("maxBy:");
        env.execute();
    }
}
