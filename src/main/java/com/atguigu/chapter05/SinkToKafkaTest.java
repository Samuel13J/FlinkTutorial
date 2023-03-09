package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class SinkToKafkaTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         * 直接调用执行环境的并行度（全局）setParallelism -- 默认所有算子的并行度都为1。
         * 但一般不会这么用，因为在程序中对程序进行硬编码，会导致无法动态扩容
         * */
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");

        DataStreamSource<String> stream = env.readTextFile("input/clicks.csv");

        stream
                .addSink(new FlinkKafkaProducer<String>(
                        "test",
                        new SimpleStringSchema(),
                        properties
                ));

        env.execute();
    }
}

