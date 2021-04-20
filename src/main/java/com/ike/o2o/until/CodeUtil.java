package com.ike.o2o.until;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        //获取session对象中的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //获取前端返回的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");

        //验证码比对(空值判断和结果对比)
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
