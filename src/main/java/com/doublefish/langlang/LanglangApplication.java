package com.doublefish.langlang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAsync
@MapperScan("com.doublefish.langlang.mapper")
public class LanglangApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LanglangApplication.class, args);
    }

}
