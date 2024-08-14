package com.ebay.test.managersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ManagerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerSystemApplication.class, args);
    }

}
