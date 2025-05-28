package com.habimed.habimedWebService;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("apiTest")
public class HabimedController {
    
    @GetMapping("/get_test")
    public String getTest() {
        return "holiwis";
    }
    

    @GetMapping("/get_name/{name}/{edad}")
    public String getParameter(@PathVariable String name, @PathVariable int edad) {
        return "hola " + name + " Tu edad es " + edad;
    }
}
