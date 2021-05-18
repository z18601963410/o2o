package com.ike.o2o.web.superadmin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 超级管理员控制器路由
 */
@Controller
@RequestMapping(value = "/superadmin", method = {RequestMethod.GET, RequestMethod.POST})
public class SuperAdminController {

    @RequestMapping(value = "/areamanage")
    private String areamanage() {
        return "/superadmin/areamanage";
    }

    @RequestMapping(value = "/headlinemanage")
    private String headlinemanage() {
        return "/superadmin/headlinemanage";
    }

    @RequestMapping(value = "/login")
    private String login() {
        return "/superadmin/login";
    }

    @RequestMapping(value = "/main")
    private String main() {
        return "/superadmin/main";
    }

    @RequestMapping(value = "/personinfomanage")
    private String personinfomanage() {
        return "/superadmin/personinfomanage";
    }

    @RequestMapping(value = "/shopcategorymanage")
    private String shopcategorymanage() {
        return "/superadmin/shopcategorymanage";
    }

    @RequestMapping(value = "/shopmanage")
    private String shopmanage() {
        return "/superadmin/shopmanage";
    }

    @RequestMapping(value = "/top")
    private String top() {
        return "/superadmin/top";
    }

}
