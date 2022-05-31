package flink.chaptor05;

import java.sql.Timestamp;

/**
 * @Author yingge
 * @Date 2022/5/31 16:03
 */
public class Event {
    public Event(String user, String url, Long timestamp) {
        this.user = user;
        this.url = url;
        this.timestamp = timestamp;
    }
    public Event(){

    }
    @Override
    public String toString() {
        return "Event{" +
                "user='" + user + '\'' +
                ", url='" + url + '\'' +
                ", timestamp=" + new Timestamp(timestamp) +
                '}';
    }

    public String user;
    public String url;
    public Long timestamp;
}
