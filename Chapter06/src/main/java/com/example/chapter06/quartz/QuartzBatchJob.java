package com.example.chapter06.quartz;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzBatchJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job quartzBatchJob1(){
        return this.jobBuilderFactory.get("quartzBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(quartzBatchStep1())
                .build();
    }

    @Bean
    public Step quartzBatchStep1() {
        return this.stepBuilderFactory.get("quartzBatchStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("quartzStep 1 run ");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
