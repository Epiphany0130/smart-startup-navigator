package com.gyqstd.smartstartupnavigator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gyqstd.smartstartupnavigator.mapper")
public class SmartStartupNavigatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStartupNavigatorApplication.class, args);
    }

}
