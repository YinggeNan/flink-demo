package flink.demo;

import org.apache.flink.api.common.functions.MapFunction;

/**
 * @Author yingge
 * @Date 2022/5/28 1:39
 */
public class WordsCapitalizer implements MapFunction<String, String> {
    @Override
    public String map(String s) {
        return s.toUpperCase();
    }

}
