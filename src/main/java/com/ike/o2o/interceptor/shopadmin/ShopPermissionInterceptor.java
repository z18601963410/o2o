package com.ike.o2o.interceptor.shopadmin;

import com.ike.o2o.entity.Shop;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 店家管理系统操作权限验证
 */
public class ShopPermissionInterceptor implements HandlerInterceptor {
    /**
     * 验证用户是否有权限对店铺进行操作
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //从session中获取当前用户可操作商铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");

        //判断当前店铺是否在当前用户可操作商铺列表内
        if (currentShop != null && shopList != null) {
            for (Shop temp : shopList) {
                if (currentShop.getShopId().equals(temp.getShopId())) {
                    return true;
                }
            }


        }
        return false;
    }
}
