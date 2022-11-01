package com.example.chaptor05;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Chaptor05Application {

    public static void main(String[] args) {
        SpringApplication.run(Chaptor05Application.class, args);
    }

}
