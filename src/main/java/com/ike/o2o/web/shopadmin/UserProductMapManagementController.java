package com.ike.o2o.web.shopadmin;

import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.UserProductMapStateEnum;
import com.ike.o2o.service.ProductSellDailyService;
import com.ike.o2o.service.UserProductMapService;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/shopAdmin")
public class UserProductMapManagementController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;

    /**
     * 获取当前店铺下的商品消费记录
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前店铺信息和分页数据
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        //非空判断
        if (currentShop != null && pageIndex > -1 && pageSize > -1) {
            UserProductMap userProductMap = new UserProductMap();
            userProductMap.setShop(currentShop);
            //商品名模糊查找条件
            if (productName != null) {
                userProductMap.setProduct(new Product(productName));
            }
            UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductMap(userProductMap, pageIndex, pageSize);
            if (UserProductMapStateEnum.SUCCESS.getState() == userProductMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("userProductMapList", userProductMapExecution.getUserProductMapList());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询失败");
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "查询条件错误");
            return modelMap;
        }
    }

    /**
     * 查询最近一周商品销量
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前商铺对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取需要模糊查找的商品名称
        String productName = HttpServletRequestUtil.getString(request, "productName");
        if (currentShop != null && currentShop.getShopId() != null) {
            //条件对象
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            //判断是否有商品ID
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);

                productSellDailyCondition.setProduct(product);
            }
            //一周时间间隔计算
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            calendar.add(Calendar.DATE, -6);
            Date startTime = calendar.getTime();
            //查询符合条件的数据
            List<ProductSellDaily> productSellDailyListOfWeek = productSellDailyService.queryProductSellDaily(productSellDailyCondition, startTime, endTime);



            modelMap.put("success", true);
            modelMap.put("productSellDailyOfWeek", productSellDailyListOfWeek);
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "currentShopId is empty!");
            return modelMap;
        }

    }

}
