package flink.chaptor05;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

/**
 * @Author yingge
 * @Date 2022/5/31 16:12
 */
public class SourceTest {
    public static void main(String[] args) throws Exception {
        // create env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // set to 1 to see log in console for convinience.
        env.setParallelism(1);

        // 1.read from file, its actual a batch mode, bounded data
        // after read one line, deal with one line
        DataStreamSource<String> stream1 = env.readTextFile("input/clicks.txt");

        // 2. read from collections
        // we usually use collections to test code not in production
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(5);
        DataStreamSource<Integer> numStream = env.fromCollection(nums);
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Mary", "./home", 1000L));
        events.add(new Event("Bob", "./cart", 2000L));
        events.add(new Event("Mary", "./home", 3000L));
        DataStreamSource<Event> stream2 = env.fromCollection(events);
        stream1.print("1");
        numStream.print("nums");
        stream2.print("2");

        // 3.read from elements
        /// use for test too.
        DataStreamSource<Event> elements = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L));
        elements.print("3");
        env.execute();
    }
}
