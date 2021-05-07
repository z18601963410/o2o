package com.ike.o2o.web.superadmin;

import com.google.zxing.common.BitMatrix;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.MatrixToImageWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(value = "/superAdmin", method = RequestMethod.GET)
@Controller
public class ManagementHandler {

    //获取首页和管理页面二维码(微信)
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

}
