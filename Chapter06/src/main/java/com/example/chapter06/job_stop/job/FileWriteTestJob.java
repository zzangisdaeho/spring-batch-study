package com.example.chapter06.job_stop.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;

@Configuration
public class FileWriteTestJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job writeTestJob(){
        return jobBuilderFactory.get("writeTestJob")
                .incrementer(new RunIdIncrementer())
                .start(writeTestStep())
                .build();
    }

    private Step writeTestStep() {
        return stepBuilderFactory.get("writeTestStep")
                .<TestClass, TestClass>chunk(10)
                .reader(ObjetReader())
                .writer(ObjectWriter())
                .build();
    }

    private ItemWriter<TestClass> ObjectWriter() {
        return new FlatFileItemWriterBuilder<TestClass>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("/Users/kimdaeho/study/spring-batch-study/Chapter06/src/main/resources/output/test.scv"))
                .delimited()
                .names(new String[]{"name", "age"})
                .build();

    }

    private ItemReader<TestClass> ObjetReader() {
        return new ListItemReader<>(
                Arrays.asList(new TestClass("one", 1), new TestClass("two", 2), new TestClass("three", 3))
        );
    }

    @Data
    @AllArgsConstructor
    static class TestClass{
        private String name;
        private int age;
    }
}
