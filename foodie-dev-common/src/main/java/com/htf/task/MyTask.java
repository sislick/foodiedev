package com.htf.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class MyTask {
    //每秒钟执行一次
    //@Scheduled(cron = "*/1 * * * * ?")
    @Scheduled(fixedRate = 2000)
    public void print(){
        System.out.println(new Date());
    }
}
