package com.ike.o2o.web.frontendadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.BitMatrix;
import com.ike.o2o.dto.*;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.UserAwardMapStateEnum;
import com.ike.o2o.enums.UserProductMapStateEnum;
import com.ike.o2o.enums.UserShopMapStateEnum;
import com.ike.o2o.service.*;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import com.ike.o2o.until.MatrixToImageWriter;
import com.ike.o2o.until.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class UserDetailController {
    private Logger logger = LoggerFactory.getLogger(UserDetailController.class);
    @Autowired
    private UserShopMapService userShopMapService;
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    /**
     * 获取当前登录顾客的所有积分(包含所有店铺)
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listusershopmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前登录客户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        //获取分页信息
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");

        //获取店铺模糊查询名
        String shopName = HttpServletRequestUtil.getString(request, "shopName");

        if (user != null && user.getUserId() != null && pageSize > -1 && pageIndex > -1) {
            UserShopMap userShopMap = new UserShopMap();
            userShopMap.setUser(user);
            if (shopName != null) {
                Shop shop = new Shop();
                shop.setShopName(shopName);
                userShopMap.setShop(shop);
            }
            //获取所有积分对象
            UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapListByShop(userShopMap, pageIndex, pageSize);
            //处理状态判断
            if (UserShopMapStateEnum.SUCCESS.getState() == userShopMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("count", userShopMapExecution.getCount());
                modelMap.put("userShopMapList", userShopMapExecution.getUserShopMapList());
                return modelMap;
            }
            modelMap.put("success", false);
            modelMap.put("errMsg", "查询错误,错误代码:" + userShopMapExecution.getState());
            return modelMap;
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty !");
        return modelMap;
    }

    /**
     * 查询顾客在各个店铺的消费记录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listuserproductmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前登录客户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        //获取分页信息
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");

        //获取店铺模糊查询名
        String productName = HttpServletRequestUtil.getString(request, "productName");

        if (user != null && user.getUserId() != null && pageSize > -1 && pageIndex > -1) {
            UserProductMap userProductMap = new UserProductMap();
            userProductMap.setUser(user);
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);
                userProductMap.setProduct(product);
            }
            //查询数据
            UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductMap(userProductMap, pageIndex, pageSize);

            if (UserProductMapStateEnum.SUCCESS.getState() == userProductMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("count", userProductMapExecution.getCount());
                modelMap.put("userProductMapList", userProductMapExecution.getUserProductMapList());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询错误,错误代码:" + userProductMapExecution.getState());
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty !");
        return modelMap;
    }

    /**
     * 积分兑换记录页面数据准备
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listuserawardmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserAwardMapsbByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //奖品名称模糊查询
        String awardName = HttpServletRequestUtil.getString(request, "awardName");
        //获取当前登录用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        //空值判断
        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {
            //查询条件封装
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setUser(user);
            if (awardName != null) {
                Award awardCondition = new Award();
                awardCondition.setAwardName(awardName);
                userAwardMap.setAward(awardCondition);
            }
            //查询
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.queryUserAwardMapByShop(userAwardMap, pageIndex, pageSize);
            if (UserAwardMapStateEnum.SUCCESS.getState() == userAwardMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("count", userAwardMapExecution.getCount());
                modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapList());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询错误,错误代码:" + userAwardMapExecution.getState());
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty !");
        return modelMap;
    }

    /**
     * 获取指定ID的礼品兑换信息
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardByUseraAwardId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        //获取User
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        //空值判断
        if (user != null && user.getUserId() != null && userAwardId > -1) {
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.queryUserAwardMapById(userAwardId);
            if (UserAwardMapStateEnum.SUCCESS.getState() == userAwardMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("award", userAwardMapExecution.getUserAwardMap().getAward());
                modelMap.put("usedStatus", userAwardMapExecution.getUserAwardMap().getUsedStatus());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询失败,错误代码:" + userAwardMapExecution.getState());
                return modelMap;
            }
        }
        modelMap.put("success", false);
        modelMap.put("errMsg", "param is empty !");
        return modelMap;
    }

    //api前缀
    private static String urlPrefix;
    //api中间
    private static String urlMiddle;
    //api后缀
    private static String urlSuffix;
    //授权回调url
    private static String useAweraUrl;


    /**
     * 拼接二维码需要的参数
     *
     * @param urlPrefix
     */
    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        UserDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        UserDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        UserDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.user_award.url}")
    public void setAuthUrl(String authUrl) {
        UserDetailController.useAweraUrl = authUrl;
    }


    /**
     * 将连接转为二维码 以流的形式返回个页面(页面显示为一个二维码)
     * 生成连接的二维码
     *
     * @param request  request
     * @param response response
     * @return
     */
    @RequestMapping(value = "/generateqrcode4award")
    @ResponseBody
    private void generateqrcode4award(HttpServletRequest request, HttpServletResponse response) {
        //获取当前userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        if (userAwardId > -1) {
            //当前时间戳
            long timestamp = System.currentTimeMillis();
            //将userAwardId和时间戳存放content中
            String content = "{\"aaauserAwardIdaaa\":" + userAwardId + ",\"aaacreateTimeaaa\":" + timestamp + "}";

            //尝试生成二维码并通过response写到前端
            try {
                //拼接url
                String longUrl = urlPrefix + useAweraUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                //将URL生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);
                //将流写出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/changeUserAwardUsedStatus", method = RequestMethod.GET)
    private String changeUserAwardUsedStatus(HttpServletRequest request) throws IOException {
        //从request中获取微信用户信息
        WechatAuth auth = getEmployeeInfo(request);
        //判断是否为空
        if (auth != null) {
            //获取进行扫码的操作员信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());

            //获取微信返回的state信息>>解码
            String qrCodeinfo = (URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));

            //二维码对象
            WechatInfo wechatInfo = null;
            ObjectMapper mapper = new ObjectMapper();

            //state转换为 wechatInfo(二维码对象)
            try {
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", ""), WechatInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //判断当前user是否有权限进行扫码操作  获取UserAwardMap对象
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.queryUserAwardMapById(wechatInfo.getUserAwardId());
            //获取ShopId
            Long shopId = userAwardMapExecution.getUserAwardMap().getShop().getShopId();
            //查询店铺下授权信息列表
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.queryShopAuthMapList(shopId, 1, 999);
            //检查微信角色是否有权限扫描二维码
            if (!checkPermission(shopAuthMapExecution.getShopAuthMapList(), user)) {
                return "shop/operationfail";
            }

            //二维码过去检验
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }

            //变更状态信息
            UserAwardMap userAwardMap = new UserAwardMap();
            //修改后状态值
            userAwardMap.setUsedStatus(1);
            //从二维码对象中获取UserAwardId
            userAwardMap.setUserAwardId(wechatInfo.getUserAwardId());
            //操作员ID
            userAwardMap.setOperator(user);
            try {
                //尝试修改状态
                userAwardMapExecution = userAwardMapService.updateUserAwardMap(userAwardMap);

                if (UserAwardMapStateEnum.SUCCESS.getState() == userAwardMapExecution.getState()) {
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

    /**
     * 二维码过期验证
     *
     * @param wechatInfo 二维码对象
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        //空值判断
        if (wechatInfo != null && wechatInfo.getCreateTime() != null) {
            //获取当前时间-创建时间 <=10分钟
            return (System.currentTimeMillis() - wechatInfo.getCreateTime()) <= 600000;
        }
        return false;
    }

    /**
     * 检查扫描二维码权限
     *
     * @param shopAuthMapList 授权信息列表
     * @return boolean
     */
    private boolean checkPermission(List<ShopAuthMap> shopAuthMapList, PersonInfo user) {
        //非空判断
        if (shopAuthMapList != null && user != null && user.getUserId() != null) {
            for (ShopAuthMap shopAuthMap : shopAuthMapList) {
                if (shopAuthMap.getEmployee().getUserId().equals(user.getUserId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
