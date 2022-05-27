package flink.demo;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;
import java.time.ZoneId;

/**
 * @Author yingge
 * @Date 2022/5/28 1:48
 */
public class InputMessageTimestampAssigner
        implements AssignerWithPunctuatedWatermarks<InputMessage> {

    @Override
    public long extractTimestamp(InputMessage element,
                                 long previousElementTimestamp) {
        ZoneId zoneId = ZoneId.systemDefault();
        return element.getSentAt().atZone(zoneId).toEpochSecond() * 1000;
    }

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(InputMessage lastElement,
                                              long extractedTimestamp) {
        return new Watermark(extractedTimestamp - 1500);
    }
}
