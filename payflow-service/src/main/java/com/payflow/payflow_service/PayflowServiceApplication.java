package com.payflow.payflow_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PayflowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayflowServiceApplication.class, args);
    }
}
