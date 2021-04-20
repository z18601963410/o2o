package com.ike.o2o.web.frontendadmin;

import com.ike.o2o.dto.HeadLineExecution;
import com.ike.o2o.entity.HeadLine;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.ShopCategory;
import com.ike.o2o.service.HeadLineService;
import com.ike.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 初始化首页信息, 包含: 头条列表,商铺分类(一级分类和二级分类)
     *
     * @return
     */
    @RequestMapping(value = "listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        //1.商铺分类列表
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        //尝试获取商铺分类列表
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory()).getShopCategoryList();
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //2.头条列表
        List<HeadLine> headLineList = new ArrayList<HeadLine>();
        //尝试获取头条列表
        try {
            //查询可用的头条 状态为1
            HeadLine headLine = new HeadLine();
            headLine.setEnableStatus(1);
            HeadLineExecution headLineExecution = headLineService.queryHeadLineList(headLine);
            modelMap.put("headLineList", headLineExecution.getHeadLineList());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }
}
