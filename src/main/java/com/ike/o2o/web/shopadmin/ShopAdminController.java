package com.ike.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopAdmin", method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping("/shopOperation")
    public String shopOperation() {
        return "shop/shopOperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        // 转发至店铺列表页面
        return "shop/shoplist";
    }

    @RequestMapping("/productcategorymanagement")
    public String productCategoryManagement() {
        //转发到商铺商品分类列表
        return "shop/productcategorymanagement";
    }

    @RequestMapping("/shopManagement")
    public String shopManagement() {
        //转发到店铺管理页面
        return "shop/shopmanagement";
    }

    @RequestMapping("/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

    @RequestMapping("/productmanagement")
    public String productManagement() {
        return "shop/productmanagement";
    }
}