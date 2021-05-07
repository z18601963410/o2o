package com.ike.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.BitMatrix;
import com.ike.o2o.dto.ShopAuthMapExecution;
import com.ike.o2o.dto.UserAccessToken;
import com.ike.o2o.dto.WechatInfo;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.entity.WechatAuth;
import com.ike.o2o.enums.ShopAuthMapStateEnum;
import com.ike.o2o.service.PersonInfoService;
import com.ike.o2o.service.ShopAuthMapService;
import com.ike.o2o.service.WechatAuthService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.MatrixToImageWriter;
import com.ike.o2o.until.wechat.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopAdmin")
public class ShopAuthManagementController {
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;

    /**
     * 微信将回调到该地址,包含code
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addshopauthmap", method = RequestMethod.GET)
    private String addShopAuthMap(HttpServletRequest request) throws IOException {
        //从request中获取微信用户信息
        WechatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            //获取微信中的user
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将user放到session中
            request.getSession().setAttribute("user", user);
            //获取微信返回的state信息>>解码
            String qrCodeinfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            //微信二维码对象
            WechatInfo wechatInfo = null;
            ObjectMapper mapper = new ObjectMapper();

            //state转换为 wechatInfo(二维码对象)
            try {
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", ""), WechatInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //二维码过去检验
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }

            //去重校验
            //已经授权过的人员不能重复授权
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.queryShopAuthMapList(wechatInfo.getShopId(), 0, 99);
            for (ShopAuthMap temp : shopAuthMapExecution.getShopAuthMapList()) {
                if (user.getUserId().equals(temp.getEmployee().getUserId())) {
                    //转发到错误页面
                    return "shop/operationfail";
                }
            }

            //根据获取的内容添加授权信息
            ShopAuthMap shopAuthMap = new ShopAuthMap();
            Shop shop = new Shop();
            shop.setShopId(wechatInfo.getShopId());

            shopAuthMap.setShop(shop);
            shopAuthMap.setEmployee(user);
            shopAuthMap.setTitle("员工");
            shopAuthMap.setTitleFlag(1);
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            try {
                //注册权限
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);

                if (ShopAuthMapStateEnum.SUCCESS.getState() == se.getState()) {
                    return "shop/operationsuccess";
                } else {
                    return "shop/operationfail";
                }
            } catch (Exception e) {
                e.printStackTrace();
                //异常跳转到失败页面
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";
    }

    /**
     * 二维码过期验证
     *
     * @param wechatInfo 二维码对象
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        //空值判断
        if (wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getShopId() != null) {
            //获取当前时间-创建时间 <=10分钟
            return (System.currentTimeMillis() - wechatInfo.getCreateTime()) <= 600000;
        }
        return false;
    }

    /**
     * 获取微信对象
     *
     * @param request
     * @return
     */
    private WechatAuth getEmployeeInfo(HttpServletRequest request) {
        //获取code
        String code = request.getParameter("code");
        WechatAuth wechatAuth = null;
        if (null != code) {
            //token对象
            UserAccessToken token;
            try {
                //获取token
                token = WechatUtil.getUserAccessToken(code);
                //获取openId
                String openId = token.getOpenId();
                //openID放到session中
                request.getSession().setAttribute("openId", openId);
                //获取微信对象
                wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wechatAuth;
    }

    //api前缀
    private static String urlPrefix;
    //api中间
    private static String urlMiddle;
    //api后缀
    private static String urlSuffix;
    //授权回调url
    private static String authUrl;
    //state
    private static int state;


    /**
     * 拼接二维码需要的参数
     *
     * @param urlPrefix
     */
    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    @Value("${wechat.state}")
    public void setState(int state) {
        ShopAuthManagementController.state = state;
    }

    /**
     * 将连接转为二维码 以流的形式返回个页面(页面显示为一个二维码)
     * 生成连接的二维码
     *
     * @param request  request
     * @param response response
     * @return
     */
    @RequestMapping(value = "/generateqrcode4shopauth")
    @ResponseBody
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
        //获取shop
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //非空判断
        if (currentShop != null && currentShop.getShopId() != null) {
            //获取时间戳
            long timestamp = System.currentTimeMillis();
            //将店铺ID和时间戳timestamp放到content中>>二维码过期时间
            String content = "{\"aaashopIdaaa\":" + currentShop.getShopId() + ",\"aaacreateTimeaaa\":" + timestamp + "}";
            try {
                //将content信息进行编码:完整URL= wechat.prefix+回调url+wechat.middle+自定义参数state值+wechat.suffix
                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                //生成短连接>>暂未实现 TODO
                //生成二维码: 连接地址+响应对象
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);
                //以流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理(删除)授权对象
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShopAuthMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码过滤
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");

        if (verifyCodeActual != null && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误,请重新输入验证码!");
            return modelMap;
        }
        //获取授权对象 id title name  >>shopAuth
        String shopAuthStr = HttpServletRequestUtil.getString(request, "shopAuthMapStr");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ShopAuthMap shopAuthMap = objectMapper.readValue(shopAuthStr, ShopAuthMap.class);
            //非空判断
            if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
                //判断是否为店家本人>>是则不允许操作
                if (checkPermission(shopAuthMap.getShopAuthId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无法对店家本身权限做操作(已是店铺的最高权限)");
                    return modelMap;
                }
                //修改
                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                //判断是否成功
                if (ShopAuthMapStateEnum.SUCCESS.getState() == shopAuthMapExecution.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "修改失败!");
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "shopAuthMap or getShopAuthId() is null");
                return modelMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", "对象绑定异常");
            return modelMap;
        }
    }

    /**
     * 获取当前店铺授权列表
     *
     * @param request request
     * @return Map<String, Object>
     */
    @RequestMapping(value = "/getshopauthmaplistbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopAuthMapListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取分页和当前店铺信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        //非空判断(分页和店铺对象及店铺ID不允许为空)
        //异常由service捕获
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            //查询list
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.queryShopAuthMapList(currentShop.getShopId(), pageIndex, pageSize);
            //根据service返回状态响应前端
            if (ShopAuthMapStateEnum.SUCCESS.getState() == shopAuthMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("count", shopAuthMapExecution.getCount());
                modelMap.put("shopAuthMapList", shopAuthMapExecution.getShopAuthMapList());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopAuthMapExecution.getStateInfo());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "查询条件错误!");
            return modelMap;
        }
    }


    /**
     * 获取指定ID shopAuth
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopAuthMapById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取授权对象ID
        long shopAuthMapId = HttpServletRequestUtil.getLong(request, "shopAuthId");
        if (shopAuthMapId > -1) {
            ShopAuthMap shopAuthMap = shopAuthMapService.queryShopAuthMapById(shopAuthMapId);
            if (shopAuthMap != null) {
                modelMap.put("success", true);
                modelMap.put("shopAuthMap", shopAuthMap);
                return modelMap;
            }
            modelMap.put("success", false);
            modelMap.put("errMsg", "没有找到该对象");
            return modelMap;
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "查询条件错误");
        return modelMap;
    }

    /**
     * 查验操作者是否为店铺拥有者
     *
     * @param shopAuthId 授权者ID
     * @return 匹配结果
     */
    private boolean checkPermission(Long shopAuthId) {
        return shopAuthMapService.queryShopAuthMapById(shopAuthId).getTitleFlag() == 0;
    }
}
