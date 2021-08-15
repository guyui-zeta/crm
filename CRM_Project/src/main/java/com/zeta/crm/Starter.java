package com.zeta.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/3 9:16
 */
@SpringBootApplication
@MapperScan("com.zeta.crm.dao")
public class Starter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }

    /**
     *  设置web项目的气动入口
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Starter.class);
    }
}
