package com.example.wonjang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/inquiry")
    public String inquiryIndex(){
        return "inquiry/index";
    }
    @GetMapping("/data")
    public String dataIndex(){
        return "data/index";
    }
    @GetMapping("/announce")
    public String announceIndex(){
        return "announce/index";
    }

}
