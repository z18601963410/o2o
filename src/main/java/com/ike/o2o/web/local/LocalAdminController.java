package com.ike.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 页面跳转
 */
@Controller
@RequestMapping(value = "/local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAdminController {
    /**
     * 跳转到绑定本地用户
     *
     * @return 目标html
     */
    @RequestMapping("/accountbind")
    private String accountBind() {
        return "local/accountbind";
    }

    /**
     * 跳转到密码修改页面
     *
     * @return 目标html
     */
    @RequestMapping("/changepsw")
    private String changePsw() {
        return "local/changepsw";
    }

    /**
     * 跳转至登录页
     *
     * @return 目标页
     */
    @RequestMapping("/login")
    private String login() {
        return "local/login";
    }


}
