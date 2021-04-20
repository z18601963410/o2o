package com.ike.o2o.web.frontendadmin;

import com.ike.o2o.dto.ShopExecution;
import com.ike.o2o.entity.Area;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopCategory;
import com.ike.o2o.service.AreaService;
import com.ike.o2o.service.ShopCategoryService;
import com.ike.o2o.service.ShopService;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 依据前端传入条件查询符合条件的商铺列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取每页数据行数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        //非空判断
        if (pageIndex > -1 && pageSize > -1) {
            //尝试获取一级类别
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //尝试获取特定二级类别
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //尝试获取区域ID
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            //尝试获取模糊名称
            String shopName = HttpServletRequestUtil.getString(request, "shopName");

            //条件组合后查询
            Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);

            //数据返回给前端
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("count", shopExecution.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    //获取分类列表和区域分类列表
    @ResponseBody
    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //1.获取店铺分类列表
        //尝试获取一级类别
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        //二级商品分类列表
        List<ShopCategory> shopCategoryList = null;

        //如果能获取一级分类
        if (parentId != -1) {
            try {
                //获取一级分类下二级分类列表
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);

                ShopCategory child = new ShopCategory();
                child.setParent(parent);

                shopCategoryList = shopCategoryService.getShopCategoryList(child).getShopCategoryList();
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            try {
                //如果没有一级分类信息则查询所有店铺:首页点击的全部商店
                shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory()).getShopCategoryList();
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);
        //2.获取区域列表信息
        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    /**
     * 组合前端查询店铺的条件
     *
     * @param parentId       一级类别ID
     * @param shopCategoryId 二级类别ID
     * @param areaId         区域ID
     * @param shopName       店铺名称
     * @return shop对象
     */
    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        //查询某个一级类别下的所有店铺
        if (parentId != -1) {
            //如果有一级类别
            ShopCategory parent = new ShopCategory();
            ShopCategory child = new ShopCategory();
            parent.setShopCategoryId(parentId);
            child.setParent(parent);
            //商铺所处的类别
            shopCondition.setShopCategory(child);
        }
        //查询某个二级类别下的所有店铺
        if (shopCategoryId != -1) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        //查询某个区域下的所有店铺
        if (areaId != -1) {
            Area area = new Area();
            area.setAreaID(areaId);
            shopCondition.setArea(area);
        }
        //根据名称模糊查找店铺
        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        //前端展示店铺都是已经审核的店铺
        shopCondition.setEnableStatus(1);

        return shopCondition;
    }
}
