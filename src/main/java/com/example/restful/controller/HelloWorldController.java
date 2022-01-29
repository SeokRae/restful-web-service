package com.example.restful.controller;

import com.example.restful.controller.dto.HelloWorldBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    // curl -i -X GET http://127.0.0.1:8888/helloWorld
    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "HelloWorld";
    }

    // curl -i -X GET http://127.0.0.1:8888/helloWorldBean
    // Java Bean 형태로 반환하는 컨트롤러
    @GetMapping("/helloWorldBean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }
}
