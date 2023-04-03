package com.atguigu.mysql;

/**
 * @author wangjie
 * @create 2023-03-29 18:25
 */

import akka.japi.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlImpl extends RichSinkFunction<Tuple4<String, Integer, String, String>> {
    private static final PropertiesUtils instance = PropertiesUtils.getInstance();
    private Connection connection;
    private PreparedStatement preparedStatement;
    String username = instance.getmysqlUserName();
    String password = instance.getmysqlPassword();
    String drivername = instance.getmysqlDriverName();
    String dburl = instance.getmysqldbURL();

    @Override
    public void invoke(Tuple4<String, Integer, String, String> value) throws Exception {
        //SinkFunction.super.invoke(value);
        Class.forName(drivername);
        connection = DriverManager.getConnection(dburl, username, password);
        String sql = "insert into brtl_pd_rtl (name ,age,sex,tel) values(?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(2, value.t1());
        preparedStatement.setInt(1, value.t2());
        preparedStatement.setString(3, value.t3());
        preparedStatement.setString(3, value.t4());
        preparedStatement.executeUpdate();
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }


    @Override
    public void invoke(Tuple4<String, Integer, String, String> value, Context context) throws Exception {
        //SinkFunction.super.invoke(value, context);
        Class.forName(drivername);
        connection = DriverManager.getConnection(dburl, username, password);
        String sql = "insert into brtl_pd_rtl (name ,age,sex,tel) values(?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, value.t1());
        preparedStatement.setInt(2, value.t2());
        preparedStatement.setString(3, value.t3());
        preparedStatement.setString(4, value.t4());
        preparedStatement.execute();
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
