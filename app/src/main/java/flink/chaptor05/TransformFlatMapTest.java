package flink.chaptor05;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Author yingge
 * @Date 2022/6/1 0:20
 */
public class TransformFlatMapTest {
    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // read from elements
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L));
        SingleOutputStreamOperator<String> result = elements.flatMap(new MyFlatMap());
        // need add type info ,because of using lambda, and java' type case earse
        SingleOutputStreamOperator<Object> result2 = elements.flatMap((FlatMapFunction<Event, Object>) (value, out) -> {
            if (StringUtils.endsWith(value.user, "Mary")) {
                out.collect(value.user);
            } else if (StringUtils.endsWith(value.user, "Bob")) {
                out.collect(value.user);
                out.collect(value.url);
                out.collect(value.timestamp.toString());
            }
        }).returns(new TypeHint<Object>() {
        });
//        result.print();
        result2.print();
        env.execute();

    }
    // flink can just know the type of the parameters
    public static class MyFlatMap implements FlatMapFunction<Event, String> {
        @Override
        public void flatMap(Event value, Collector<String> out) throws Exception {
            out.collect(value.user);
            out.collect(value.url);
            out.collect(value.timestamp.toString());
        }
    }
}
