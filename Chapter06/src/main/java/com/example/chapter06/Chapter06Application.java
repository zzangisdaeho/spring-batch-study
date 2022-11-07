package com.example.chapter06;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
public class Chapter06Application {

    public static void main(String[] args) {
        List<String> realArgs = new ArrayList<>(Arrays.asList(args));

        realArgs.add("transactionFile=classpath:/input/transactionFile.csv");
        realArgs.add("summaryFile=file:/Users/kimdaeho/study/spring-batch-study/Chapter06/src/main/resources/output/summaryFile.csv");
//        SpringApplication.run(Chapter06Application.class, args);
        SpringApplication.run(Chapter06Application.class, realArgs.toArray(String[]::new));
    }

}
