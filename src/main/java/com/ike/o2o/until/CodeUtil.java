package com.ike.o2o.until;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        //获取session对象中的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //获取前端返回的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        /*
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }else{
        return true
        }
        */
        //验证码比对(空值判断和结果对比)
        return !(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected));
    }

    /**
     * 生成二维码
     * @param content 需要编码内容的对象
     * @param response 响应信息
     * @return 二维码对象
     */
    public static BitMatrix generateQRCodeStream(String content, HttpServletResponse response) {
        //添加响应头信息
        response.setHeader("Cache-Control", "no-store"); //对缓存的控制
        response.setHeader("Pragma", "no-cache");// 在请求/响应链上附近的一些参数--拒绝缓存
        response.setDateHeader("Expires", 0);//告诉客户端该响应数据会在指定的时间过期，通常用于给客户端缓存作为参考。
        response.setContentType("image/png");//响应内容类型

        //设置文字编码和内边框距
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);

        //比特矩阵对象>>二维码
        BitMatrix bitMatrix;

        try {
            //参数顺序: 编码内容,编码类型,生成图片宽度,图片高度,设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }
}
