package com.toy4.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HelloController {

    private final PasswordEncoder passwordEncoder;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public String hello() {
        return "Hello, FC-MINI-4 attendance service!";
    }

    @GetMapping("/{password}")
    public String encodePassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }
}
