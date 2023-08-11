package com.toy4.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public String hello() {
        return "Hello, FC-MINI-4 attendance service!";
    }
}
