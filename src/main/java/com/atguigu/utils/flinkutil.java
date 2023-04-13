package com.atguigu.utils;

import com.atguigu.mysql.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wangjie
 * @create 2023-04-07 17:07
 */
public class flinkutil {
    private static Logger logger = LoggerFactory.getLogger(flinkutil.class);
    InputStream in = null;
    private static flinkutil instance = null;
    private String localFilePath = null;

    private flinkutil() {
        InputStream in = null;
        try{
            in = flinkutil.class.getClassLoader().getResourceAsStream("args.properties");
            Properties prop = new Properties();
            prop.load(in);

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("");
            }
        }
    }

    public static flinkutil getInstance() {
        if (instance == null) {
            instance = new flinkutil();
        }
        return instance;
    }
}
