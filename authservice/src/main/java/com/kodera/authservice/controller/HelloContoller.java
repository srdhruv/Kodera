package com.kodera.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloContoller { // dummy for now to test jwt auth
    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello Secure world";
    }
}
