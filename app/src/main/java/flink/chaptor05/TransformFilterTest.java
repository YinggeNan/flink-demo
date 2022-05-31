package flink.chaptor05;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author yingge
 * dont' need to return typeinfo, because filter don't modify the type of data stream
 * @Date 2022/6/1 0:06
 */
public class TransformFilterTest {
    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // read from elements
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L));
        // 1. pass in a object belong to a class implemented FilterFunction
        SingleOutputStreamOperator<Event> result = elements.filter(new MyFilterFunction());
        // 2. pass in a anonymous class
        // 3. pass in a lambada expression
        SingleOutputStreamOperator<Event> result3 = elements.filter(value -> StringUtils.endsWith(value.user, "Mary"));
        result.print();
        env.execute();
    }


    public static class MyFilterFunction implements FilterFunction<Event>{
        @Override
        public boolean filter(Event value) throws Exception {
            return StringUtils.endsWith(value.user, "Mary");
        }
    }
}
