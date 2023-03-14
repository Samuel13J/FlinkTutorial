package com.atguigu.chapter02;

import akka.japi.tuple.Tuple4;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author wangjie
 * @create 2023-03-14 18:07
 * flink消费kafka数据后写入mysql
 */
public class KafkaToMysql implements Serializable {

    //定义内部类，和需要写入的表结构一致
    static class user {

        final String name;
        final String gender;
        final String phoneNumber;
        final Integer age;

        public user(String name, Integer age, String gender, String phoneNumber) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phoneNumber = phoneNumber;
        }
    }

    public static void main(String[] args) throws Exception {

        //kafka相关配置
        String topic = "test";
        Properties kafkaConf = new Properties();
        kafkaConf.put(ConsumerConfig.GROUP_ID_CONFIG,"kafkaTest1");
        kafkaConf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        kafkaConf.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        kafkaConf.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        kafkaConf.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3000);
        kafkaConf.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
        kafkaConf.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        kafkaConf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //获取流执行环境
        StreamExecutionEnvironment envs = StreamExecutionEnvironment.getExecutionEnvironment();

        //添加kafka source
        DataStreamSource<String> mykafka = envs.addSource(new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema()
                , kafkaConf));
        //打印数据
        mykafka.print();
         //*new user("李华123", 12, "男", "1881881888")*//*
        //kafka数据转换成	Tuple4<String, Integer, String, String>类型
//        mykafka.map((MapFunction<String, Tuple4<String, Integer, String, String>>) value);

/*今天在使用Flink 时泛型采用的时Tuple，在后面进行算子操作时，采用了lamada表达式发现，代码运行时报以下错误
The generic type parameters of ‘Tuple4’ are missing. In many cases lambda methods don’t provide enough information for automatic type extraction when Java generics are involved. An easy workaround is to use an (anonymous) class instead that implements the ‘org.apache.flink.api.common.functions.MapFunction’ interface. Otherwise the type has to be specified explicitly using type information.
其翻译过来缺少“Tuple4”的泛型类型参数。在许多情况下，当涉及Java泛型时，lambda方法不能为自动类型提取提供足够的信息。一个简单的解决方法是使用（匿名）类来实现网址：apache.flink.api.common.functions函数.MapFunction的接口。否则，必须使用类型信息显式指定类型。*/

        //内部类转换数据格式
        //如果直接通过流返回Tuple4则会报错，当涉及Java泛型时，lambda方法不能为自动类型提取提供足够的信息

        DataStream<Tuple4<String, Integer, String, String>> stream = mykafka.map(new MapFunction<String, Tuple4<String,
                Integer, String, String>>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Tuple4<String, Integer, String, String> map(String value) throws Exception {
                String[] strings = value.split(",");
                return new Tuple4<String, Integer, String, String>(strings[0],Integer.parseInt(strings[1]),strings[2],
                        strings[3]);
            }
        });
        stream.addSink(new MysqlImpl());
        envs.execute();
    }
}