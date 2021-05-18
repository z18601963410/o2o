package com.ike.o2o;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@RestController //等效于@RequestMapping + @ResponseBody  向前端直接输出内容
@Controller
public class Hello {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello SpringBoot!";
    }

    /**
     * 输入/o2o 默认重定向到首页
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    private String forwardToFrontendIndex() {
        return "redirect:/frontend/index";
    }
}
