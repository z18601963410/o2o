package com.ike.o2o.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superAdmin", method = RequestMethod.GET)
public class SuperAdminController {

    @RequestMapping("/help")
    public String help() {
        return "local/help";
    }
}
