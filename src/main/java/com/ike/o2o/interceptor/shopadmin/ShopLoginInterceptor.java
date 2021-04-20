package com.ike.o2o.interceptor.shopadmin;

import com.ike.o2o.entity.PersonInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录拦截器-->校验用户是否登录
 */
public class ShopLoginInterceptor implements HandlerInterceptor {
    //在用户操作前进行拦截>对shopAdmin下操作时进行验证
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从session中提取客户信息
        Object userObj = request.getSession().getAttribute("user");

        //判断是否为空->非空则继续处理,如果为空则返回到登录页面
        if (userObj != null) {
            //若不为空则强制转换为personInfo对象
            PersonInfo personInfo = (PersonInfo) userObj;

            //空值判断(personID不为空,personID大于0(小于0则不存在),person状态处于可用状态)
            if (personInfo.getUserId() != null && personInfo.getUserId() > 0 && personInfo.getEnableStatus() == 1)
            return true;
        }
        // 若不满足登录验证，则直接跳转到帐号登录页面-->往页面写下列数据
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;

    }
}
