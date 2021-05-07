package com.ike.o2o.web.shopadmin;

import com.ike.o2o.dto.UserAwardMapExecution;
import com.ike.o2o.entity.Award;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserAwardMap;
import com.ike.o2o.enums.UserAwardMapStateEnum;
import com.ike.o2o.service.UserAwardMapService;
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
@RequestMapping(value = "/shopAdmin")
public class UserAwardMapMangementController {

    @Autowired
    private UserAwardMapService userAwardMapService;

    @RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取模糊查询name
        String awardName = HttpServletRequestUtil.getString(request, "awardName");
        //获取分页参数
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        if (currentShop != null && currentShop.getShopId() != null && pageIndex > -1 && pageSize > -1) {
            //数据封装
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(currentShop);
            if (awardName != null) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.queryUserAwardMapByShop(userAwardMap, pageIndex, pageSize);

            //判断状态码信息
            if (UserAwardMapStateEnum.SUCCESS.getState() == userAwardMapExecution.getState()) {
                modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapList());
                modelMap.put("success", true);
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询错误,错误状态码:" + userAwardMapExecution.getState());
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty!" );
        return modelMap;
    }
}
