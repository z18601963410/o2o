package com.ike.o2o.web.frontendadmin;

import com.ike.o2o.dto.ProductExecution;
import com.ike.o2o.service.ProductService;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    /**
     * var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
     * product.promotionPrice
     * product.imgAddr
     * product.lastEditTime
     * product.productName
     * product.productDesc
     * product.normalPrice
     */
    @RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取商品ID
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        if (productId > -1) {
            ProductExecution productExecution = productService.queryProductById(productId);
            if(productExecution!=null){
                modelMap.put("success", true);
                modelMap.put("product", productExecution.getProduct());
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询失败!");
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productId is error");
        }
        return modelMap;
    }
}
