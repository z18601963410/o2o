package com.ike.o2o.web.shopadmin;

import com.ike.o2o.dto.UserShopMapExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.enums.UserShopMapStateEnum;
import com.ike.o2o.service.UserShopMapService;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopAdmin")
public class UserShopMapManagementController {
    private Logger logger = LoggerFactory.getLogger(UserShopMapManagementController.class);
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 获取店铺下用户积分列表信息,支持模糊查询
     *
     * @param request request
     * @return Map<String, Object>
     */
    @RequestMapping(value = "/listusershopmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserShopMapByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当天店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取分页参数和模糊查询参数
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        String userName = HttpServletRequestUtil.getString(request, "userName");

        //非空判断
        if (currentShop != null && currentShop.getShopId() != null && pageIndex > -1 && pageSize > -1) {
            //参数封装
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setShop(currentShop);
            //模糊查询参数
            if (userName != null) {
                PersonInfo user = new PersonInfo();
                user.setName(userName);
                userShopMapCondition.setUser(user);
            }
            //查询
            UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapListByShop(userShopMapCondition, pageIndex, pageSize);
            if (UserShopMapStateEnum.SUCCESS.getState() == userShopMapExecution.getState()) {
                //数据封装
                modelMap.put("success", true);
                modelMap.put("userShopMapList", userShopMapExecution.getUserShopMapList());
                return modelMap;
            } else {
                logger.error("数据查询不符合预期:" + userShopMapExecution.getState());
                modelMap.put("success", false);
                modelMap.put("errMsg", "请求失败!");
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "param is empty!");
            return modelMap;
        }
    }

}
