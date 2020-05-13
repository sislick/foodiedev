package com.htf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * web启动类
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.htf","org.n3r.idworker"})
@EnableRedisHttpSession  // 开启使用redis作为spring session
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
