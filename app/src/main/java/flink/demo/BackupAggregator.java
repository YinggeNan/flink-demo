package flink.demo;

import org.apache.flink.api.common.functions.AggregateFunction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yingge
 * @Date 2022/5/28 1:50
 */
public class BackupAggregator
        implements AggregateFunction<InputMessage, List<InputMessage>, Backup> {

    @Override
    public List<InputMessage> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<InputMessage> add(
            InputMessage inputMessage,
            List<InputMessage> inputMessages) {
        inputMessages.add(inputMessage);
        return inputMessages;
    }

    @Override
    public Backup getResult(List<InputMessage> inputMessages) {
        return new Backup(inputMessages, LocalDateTime.now());
    }

    @Override
    public List<InputMessage> merge(List<InputMessage> inputMessages,
                                    List<InputMessage> acc1) {
        inputMessages.addAll(acc1);
        return inputMessages;
    }
}