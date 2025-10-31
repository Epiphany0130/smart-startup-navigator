package com.gyqstd.smartstartupnavigator.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GuYuqi
 * @version 1.0
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @GetMapping("/test")
    public String test() {
        return "AI Controller is working!";
    }
}
