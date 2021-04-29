package com.ike.o2o.web.handler;

import com.ike.o2o.exception.ProductOperationException;
import com.ike.o2o.exception.ShopOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * 全局异常处理类
 * 扫描所有的的controller,触发异常后均会交由本controller进行进一步处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //日志对象
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)//拦截异常类型,Exception即为所有异常
    @ResponseBody
    public Map<String, Object> handle(Exception e) {
        Map<String, Object> modelMap = new HashMap<>();

        //异常类型匹配>>对特定异常类型进行特殊处理
        if (e instanceof ShopOperationException) {
            modelMap.put("errMsg", e.getMessage());
        } else if (e instanceof ProductOperationException) {
            modelMap.put("errMsg", e.getMessage());
        } else {
            //其他异常类型处理 记录日志
            logger.error("系统异常:" + e.getMessage());
            modelMap.put("errMsg", "请联系管理员处理!");
        }
        //返回给前端
        modelMap.put("success", false);
        return modelMap;
    }
}
