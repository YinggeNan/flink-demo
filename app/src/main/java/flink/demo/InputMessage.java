package flink.demo;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * @Author yingge
 * @Date 2022/5/28 1:41
 */
@JsonSerialize
public class InputMessage {
    String sender;
    String recipient;
    LocalDateTime sentAt;
    String message;

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
