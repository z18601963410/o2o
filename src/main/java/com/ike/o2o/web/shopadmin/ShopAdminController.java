package com.ike.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopAdmin", method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping("/shopauthedit")
    public String modifyShopAuthMap() {
        return "shop/shopauthedit";
    }

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


    //新增

    @RequestMapping("/awardoperation")
    public String awardoperation() {
        return "shop/awardoperation";
    }

    /**
     * 奖品管理
     *
     * @return html地址
     */
    @RequestMapping("/awardmanagement")
    public String awardmanagement() {
        return "shop/awardmanagement";
    }

    /**
     * 消费记录
     *
     * @return html地址
     */
    @RequestMapping("/productbuycheck")
    public String productbuycheck() {
        return "shop/productbuycheck";
    }

    /**
     * 积分兑换
     *
     * @return html页面
     */
    @RequestMapping("/awarddelivercheck")
    public String awarddelivercheck() {
        return "shop/awarddelivercheck";
    }

    /**
     * 顾客积分
     *
     * @return html页面
     */
    @RequestMapping("/usershopcheck")
    public String usershopcheck() {
        return "shop/usershopcheck";
    }

    /**
     * 授权管理
     *
     * @return html页面
     */
    @RequestMapping("/shopauthmanagement")
    public String shopauthmanagement() {
        return "shop/shopauthmanagement";
    }

    /**
     * 奖品详情页面
     *
     * @return
     */
    @RequestMapping("/awarddetail")
    public String awarddetail() {
        return "shop/awarddetail";
    }

}