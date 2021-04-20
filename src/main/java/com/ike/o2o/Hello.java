package com.ike.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController //等效于@RequestMapping + @ResponseBody
public class Hello {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello SpringBoot!";
    }
}
