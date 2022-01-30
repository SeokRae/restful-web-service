package com.example.restful.controller;

import com.example.restful.controller.dto.HelloWorldBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

    private final MessageSource messageSource;


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

    // curl -i -X GET http://localhost:8888/hello-world-internationalized \
    //   -H 'Accept-Language: kr'
    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("greeting.message", null, locale);
    }
}
