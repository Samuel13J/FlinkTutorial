//package com.atguigu.kafka;
//
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.TimeCharacteristic;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
//import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Properties;
//
///**
// * @author wangjie
// * @create 2023-03-03 14:16
// */
//public class Kafka2Hdfs {
//
//    private static Logger LOG = LoggerFactory.getLogger(Kafka2Hdfs.class);
//
//    public static void main(String[] args) {
//        if (args.length != 3) {
//            LOG.error("kafka(localhost:9092), hdfs(hdfs://kafka/logs), flink(parallelism=1) must be exist.");
//            return;
//        }
//        String bootStrapServer = args[0];
//        String hdfsPath = args[1];
//        int parallelism = Integer.parseInt(args[2]);
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.enableCheckpointing(5000);
//        env.setParallelism(parallelism);
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
////      topic cg
//        DataStream<String> transction = env.addSource(new FlinkKafkaConsumer010<>("test", new SimpleStringSchema(), configByKafkaServer(bootStrapServer)));
//
//        // Storage into hdfs
//        BucketingSink<String> sink = new BucketingSink<>(hdfsPath);
//
//        sink.setBucketer(new DateTimeBucketer<String>("yyyy-MM-dd"));
//
//        sink.setBatchSize(1024 * 1024 * 1024); // this is 1GB
//        sink.setBatchRolloverInterval(1000 * 60 * 60); // one hour producer a file into hdfs
//        transction.addSink(sink);
//
//        env.execute("Kafka2Hdfs");
//    }
//
//    private static Object configByKafkaServer(String bootStrapServer) {
//        Properties props = new Properties();
//        props.setProperty("bootstrap.servers", bootStrapServer);
//        props.setProperty("group.id", "test_bll_group");
//        props.put("enable.auto.commit", "true");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        return props;
//    }
//
//}