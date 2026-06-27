package com.cprad.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InvoiceSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoiceSchedulerApplication.class, args);
    }

}
