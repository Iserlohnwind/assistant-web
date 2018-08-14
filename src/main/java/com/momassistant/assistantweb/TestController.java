package com.momassistant.assistantweb;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestController
public class TestController {

    @RequestMapping("test")
    public String test() {
        return "1";
    }
}
