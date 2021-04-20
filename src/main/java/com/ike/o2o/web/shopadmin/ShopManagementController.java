package com.ike.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.dto.Result;
import com.ike.o2o.dto.ShopExecution;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.ProductCategoryStateEnum;
import com.ike.o2o.enums.ShopStateEnum;
import com.ike.o2o.service.AreaService;
import com.ike.o2o.service.ShopCategoryService;
import com.ike.o2o.service.ShopService;
import com.ike.o2o.service.ProductCategoryService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.Request;

import javax.management.ValueExp;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopAdmin")
public class ShopManagementController {
    Logger logger = LoggerFactory.getLogger(ShopManagementController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 获取shopId
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShopManagementInfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //尝试从request获取shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //判断是否shopId值
        if (shopId <= 0) {
            //尝试从session中获取Shop对象
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            //空值判断
            if (currentShopObj == null) {
                //非法请求,请求重定向
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopAdmin/shoplist");
            } else {
                //将session中的shop对象强转
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                //将shopId进行返回
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            //查询店铺
            Shop currentShop = shopService.getByShopId(shopId);
            //将shop放入到session中,然后将shopId返回
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("shopId", shopId);
            modelMap.put("redirect", false);
        }

        return modelMap;
    }


    /**
     * 获取店铺列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShopList", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 999);
            //将店铺列表存放到session中和user对象返回给前台
            request.getSession().setAttribute("shopList", shopExecution.getShopList());
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
    }

    /**
     * 根据店铺ID获取店铺对象
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShopById", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取店铺ID
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");

        if (shopId > -1) {
            try {
                //根据ID获取shop对象
                Shop shop = shopService.getByShopId(shopId);
                //将当前对象存放到session中
                request.getSession().setAttribute("currentShop",shop);
                //获取区域列表
                List<Area> areaList = areaService.getAreaList();
                //添加到modelMap中
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    /**
     * 修改店铺
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyShop", method = RequestMethod.POST)//请求
    @ResponseBody//响应
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //验证码判断
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        //接收请求尝试json>pogo,包括店铺信息合图片
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //处理上传图片  CommonsMultipartFile无法直接转为File对象
        CommonsMultipartFile shopImg = null;
        //上传文件解析器,解析request,参数: 从本次回话的上下文对象中获取上传内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        //判断request中是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //类型转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //根据特定字段,提取文件流,将上传流信息封装套 shopImg中
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //修改店铺
        if (shop != null) {
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(user);
            ShopExecution shopExecution;
            try {
                //执行店铺注册
                if (shopImg != null) {
                    shopExecution = shopService.modifyShop(shop, shopImg.getInputStream(),
                            shopImg.getOriginalFilename());
                } else {
                    //图片流为空
                    shopExecution = shopService.modifyShop(shop, null,
                            null);
                }
                //检查处理结果是否是 SUCCESS
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    //返回注册失败的状态信息
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMeg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺ID");
        }
        return modelMap;
    }

    /**
     * 获取初始化信息,包括店铺类别和区域类别
     *
     * @return
     */
    @RequestMapping(value = "/getShopInitInfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory()).getShopCategoryList();
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 注册店铺
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/registerShop", method = RequestMethod.POST)//请求
    @ResponseBody//响应
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //验证码判断
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        //接收请求尝试json>pogo,包括店铺信息合图片
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //处理上传图片  CommonsMultipartFile无法直接转为File对象
        CommonsMultipartFile shopImg = null;
        //上传文件解析器,解析request,参数: 从本次回话的上下文对象中获取上传内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request中是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //类型转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //根据特定字段,提取文件流,将上传流信息封装套 shopImg中
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            //无文件流则报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //注册店铺  分为三步:  ①获取登录用户的信息,通过session获取 ②执行店铺注册功能
        // ③获取当前用户可操作的shopList(用户登录时就获取可操作店铺并保存在session中)并将新注册商铺进行插入
        if (shop != null && shopImg != null) {
            //通过session获取登录用户的信息
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);

            try {
                //执行店铺注册,注册成功后返回用户可管理店铺页面
                ShopExecution shopExecution = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());

                //检查执行结果状态是否符合预期
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    //该用户可以操作的店铺列表
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");

                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    //将当前新添加店铺ArrayList中
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    //返回注册失败的状态信息
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMeg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
        }
        return modelMap;
    }
}