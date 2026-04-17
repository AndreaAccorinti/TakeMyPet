package com.takemypet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TakeMyPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeMyPetApplication.class, args);
    }
}
