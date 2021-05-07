package com.ike.o2o.web.frontendadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/frontend")
public class FrontendAdminController {
    /**
     * 首页路由
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    private String index() {
        return "frontend/index";
    }

    /**
     * 商品列表页路由
     *
     * @return
     */
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    private String showShopList() {
        return "frontend/shoplist";
    }

    /**
     * 店铺详情页路由
     *
     * @return
     */
    @RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
    private String showShopDetail() {
        return "frontend/shopdetail";
    }

    /**
     * 商品详情页路由
     *
     * @return
     */
    @RequestMapping(value = "/productdetail", method = RequestMethod.GET)
    private String showProductDetail() {
        return "frontend/productdetail";
    }

    /**
     * 转发到当天店铺奖品页面
     *
     * @return 路由地址
     */
    @RequestMapping(value = "/awardlist", method = RequestMethod.GET)
    private String awardlist() {
        return "frontend/awardlist";
    }

    /**
     * 我的积分页
     *
     * @return
     */
    @RequestMapping(value = "/mypoint", method = RequestMethod.GET)
    private String mypoint() {
        return "frontend/mypoint";
    }

    /**
     * 奖品兑换页面
     *
     * @return
     */
    @RequestMapping(value = "/myrecord", method = RequestMethod.GET)
    private String myrecord() {
        return "frontend/myrecord";
    }

    /**
     * 积分兑换页面
     *
     * @return
     */
    @RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
    private String pointrecord() {
        return "frontend/pointrecord";
    }

    /**
     * 兑换记录详情页面
     *
     * @return
     */
    @RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
    private String myawarddetail() {
        return "frontend/myawarddetail";
    }
}
