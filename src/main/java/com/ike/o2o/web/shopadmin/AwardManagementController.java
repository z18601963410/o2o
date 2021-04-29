package com.ike.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.dto.AwardExecution;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.entity.Award;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.enums.AwardStateEnum;
import com.ike.o2o.exception.AwardOperationException;
import com.ike.o2o.service.AwardService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.IIOException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopAdmin")
public class AwardManagementController {
    private Logger logger = LoggerFactory.getLogger(AwardManagementController.class);
    @Autowired
    private AwardService awardService;

    /**
     * 获取奖品列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前店铺对象和参数
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        String awardName = HttpServletRequestUtil.getString(request, "awardName");

        //非空判断
        if (currentShop != null && currentShop.getShopId() != null && pageIndex > -1 && pageSize > -1) {
            Award awardCondition = new Award();
            awardCondition.setShopId(currentShop.getShopId());
            if (awardName != null) {
                awardCondition.setAwardName(awardName);
            }
            AwardExecution awardExecution = awardService.queryAwardListByShop(awardCondition, pageIndex, pageSize);
            if (AwardStateEnum.SUCCESS.getState() == awardExecution.getState()) {
                modelMap.put("awardList", awardExecution.getAwardList());
                modelMap.put("count", awardExecution.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询错误!");
            }
        }
        return modelMap;
    }

    /**
     * 根据ID查询奖品对象>>用于编辑
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        if (currentShop != null && currentShop.getShopId() != null && awardId > -1) {
            Award award = awardService.queryAwardByAwardId(awardId);
            //判断奖品的shopID是否合法
            if (!currentShop.getShopId().equals(award.getShopId())) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "非法查询!");
                return modelMap;
            }
            modelMap.put("success", true);
            modelMap.put("award", award);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "查询失败!");
        }
        return modelMap;
    }

    /**
     * 添加奖品对象
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addaward", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码判断
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误!");
            return modelMap;
        }
        //获取当前店铺对象和奖品对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //奖品对象
        String strAward = HttpServletRequestUtil.getString(request, "awardStr");
        //奖品对象转换
        try {
            Award award = new ObjectMapper().readValue(strAward, Award.class);
            //获取ImageHolder
            ImageHolder awardImg = ImageUtil.getImageHolder(request, "thumbnail");
            //非空判断
            if (award != null && awardImg != null && currentShop != null && currentShop.getShopId() != null) {
                //添加店铺信息
                award.setShopId(currentShop.getShopId());
                //添加award
                if (AwardStateEnum.SUCCESS.getState() == awardService.addAward(award, awardImg).getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.put("success", false);
            return modelMap;
        }
        modelMap.put("success", false);
        return modelMap;
    }

    @RequestMapping(value = "/modifyaward", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        //验证码判断
        if (verifyCodeActual != null && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误!");
            return modelMap;
        }
        //获取当前店铺对象和奖品对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //奖品对象
        String strAward = HttpServletRequestUtil.getString(request, "awardStr");
        //奖品对象转换
        try {
            Award award = new ObjectMapper().readValue(strAward, Award.class);
            //获取ImageHolder
            ImageHolder awardImg = ImageUtil.getImageHolder(request, "thumbnail");
            //非空判断
            if (award != null && currentShop != null && currentShop.getShopId() != null) {
                //添加店铺信息
                award.setShopId(currentShop.getShopId());
                AwardExecution awardExecution;

                if (awardImg != null) {
                    awardExecution = awardService.editAward(award, awardImg);
                } else {
                    awardExecution = awardService.editAward(award, null);
                }
                //添加award
                if (AwardStateEnum.SUCCESS.getState() == awardExecution.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.put("success", false);
            return modelMap;
        }
        modelMap.put("success", false);
        return modelMap;
    }
}
