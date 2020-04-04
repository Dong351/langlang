package com.doublefish.langlang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.doublefish.langlang.mapper")
public class LanglangApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LanglangApplication.class, args);
    }

}
