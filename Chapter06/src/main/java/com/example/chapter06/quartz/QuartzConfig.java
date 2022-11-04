package com.example.chapter06.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail quartzJobDetail(){
        return JobBuilder.newJob(QuartzScheduledJob.class).storeDurably().build();
    }

//    @Bean
    public Trigger jobTrigger(){
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).withRepeatCount(4);

        return TriggerBuilder.newTrigger().forJob(quartzJobDetail()).withSchedule(simpleScheduleBuilder).build();
    }
}
