package com.ike.o2o.web.frontendadmin;

import com.ike.o2o.dto.AwardExecution;
import com.ike.o2o.dto.UserAwardMapExecution;
import com.ike.o2o.dto.UserShopMapExecution;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.AwardStateEnum;
import com.ike.o2o.enums.UserAwardMapStateEnum;
import com.ike.o2o.enums.UserShopMapStateEnum;
import com.ike.o2o.service.AwardService;
import com.ike.o2o.service.UserAwardMapService;
import com.ike.o2o.service.UserShopMapService;
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
@RequestMapping(value = "/frontend")
public class AwardListController {

    @Autowired
    private AwardService awardService;

    @Autowired
    private UserAwardMapService userAwardMapService;

    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 获取当前店铺的奖品列表
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listawardsbyshop")
    @ResponseBody
    private Map<String, Object> listAwardByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前店铺ID
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //获取分页信息
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //奖品名称
        String awardName = HttpServletRequestUtil.getString(request, "awardName");
        //获取当前顾客信息>>查询顾客积分
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        if (shopId > -1 && pageIndex > -1 && pageSize > -1) {
            Award award = new Award();
            //条件封装
            award.setShopId(shopId);
            if (awardName != null) {
                award.setAwardName(awardName);
            }
            int totalPoint = -1;
            //查询
            AwardExecution awardExecution = awardService.queryAwardListByShop(award, pageIndex, pageSize);
            //如果顾客已经登录则查询查询该顾客的积分信息
            if (user != null) {
                UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapByUserId(user.getUserId(), shopId);
                if (UserShopMapStateEnum.SUCCESS.getState() == userShopMapExecution.getState()) {
                    totalPoint = userShopMapExecution.getUserShopMap().getPoint();
                }
            }
            //返回结构状态码校验
            if (AwardStateEnum.SUCCESS.getState() == awardExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("awardList", awardExecution.getAwardList());
                modelMap.put("count", awardExecution.getCount());
                if (totalPoint > -1) {
                    modelMap.put("totalPoint", totalPoint);
                }
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询错误,状态码:" + awardExecution.getState());
                return modelMap;
            }

        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty!");
        return modelMap;
    }

    /**
     * 添加奖品兑换记录
     *
     * @param request request
     * @return modelMap
     */
    @ResponseBody
    @RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前店铺
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //获取当前顾客信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //获取当前奖品ID
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");

        //非空判断
        if (shopId > -1 && user != null && user.getUserId() != null && awardId != null) {
            //查询数据封装
            UserAwardMap userAwardMap = new UserAwardMap();
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userAwardMap.setShop(shop);
            userAwardMap.setUser(user);

            Award award = new Award();
            award.setAwardId(awardId);
            userAwardMap.setAward(award);
            userAwardMap.setPoint(awardService.queryAwardByAwardId(awardId).getPoint());

            //执行插入
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.insertUserAwardMap(userAwardMap);
            //对服务层处理状态检查
            if (UserAwardMapStateEnum.SUCCESS.getState() == userAwardMapExecution.getState()) {
                modelMap.put("success", true);
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "新增记录失败,错误状态码:" + userAwardMapExecution.getState());
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty!");
        return modelMap;
    }
}
