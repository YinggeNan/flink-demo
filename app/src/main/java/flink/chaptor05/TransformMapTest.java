package flink.chaptor05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author yingge
 * 使用lambda进行map操作后，或许需要对map后的data stream使用return返回类型告诉flink map操作后的数据类型
 * @Date 2022/5/31 23:54
 */
public class TransformMapTest {
    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // read from elements
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L));

        // transform to extract the user field
        // 1. use custom defined class to implement the MapFunction
        SingleOutputStreamOperator<String> result = elements.map(new MyMapper());

        // 2. use anonymous class to implement the MapFunction
        SingleOutputStreamOperator<String> result2 = elements.map(new MapFunction<Event, String>() {
            @Override
            public String map(Event value) throws Exception {
                return value.user;
            }
        });

        // 3. use lambda function to implement MapFunction
        // but if we use lambda express, flink maybe can't know the type of the parameter,
        // so we need to give flink what type the parameter just is
        SingleOutputStreamOperator<String> result3 = elements.map(value -> value.user);
//        result.print();
//        result2.print();
        result3.print();
        env.execute();


    }
    // flink can just know the type of the parameters
    public static class MyMapper implements MapFunction<Event, String>{
        @Override
        public String map(Event value) throws Exception {
             return value.user;
        }
    }
}
