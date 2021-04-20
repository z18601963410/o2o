package com.ike.o2o.web.frontendadmin;

import com.ike.o2o.dto.ProductExecution;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.service.ProductCategoryService;
import com.ike.o2o.service.ProductService;
import com.ike.o2o.service.ShopService;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.PageCalculator;
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
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    /**
     * 获取店铺详细信息和该店铺下所属商品分类列表
     *
     * @param request
     * @return 店铺详情和商品列表
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //1.获取商铺ID
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            //2.获取商铺详情
            Shop shop = shopService.getByShopId(shopId);
            //3.获取商铺下所属商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);

            if (shop != null && productCategoryList != null) {
                modelMap.put("shop", shop);
                modelMap.put("productCategoryList", productCategoryList);
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "shop or productCategoryList is null");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "shopId is null");
        }
        return modelMap;
    }

    /**
     * 查询店铺下符合条件的商品信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //1.获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //2.获取每页行数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //3.获取商铺ID
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //4.空值判断
        if (pageIndex > -1 && pageSize > -1 && shopId > -1) {
            //5.尝试获取商品分类ID
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            //6.尝试模糊名查询
            String productName = HttpServletRequestUtil.getString(request, "productName");
            //7.组合条件
            Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
            //8.依据条件查询
            ProductExecution productExecution = productService.queryProductList(productCondition, PageCalculator.calculateRowIndex(pageIndex,pageSize), pageSize);

            modelMap.put("productList", productExecution.getProductList());
            modelMap.put("count", productExecution.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    /**
     * 组合条件到对象中
     *
     * @param shopId            商铺ID
     * @param productCategoryId 商品分类ID
     * @param productName       商品名称
     * @return 商品对象
     */
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Shop shop = new Shop();
        shop.setShopId(shopId);
        Product productCondition = new Product();
        productCondition.setShop(shop);
        //如果有商品分类ID将商品分类信息整合到Product中
        if (productCategoryId != -1) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //如果有模糊名则整合条件
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
