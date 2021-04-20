package com.ike.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.dto.ProductExecution;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.enums.ProductStateEnum;
import com.ike.o2o.exception.ProductOperationException;
import com.ike.o2o.service.ProductCategoryService;
import com.ike.o2o.service.ProductService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopAdmin")
public class ProductManagementController {
    public static final int IMAGE_Mix_SIZE = 6;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 根据商品ID获取商品属性
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取店铺对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取商品ID
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        //获取商品分类列表
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
        //查询商品数据
        if (productId < 0) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "商品ID无效");
            return modelMap;
        }
        //数据封装和返回
        ProductExecution productExecution = productService.queryProductById(productId);
        //添加商品分类
        modelMap.put("productCategoryList", productCategoryList);
        modelMap.put("success", true);
        modelMap.put("product", productExecution.getProduct());
        return modelMap;
    }

    /**
     * 商品修改
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //验证码跳过标识
        boolean stateChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //验证码判断
        if (!stateChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        //1.获取商铺对象
        ObjectMapper objProduct = new ObjectMapper();
        Product newProduct = new Product();
        ImageHolder thumbnailImageHolder = null;
        List<ImageHolder> productImgList = new ArrayList<>();

        //2.接收前端参数 product / 缩略图Thumbnail / 商品详情图片List<productImg>
        //2.1product对象处理
        String stringProduct = HttpServletRequestUtil.getString(request, "productStr");
        if (stringProduct != null) {
            try {
                //对象转换
                newProduct = objProduct.readValue(stringProduct, Product.class);
                //初始化商品信息
                newProduct.setShop(currentShop);
            } catch (Exception e) {
                throw new ProductOperationException("Product对象转换失败");
            }
        }
        //2.2 判断request是否包含文件流 缩略图和详情图处理
        CommonsMultipartFile tempCommonsMultipartFile = null;
        //解析全请求对象
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver
                (request.getSession().getServletContext());
        //对象转换
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        //判断是否包含文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            multipartHttpServletRequest = (MultipartHttpServletRequest) request;

            //获取指定的文件流 缩略图流
            tempCommonsMultipartFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
            try {
                if (tempCommonsMultipartFile != null) {
                    thumbnailImageHolder = new ImageHolder
                            (tempCommonsMultipartFile.getOriginalFilename(),
                                    tempCommonsMultipartFile.getInputStream());
                }
            } catch (Exception e) {
                throw new ProductOperationException("thumbnailImageHolder转换失败:" + e.getMessage());
            }
            //获取详情图片及处理
            for (int i = 0; i < IMAGE_Mix_SIZE; i++) {
                try {
                    //循环尝试获取详情图片文件流
                    tempCommonsMultipartFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (tempCommonsMultipartFile != null) {
                        productImgList.add(new ImageHolder(
                                tempCommonsMultipartFile.getOriginalFilename(), tempCommonsMultipartFile.getInputStream()
                        ));
                    }
                } catch (Exception e) {
                    throw new ProductOperationException("productImageHolder转换失败:" + e.getMessage());
                }
            }
        }
        //3.调用service方法提交修改
        ProductExecution productExecution = productService.modifyProduct(newProduct, currentShop.getShopId(), thumbnailImageHolder, productImgList);
        //4.返回处理结果 根据service处理结果
        if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
            modelMap.put("success", true);
        } else {
            modelMap.put("false", false);
            modelMap.put("errMsg", productExecution.getStateInfo());
        }
        return modelMap;
    }

    /**
     * 添加商品对象
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        /*
        1.验证码校验
        2.图片处理
        3.商品对象初始化
        4.商品添加
         */
        Map<String, Object> modelMap = new HashMap<>();
        //1.验证码验证
        boolean checkVerifyCodeResult = CodeUtil.checkVerifyCode(request);
        if (!checkVerifyCodeResult) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        //初始化对象
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImageList = new ArrayList<>();


        //2.缩略图和商品详情图片处理
        CommonsMultipartResolver multipartRequest =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //判断是否包含文件流信息
            if (multipartRequest.isMultipart(request)) {
                thumbnail = handlerImage(request, thumbnail, productImageList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //3.商品对象处理
        try {
            String strProduct = HttpServletRequestUtil.getString(request, "productStr");
            //尝试将strProduct转换为Product对象
            product = objectMapper.readValue(strProduct, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //4.商品对象、商品对象缩略图、商品详情图片不为空则执行商品添加动作
        if (product != null && thumbnail != null && productImageList.size() > 0) {
            try {
                //从session中获取商铺对象 赋值给商品对象
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);

                //执行添加
                ProductExecution productExecution = productService.addProduct(product, thumbnail, productImageList);

                //对添加结果进行判断
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                    return modelMap;
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 获取商铺商品列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取shop对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        try {
            //查询shop下的productList
            ProductExecution productExecution = productService.queryProductList(currentShop.getShopId());
            //数据封装
            modelMap.put("success", true);
            modelMap.put("productList", productExecution.getProductList());
            //返回结果
        } catch (ProductOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 图片处理工具方法
     *
     * @param request          请求对象
     * @param thumbnail        商品缩略图
     * @param productImageList 商品详情图片列表
     * @return
     * @throws IOException
     */
    private ImageHolder handlerImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImageList) throws IOException {
        //类型转换
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //获取缩略图文件
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        //非空判断
        if (thumbnailFile != null) {
            //从request 解析出缩略图对象赋值到thumbnail
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        for (int i = 0; i < IMAGE_Mix_SIZE; i++) {
            //解析商品详情图片
            CommonsMultipartFile productImageFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImageFile != null) {
                ImageHolder imageHolderTemp = new ImageHolder(productImageFile.getOriginalFilename(), productImageFile.getInputStream());
                //将商品详情图片添加集合中
                productImageList.add(imageHolderTemp);
            } else {
                break;
            }
        }
        return thumbnail;
    }
}
