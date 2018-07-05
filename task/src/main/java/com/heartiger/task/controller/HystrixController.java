package com.heartiger.task.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    //@HystrixCommand(fallbackMethod = "fallback")
//    @HystrixCommand(commandProperties = {
//        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//    }) // this set timeout time, default is 1s.

//    @HystrixCommand(commandProperties = {
//        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
//        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
//        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
//        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
//    })
    @HystrixCommand
    @GetMapping("/getSomeThing")
    public String getSomeThing() throws Exception {
        throw new Exception("Happend");
    }

    private String fallback(){
        return "Welcome to fallback";
    }

    private String defaultFallback(){
        return "Welcome to default fallback";
    }
}
