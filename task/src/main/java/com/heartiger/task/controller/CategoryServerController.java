package com.heartiger.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class can received request from other server can return it.
 */
@RestController
public class CategoryServerController {

    @GetMapping("/msg")
    public String msg() {
        return "this is a test message";
    }
}
