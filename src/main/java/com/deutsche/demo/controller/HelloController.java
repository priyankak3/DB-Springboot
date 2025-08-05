package com.deutsche.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
@RequestMapping("hello")
    public String hello(){
    System.out.println("Hello");
    return "Hello World!";
}

}
