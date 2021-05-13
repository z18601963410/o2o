package com.ike.o2o.web.superadmin;

import com.google.zxing.common.BitMatrix;
import com.ike.o2o.entity.Area;
import com.ike.o2o.service.AreaService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.MatrixToImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取首页和管理页面二维码(微信)
 */
@RequestMapping(value = "/superAdmin", method = RequestMethod.GET)
@Controller
public class ManagementHandler {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(ManagementHandler.class);

    @RequestMapping(value = "/getAuthQRCodeFrontend")
    private void getAuthQRCodeFrontend(HttpServletResponse response) throws IOException {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd68d9331f1f044b4&redirect_uri=http://8.136.235.141/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        //二维码对象
        BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(url, response);

        //写入到前端
        MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
    }

    @RequestMapping(value = "/getAuthQRCodeBack")
    private void getAuthQRCodeBack(HttpServletResponse response) throws IOException {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd68d9331f1f044b4&redirect_uri=http://8.136.235.141/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=2#wechat_redirect";

        //二维码对象
        BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(url, response);

        //写入到前端
        MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
    }


    private AreaService areaService;

    @Autowired
    private ManagementHandler(AreaService areaService) {
        this.areaService = areaService;
    }

    @RequestMapping(value = "/listArea", method = RequestMethod.GET)//方法的请求路由和请求方式
    @ResponseBody //json形式返回
    private Map<String, Object> listArea() {
        //日志
        logger.info("===start===");
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list ;
        try {
            list = areaService.getAreaList();
            modelMap.put("row", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.getStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMg", e.getMessage());
        }
        //日志
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - startTime);
        logger.info("===end===");
        return modelMap;
    }

}
