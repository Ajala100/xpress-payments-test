package com.xpresspayment.javabackendexercise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class T {
    @GetMapping
    public String testing(){
        return "done";
    }
}
