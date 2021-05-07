package com.ike.o2o.web.frontendadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.BitMatrix;
import com.ike.o2o.dto.*;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.entity.UserProductMap;
import com.ike.o2o.entity.WechatAuth;
import com.ike.o2o.enums.UserProductMapStateEnum;
import com.ike.o2o.service.*;
import com.ike.o2o.service.impl.WechatAuthServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ike.o2o.until.CodeUtil.generateQRCodeStream;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private WechatAuthService wechatAuthService;

    //api前缀
    private static String urlPrefix;
    //api中间
    private static String urlMiddle;
    //api后缀
    private static String urlSuffix;
    //授权回调url
    private static String userProductUrl;


    /**
     * 拼接二维码需要的参数
     *
     * @param urlPrefix
     */
    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.product.url}")
    public void setAuthUrl(String userProductUrl) {
        ProductDetailController.userProductUrl = userProductUrl;
    }

    /**
     * 展示商品信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //获取商品ID
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        if (productId > -1) {
            ProductExecution productExecution = productService.queryProductById(productId);
            if (productExecution != null) {
                modelMap.put("success", true);
                modelMap.put("product", productExecution.getProduct());
                if (user != null) {
                    modelMap.put("needQRCode", true);
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询失败!");
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productId is error");
        }
        return modelMap;
    }

    /**
     * 生成商品二维码供商家扫描,添加购买记录
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        //获取商品ID
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        //获取登录的顾客信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //二维码时间戳
        long timestamp = System.currentTimeMillis();

        //非空判断
        if (productId > -1 && user != null && user.getUserId() != null) {
            //将userAwardId和时间戳存放content中
            String content = "{"
                    + "\"aaacustomerIdaaa\":" + user.getUserId()
                    + ",\"aaacreateTimeaaa\":" + timestamp
                    + ",\"aaaproductIdaaa\":" + productId
                    + "}";
            try {
                //拼接url  前缀+回调URL+中间部分+content(用于识别)+后缀
                String longUrl = urlPrefix + userProductUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                //生成二维码流
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(longUrl, response);
                //将流写入到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加商品购买记录
     *
     * @param request HttpServletRequest
     * @return 转发页面
     */
    @RequestMapping(value = "/addUserProductMapRecord", method = RequestMethod.GET)
    private String addUserProductMapRecord(HttpServletRequest request) throws IOException {
        //获取微信员工信息
        WechatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            //获取操作员信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //解析state信息
            String qrCodeinfo = (URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            //二维码对象
            WechatInfo wechatInfo = null;
            ObjectMapper mapper = new ObjectMapper();

            //state转换为 wechatInfo(二维码对象)
            try {
                //顾客ID和商品ID
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", ""), WechatInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //获取shopId
            Long shopId = productService.queryProductById(wechatInfo.getProductId()).getProduct().getShop().getShopId();
            //查询操作授权信息
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.queryShopAuthMapList(shopId, 1, 999);
            //检查微信角色是否有权限扫描二维码
            if (!checkPermission(shopAuthMapExecution.getShopAuthMapList(), user)) {
                return "shop/operationfail";
            }
            //二维码过去检验
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }

            //添加顾客商品映射对象
            UserProductMap userProductMap = new UserProductMap();
            //商品积分
            userProductMap.setPoint(productService.queryProductById(wechatInfo.getProductId()).getProduct().getPoint());
            //顾客对象
            userProductMap.setUser(personInfoService.getPersonInfoById(wechatInfo.getCustomerId()));
            //商品对象
            userProductMap.setProduct(productService.queryProductById(wechatInfo.getProductId()).getProduct());
            //店铺对象
            userProductMap.setShop(productService.queryProductById(wechatInfo.getProductId()).getProduct().getShop());
            //操作员对象
            userProductMap.setOperator(user);

            //添加映射记录
            UserProductMapExecution userProductMapExecution = userProductMapService.addUserProductMap(userProductMap);

            if (UserProductMapStateEnum.SUCCESS.getState() == userProductMapExecution.getState()) {
                return "shop/operationsuccess";
            } else {
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";
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

    public WechatAuth getEmployeeInfo(HttpServletRequest request) {
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
}
