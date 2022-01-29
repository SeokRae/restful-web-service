package com.example.restful.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelloWorldBean {

    @Getter
    private final String message;
}
