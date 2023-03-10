package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.security.Principal;
import java.util.Properties;

public class SourceKafkaTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//         checkpoint设置
//         每隔1s进行启动一个检查点【设置checkpoint的周期】
//         env.enableCheckpointing(1000);
//         设置模式为：exactly_one，仅一次语义
//         env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//         确保检查点之间有1s的时间间隔【checkpoint最小间隔】
//         env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
//         检查点必须在1s之内完成，或者被丢弃【checkpoint超时时间】
//         env.getCheckpointConfig().setCheckpointTimeout(1000);
//         同一时间只允许进行一次检查点
//         env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//         表示一旦Flink程序被cancel后，会保留checkpoint数据，以便根据实际需要恢复到指定的checkpoint
//         env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//         设置statebackend,将检查点保存在hdfs上面，默认保存在内存中。这里先保存到本地
//         env.setStateBackend(new FsStateBackend("file:///F:/kafkaTool/aaa"));
//         Kafka相关设置val properties = new Properties()
//         properties.setProperty("bootstrap.servers", "hadoop:9091,hadoop:9092,hadoop:9093");
//         properties.setProperty("group.id", "consumer-group");
//         properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//         properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//         properties.setProperty("auto.offset.reset", "latest");
//         默认是latest，当第一次运行，会读取最近提交的offset
//         properties.setProperty("enable.auto.commit", "true");
//         默认是trueval flinkKafkaConsumer011 = new FlinkKafkaConsumer011[String]("GMALL_EVENT_0105", new SimpleStringSchema(), properties)
//         设置根据程序checkpoint进行offset提交
//         flinkKafkaConsumer011.setCommitOffsetsOnCheckpoints(true);
//         flinkKafkaConsumer011.setStartFromGroupOffsets();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");

        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<String>(
                "test",
                new SimpleStringSchema(),
                properties
        ));
        System.out.println(stream);


        stream.print("Kafka");

        env.execute();
    }
}

