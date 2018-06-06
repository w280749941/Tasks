package com.example.task_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TaskUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskUserApplication.class, args);
    }
}
