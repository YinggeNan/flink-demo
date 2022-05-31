package flink.chaptor05;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Random;

/**
 * @Author yingge
 * @Date 2022/5/31 20:54
 */
public class SourceCustomTest {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
//        DataStreamSource<Event> customStream = env.addSource(new ClickSource());
        // can't set parallel thread for SourceFunction
//        DataStreamSource<Event> customStream = env.addSource(new ClickSource()).setParallelism(2);
        // parallel stream, can set parallel thread
        DataStreamSource<Integer> customStream = env.addSource(new ParallelSourceFunctionInstance()).setParallelism(2);
        customStream.print();
        env.execute();
    }

    // custom parallel SourceFunction
    public static class ParallelSourceFunctionInstance implements ParallelSourceFunction<Integer>{
        private Boolean running =true;
        private Random random = new Random();
        @Override
        public void run(SourceContext ctx) throws Exception {
            while(running){
                ctx.collect(random.nextInt());
            }
        }

        @Override
        public void cancel() {
            running=false;
        }
    }
}
