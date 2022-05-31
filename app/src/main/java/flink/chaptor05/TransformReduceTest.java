package flink.chaptor05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author yingge
 * @Date 2022/6/1 1:10
 */
public class TransformReduceTest {
    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // read from elements
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart1", 2100L),
                new Event("Alice", "./prod?id=100", 2400L),
                new Event("Bob", "./prod?id=1", 2500L),
                new Event("Alice", "./prod?id=101", 3100L),
                new Event("Bob", "./prod?id=2", 3700L),
                new Event("Bob", "./cart2", 4000L));
        // find the user that most visited, so we need to record the numbers of every visit of every user

        // 1. count the numbers of visit per user
        // we can think keyby will partition by every different key, don't worry about the hash conflicts
        // after used keybe to partition, we can run parallel thread to deal with multiple partition in multiple slot
        SingleOutputStreamOperator<Tuple2<String, Long>> clickUser = elements.map(new MapFunction<Event, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(Event value) throws Exception {
                return Tuple2.of(value.user, 1L);
            }
        }).keyBy(data -> data.f0)
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    /**
                     * the current aggregated record returned will be saved as the newest state automatically
                     * @param value1
                     * @param value2
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                });
        // 2. pick the most active user
        // so we need to deal with in one slot, we can't partition dataSteam into multiple partitions
        SingleOutputStreamOperator<Tuple2<String, Long>> result = clickUser.keyBy(data -> "key").reduce(new ReduceFunction<Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                return value1.f1 > value2.f1 ? value1 : value2;
            }
        });
        result.print();
        env.execute();
    }



}
