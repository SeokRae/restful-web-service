package com.example.restful.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    // curl -i -X GET http://127.0.0.1:8888/helloWorld
    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "HelloWorld";
    }
}
