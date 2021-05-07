package com.ike.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ike.o2o.dto.UserAccessToken;
import com.ike.o2o.dto.WechatAuthExecution;
import com.ike.o2o.dto.WechatUser;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.WechatAuth;
import com.ike.o2o.enums.WechatAuthStateEnum;
import com.ike.o2o.service.PersonInfoService;
import com.ike.o2o.service.WechatAuthService;
import com.ike.o2o.until.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd68d9331f1f044b4&redirect_uri=http://8.136.235.141/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * 连接地址含义说明:
 * https://open.weixin.qq.com/connect/oauth2/authorize?				#微信提供的API
 * appid=wxd68d9331f1f044b4											#微信测试号唯一标识
 * &redirect_uri=http://8.136.235.141/o2o/wechatlogin/logincheck	#授权后重定向的回调链接地址
 * &role_type=1														#传递的自定义数据
 * &response_type=code												#返回类型，请填写code。回调数据将封装到code中,解析code获得数据
 * &scope=snsapi_userinfo											#应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
 * &state=1															#原样返回该参数,可自定义. 此处获取该参数判断是店家管理还是首页访问
 * #wechat_redirect													#微信调用回调地址
 *
 * @author xiangze
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONTEND = "1";
    //private static final String SHOPEND = "2";
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck", method = {RequestMethod.GET})
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");

        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用,
        // 获取该参数判断客户类型
        String roleType = request.getParameter("state");

        log.debug("weixin login code:" + code);

        WechatUser user = null;
        String openId = null;
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;//包含了tokenID
            try {
                // 通过code获取access_token                     ->连接微信服务器
                token = WechatUtil.getUserAccessToken(code);//获取token信息
                log.debug("weixin login token:" + token.toString());

                // 通过token获取accessToken(唯一性令牌)
                String accessToken = token.getAccessToken();
                // 通过token获取openId
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息   ->连接微信服务器
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());

                //将当前用户的openId存放到session中,作为用户标识
                request.getSession().setAttribute("openId", openId);

                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
                e.printStackTrace();
            }
        }
        // 若微信帐号为空则需要注册微信帐号，同时注册用户信息
        if (auth == null) {
            //将微信对象转换为personInfo对象
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);//PersonInfo注册必须包含name
            auth = new WechatAuth();
            auth.setOpenId(openId);
            if (FRONTEND.equals(roleType)) {
                //普通用户
                personInfo.setUserType(1);
            } else {
                //商家
                personInfo.setUserType(2);
            }
            auth.setPersonInfo(personInfo);
            WechatAuthExecution we = wechatAuthService.register(auth);
            if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                return null;
            } else {
                personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
                //将personInfo对象插入到session中
                request.getSession().setAttribute("user", auth.getPersonInfo());
            }
        } else {
            //将personInfo对象插入到session中
            request.getSession().setAttribute("user", auth.getPersonInfo());
        }
        // 若用户点击的是前端展示系统按钮则进入前端展示系统
        if (FRONTEND.equals(roleType)) {
            //1 到首页
            return "frontend/index";
        } else {
            //2 后台管理页面
            return "shop/shoplist";
        }
    }
}
