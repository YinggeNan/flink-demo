package flink.chaptor05;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Calendar;
import java.util.Random;

/**
 * @Author yingge
 * @Date 2022/5/31 20:55
 */
public class ClickSource implements SourceFunction<Event> {
    // declare a flag
    private Boolean running = true;
    @Override
    public void run(SourceContext<Event> ctx) throws Exception {

        // random seed
        Random random = new Random();

        // define the set of data
        String[] users = {"Mary","Alice","Bob","Cary"};
        String[] urls= {"./home","./cart","./fav","prod?id=100","./prod?id=10"};

        // cycle to generate data
        while(running){
            String user = users[random.nextInt(users.length)];
            String url = urls[random.nextInt(users.length)];
            Long timestamp = Calendar.getInstance().getTimeInMillis();
            ctx.collect(new Event(user,url,timestamp));

            // reduce event generation rate and reduce system stress
            Thread.sleep(1000L);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
