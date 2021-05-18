package com.ike.o2o.interceptor.superadmin;

import com.ike.o2o.entity.PersonInfo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 超管系统登录拦截器
 */
public class SuperAdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //从session中提取客户信息
        Object userObj = request.getSession().getAttribute("user");

        //判断是否为空->非空则继续处理,如果为空则返回到登录页面
        if (userObj != null) {
            //若不为空则强制转换为personInfo对象
            PersonInfo personInfo = (PersonInfo) userObj;
            //判断是否为root账户
            if ("root".equals(personInfo.getName())) {
                return true;
            } else {
                PrintWriter out = response.getWriter();
                out.println("<html><script>alert('You do not have privilege!');window.open ('" + request.getContextPath() + "/superadmin/login','_self')</script></html>");
                return false;
            }
        }
        // 若不满足登录验证，则直接跳转到帐号登录页面-->往页面写下列数据
        PrintWriter out = response.getWriter();
        out.println("<html><script>alert('You do not have privilege!');window.open ('" + request.getContextPath() + "/superadmin/login','_self')</script></html>");
        return false;
    }
}
