package com.ike.o2o.web.superadmin;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.dto.*;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.HeadLineStateEnum;
import com.ike.o2o.enums.PersonInfoStateEnum;
import com.ike.o2o.enums.ShopCategoryStateEnum;
import com.ike.o2o.enums.ShopStateEnum;
import com.ike.o2o.exception.ShopOperationException;
import com.ike.o2o.service.*;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/superadmin")
@Controller
public class MainManagementController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(MainManagementController.class);
    private HeadLineService headLineService;
    private ShopCategoryService shopCategoryService;
    private AreaService areaService;
    private PersonInfoService personInfoService;
    private ShopService shopService;

    @Autowired
    private MainManagementController(HeadLineService headLineService, ShopCategoryService shopCategoryService,
                                     AreaService areaService, PersonInfoService personInfoService
            , ShopService shopService) {
        this.headLineService = headLineService;
        this.shopCategoryService = shopCategoryService;
        this.areaService = areaService;
        this.personInfoService = personInfoService;
        this.shopService = shopService;
    }


    @RequestMapping(value = "/listArea", method = RequestMethod.GET)//方法的请求路由和请求方式
    @ResponseBody //json形式返回
    private Map<String, Object> listArea() {
        //日志
        logger.info("===start===");
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list ;
        try {
            list = areaService.getAreaList();
            modelMap.put("row", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.getStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMg", e.getMessage());
        }
        //日志
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - startTime);
        logger.info("===end===");
        return modelMap;
    }


    /**
     * 头条列表
     *
     * @return Map<String, Object>
     */
    @RequestMapping("/listheadlines")
    @ResponseBody
    private Map<String, Object> listheadlines(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();
        int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
        try {
            HeadLine headLine = new HeadLine();
            if (enableStatus > -1) {
                headLine.setEnableStatus(enableStatus);
            }
            HeadLineExecution headLineExecution = headLineService.queryHeadLineList(headLine);
            modelMap.put("rows", headLineExecution.getHeadLineList());
            modelMap.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回数据 loadData
        return modelMap;
    }

    /**
     * 修改头条对象
     *
     * @param request HttpServletRequest
     * @return 处理状态
     * @throws Exception 对象转换异常
     */
    @RequestMapping(value = "/modifyheadline", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyheadline(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        //获取头条对象String
        String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
        //JSON>>Java
        ObjectMapper objectMapper = new ObjectMapper();
        if (headLineStr != null) {
            HeadLine headLine = objectMapper.readValue(headLineStr, HeadLine.class);
            ImageHolder headLineImg = ImageUtil.getImageHolder(request, "headTitleManagementEdit_lineImg");
            HeadLineExecution headLineExecution = headLineService.modifyHeadLine(headLine, headLineImg);
            //将处理状态返回
            modelMap.put("success", HeadLineStateEnum.SUCCESS.getState() == headLineExecution.getState());
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "param is empty ! ");
            return modelMap;
        }
    }

    /**
     * 新增头条对象
     *
     * @param request HttpServletRequest
     * @return 处理状态
     * @throws Exception 对象转换异常
     */
    @RequestMapping(value = "/addheadline", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addheadline(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();

        //获取头条对象String
        String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
        //JSON>>Java
        ObjectMapper objectMapper = new ObjectMapper();
        if (headLineStr != null) {
            HeadLine headLine = objectMapper.readValue(headLineStr, HeadLine.class);
            ImageHolder headLineImg = ImageUtil.getImageHolder(request, "headTitleManagementAdd_lineImg");
            HeadLineExecution headLineExecution = headLineService.addHeadLine(headLine, headLineImg);
            //将处理状态返回
            modelMap.put("success", HeadLineStateEnum.SUCCESS.getState() == headLineExecution.getState());
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "param is empty ! ");
            return modelMap;
        }
    }

    /**
     * 删除头条对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping("/removeheadline")
    private Map<String, Object> deleteLine(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long headLineId = HttpServletRequestUtil.getLong(request, "headLineId");
        if (headLineId > -1) {
            HeadLineExecution headLineExecution = headLineService.removeHeadLine(headLineId);
            modelMap.put("success", HeadLineStateEnum.SUCCESS.getState() == headLineExecution.getState());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 删除头条对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/removeheadlinelist", method = RequestMethod.POST)
    private Map<String, Object> deleteLineList(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        String headLineIdListStr = HttpServletRequestUtil.getString(request, "headLineIdListStr");
        ObjectMapper objectMapper = new ObjectMapper();
        //参数类型
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Long.class);
        HeadLineExecution headLineExecution;
        //尝试转换为list
        if (headLineIdListStr != null) {
            List<Long> headLineIdList = objectMapper.readValue(headLineIdListStr, javaType);
            //执行删除
            for (Long headLineId : headLineIdList
            ) {
                headLineExecution = headLineService.removeHeadLine(headLineId);
                if (HeadLineStateEnum.SUCCESS.getState() != headLineExecution.getState()) {
                    modelMap.put("success", false);
                    return modelMap;
                }
            }
        } else {
            modelMap.put("success", false);
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }


    /**
     * 获取所有的商店类型信息
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/listshopcategorys", method = RequestMethod.POST)
    private Map<String, Object> listShopCategorys(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        ShopCategoryExecution shopCategoryExecution = shopCategoryService.getShopCategoryList(null);
        if (ShopCategoryStateEnum.SUCCESS.getState() == shopCategoryExecution.getState()) {
            modelMap.put("success", true);
            modelMap.put("rows", shopCategoryExecution.getShopCategoryList());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 获取商铺分类一级类型
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/list1stlevelshopcategorys", method = RequestMethod.POST)
    private Map<String, Object> listLstLevelShopCategory(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        ShopCategoryExecution shopCategoryExecution = shopCategoryService.getShopCategoryList(new ShopCategory());
        if (ShopCategoryStateEnum.SUCCESS.getState() == shopCategoryExecution.getState()) {
            modelMap.put("success", true);
            modelMap.put("rows", shopCategoryExecution.getShopCategoryList());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 修改商铺分类对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/modifyshopcategory", method = RequestMethod.POST)
    private Map<String, Object> modifyshopcategory(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        //商铺分类str对象
        String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");
        //图片流对象
        ImageHolder img = ImageUtil.getImageHolder(request, "shopCategoryManagementEdit_shopCategoryImg");
        //对象转换对象
        ObjectMapper objectMapper = new ObjectMapper();
        if (shopCategoryStr != null) {
            ShopCategory shopCategory = objectMapper.readValue(shopCategoryStr, ShopCategory.class);
            //更新对象信息
            ShopCategoryExecution shopCategoryExecution = shopCategoryService.modifyShopCategory(shopCategory, img);
            modelMap.put("success", ShopCategoryStateEnum.SUCCESS.getState() == shopCategoryExecution.getState());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 新增商品分类对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/addshopcategory", method = RequestMethod.POST)
    private Map<String, Object> addshopcategory(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        //商铺分类str对象
        String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");
        //图片流对象
        ImageHolder img = ImageUtil.getImageHolder(request, "shopCategoryManagementAdd_shopCategoryImg");
        //对象转换
        ObjectMapper objectMapper = new ObjectMapper();
        if (shopCategoryStr != null && img != null) {
            ShopCategory shopCategory = objectMapper.readValue(shopCategoryStr, ShopCategory.class);
            //更新对象
            ShopCategoryExecution shopCategoryExecution = shopCategoryService.addShopCategory(shopCategory, img);
            modelMap.put("success", ShopCategoryStateEnum.SUCCESS.getState() == shopCategoryExecution.getState());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }


    /**
     * 区域列表
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/listAreaManagementInfo", method = RequestMethod.GET)
    private Map<String, Object> listAreaManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        List<Area> areaList = areaService.getAreaList();
        modelMap.put("rows", areaList);
        modelMap.put("success", true);
        return modelMap;
    }

    /**
     * 修改取悦列表对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/modifyarea", method = RequestMethod.POST)
    private Map<String, Object> modifyarea(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        String areaStr = HttpServletRequestUtil.getString(request, "areaStr");
        ObjectMapper objectMapper = new ObjectMapper();
        if (areaStr != null) {
            Area area = objectMapper.readValue(areaStr, Area.class);
            modelMap.put("success", areaService.editArea(area));
        }
        return modelMap;
    }

    /**
     * 新增区域对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/addarea", method = RequestMethod.POST)
    private Map<String, Object> addarea(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        String areaStr = HttpServletRequestUtil.getString(request, "areaStr");
        ObjectMapper objectMapper = new ObjectMapper();
        if (areaStr != null) {
            Area area = objectMapper.readValue(areaStr, Area.class);
            modelMap.put("success", areaService.addArea(area));
        }
        return modelMap;
    }

    /**
     * 获取personinfo列表
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/listpersonInfos", method = RequestMethod.POST)
    private Map<String, Object> listpersonInfos(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取请求参数
        int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
        int page = HttpServletRequestUtil.getInt(request, "page");
        int rows = HttpServletRequestUtil.getInt(request, "rows");
        String name = HttpServletRequestUtil.getString(request, "name");

        if (page > -1 && rows > -1) {
            PersonInfo user = new PersonInfo();
            if (enableStatus > -1) {
                user.setEnableStatus(enableStatus);
            }
            //名称模糊查询
            if (name != null) {
                user.setName(name);
            }
            //请求社区
            PersonInfoExecution personInfoExecution = personInfoService.getPersonInfoList(user, page, rows);
            //判断返回状态
            if (PersonInfoStateEnum.SUCCESS.getState() == personInfoExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("rows", personInfoExecution.getPersonInfoList());
            } else {
                modelMap.put("success", false);
            }
        }
        return modelMap;
    }

    /**
     * 修改personinfo状态
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
    private Map<String, Object> modifypersonInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long userId = HttpServletRequestUtil.getLong(request, "userId");
        int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");

        if (userId > -1 && enableStatus > -1) {
            PersonInfo user = new PersonInfo();
            user.setUserId(userId);
            user.setEnableStatus(enableStatus);
            PersonInfoExecution personInfoExecution = personInfoService.modifyPersonInfo(user);
            modelMap.put("success", PersonInfoStateEnum.SUCCESS.getState() == personInfoExecution.getState());
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }


    /**
     * 查询商铺列表
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/listshops", method = RequestMethod.POST)
    private Map<String, Object> listshops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String shopName = HttpServletRequestUtil.getString(request, "shopName");
        int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
        long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
        int page = HttpServletRequestUtil.getInt(request, "page");
        int rows = HttpServletRequestUtil.getInt(request, "rows");

        if (page > -1 && rows > -1) {
            Shop shop = new Shop();
            //查询条件组合
            if (shopName != null) {
                shop.setShopName(shopName);
            }
            if (enableStatus > -1) {
                shop.setEnableStatus(enableStatus);
            }
            if (shopCategoryId > -1) {
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setShopCategoryId(shopCategoryId);
                shop.setShopCategory(shopCategory);
            }
            //查询
            ShopExecution shopExecution = shopService.getShopList(shop, page, rows);
            if (ShopCategoryStateEnum.SUCCESS.getState() == shopExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("rows", shopExecution.getShopList());
            } else {
                modelMap.put("success", false);
            }
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 查询指定ID商铺
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/searchshopbyid", method = RequestMethod.POST)
    private Map<String, Object> searchshopbyid(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            Shop shop = shopService.getByShopId(shopId);
            List<Shop> shopList = new ArrayList<>();
            shopList.add(shop);
            modelMap.put("success", true);
            modelMap.put("rows", shopList);
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    /**
     * 获取商铺分类
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/list2ndlevelshopcategorys", method = RequestMethod.POST)
    private Map<String, Object> list2ndlevelshopcategorys(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取商铺分类
        ShopCategoryExecution shopCategoryExecution = shopCategoryService.getShopCategoryList(new ShopCategory());
        modelMap.put("success", ShopCategoryStateEnum.SUCCESS.getState() == shopCategoryExecution.getState());
        if (shopCategoryExecution.getShopCategoryList().size() > 0) {
            modelMap.put("rows", shopCategoryExecution.getShopCategoryList());
        }
        return modelMap;
    }

    /**
     * 修改商铺对象
     *
     * @param request HttpServletRequest
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    private Map<String, Object> modifyshop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        if (shopStr != null) {
            try {
                Shop shopCondition = objectMapper.readValue(shopStr, Shop.class);
                //更新对象
                ShopExecution shopExecution = shopService.modifyShop(shopCondition, null, null);
                modelMap.put("success", ShopStateEnum.SUCCESS.getState() == shopExecution.getState());
            } catch (Exception e) {
                throw new ShopOperationException("异常啦!对象生成失败!");
            }
        }
        return modelMap;
    }

}
