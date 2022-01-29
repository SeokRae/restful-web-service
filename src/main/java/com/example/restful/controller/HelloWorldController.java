package com.example.restful.controller;

import com.example.restful.controller.dto.HelloWorldBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    // curl -i -X GET http://127.0.0.1:8888/hello-world-bean
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "HelloWorld";
    }

    // curl -i -X GET http://127.0.0.1:8888/hello-world-bean
    // Java Bean 형태로 반환하는 컨트롤러
    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    // curl -i -X GET http://127.0.0.1:8888/hello-world-bean/path-variable/seok
    // 특정 resource에 접근하기위한 pathvariable 사용 컨트롤러
    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World %s", name));
    }
}
