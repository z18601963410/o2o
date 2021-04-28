package com.ike.o2o.web.shopadmin;

import com.ike.o2o.dto.ProductCategoryExecution;
import com.ike.o2o.dto.Result;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.enums.ProductCategoryStateEnum;
import com.ike.o2o.exception.ProductCategoryOperationException;
import com.ike.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopAdmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/removeProductCategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //判断接收的前端参数
        if (productCategoryId != null && productCategoryId > 0) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            try {
                //获取shop对象
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution = productCategoryService.removeProductCategory(productCategory);
                //判断DTO对象的状态是否成功
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (ProductCategoryOperationException pe) {
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }

    /**
     * 添加商铺商品分类信息
     *
     * @param productCategories
     * @return
     */
    @RequestMapping(value = "/addProductCategories", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProductCategories(@RequestBody List<ProductCategory> productCategories
            , HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取currentShop对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //设置对象的shopId
        for (ProductCategory productCategory : productCategories) {
            productCategory.setShopId(currentShop.getShopId());
        }
        //productCategories的空值判断
        if (productCategories.size() > 0) {
            //执行批量
            ProductCategoryExecution productCategoryExecution =
                    productCategoryService.batchAddProductCategory(productCategories, currentShop.getShopId());
            //对处理结果进行判断
            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", productCategoryExecution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少属于一个商品类别");
        }
        return modelMap;
    }


    /**
     * 获取商铺商品分类信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProductCategoryList", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        //Result<List<ProductCategory>>
        Map<String, Object> modelMap = new HashMap<>();
        //商铺对象 (Shop)
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //商品类别
        List<ProductCategory> productCategoryList = null;
        if (currentShop != null && currentShop.getShopId() > 0) {
            productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, productCategoryList);
        } else {
            ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, productCategoryStateEnum.getState(), productCategoryStateEnum.getStateInfo());
        }
    }
}
