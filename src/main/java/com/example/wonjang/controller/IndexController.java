package com.example.wonjang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/lecture")
    public String lectureIndex(){
        return "lecture/index";
    }
    @GetMapping("/inquiry")
    public String inquiryIndex(){
        return "inquiry/index";
    }
    @GetMapping("/announce")
    public String announceIndex(){
        return "announce/index";
    }
    @GetMapping("/lecture/{id}")
    public String lectureIndex(
            @PathVariable int id
    ){
        System.out.println("id = " + id);
        return "lecture/player";
    }
}
