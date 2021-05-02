package com.ike.o2o.until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * 功能废弃
 * 短连接生成工具: 将长URL转为短URL  该工具类暂时无法使用
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 * <p>
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.45</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>3.9.1</version>
 * </dependency>
 **/
//需要在pom.xml引入以下依赖
/**
 <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 <dependency>
 <groupId>com.alibaba</groupId>
 <artifactId>fastjson</artifactId>
 <version>1.2.45</version>
 </dependency>
 <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 <dependency>
 <groupId>com.squareup.okhttp3</groupId>
 <artifactId>okhttp</artifactId>
 <version>3.9.1</version>
 </dependency>
 **/

/**
 *
 * 使用新浪的短链接服务生成短链接
 *
 */

public class ShortNetAddressUtil {

    static String actionUrl = "http://api.t.sina.com.cn/short_url/shorten.json";

    static String APP_KEY = "2815391962,31641035,3271760578,3925598208";


    @SuppressWarnings("deprecation")
    public static String getShortURL(String longUrl) {
        longUrl = java.net.URLEncoder.encode(longUrl);
        String appkey = APP_KEY;
        String[] sourceArray = appkey.split(",");
        for (String key : sourceArray) {
            String shortUrl = sinaShortUrl(key, longUrl);
            if (shortUrl != null) {
                return shortUrl;
            }
        }
        return null;
    }

    public static String sinaShortUrl(String source, String longUrl) {
        String result = sendPost(actionUrl, "url_long=" + longUrl + "&source=" + source);
        //String result=sendPost(actionUrl,"link="+longUrl+"url="+longUrl);
        if ("".equals(result)) {
            return "";
        }
        JSONArray jsonArr = JSON.parseArray(result);
        JSONObject json = JSON.parseObject(jsonArr.get(0).toString());
        return json.get("url_short").toString();
    }

    /**
     *
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     *
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *
     * @return 所代表远程资源的响应结果
     *
     *
     *
     */

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    public static void main(String[] args) {
        //String longUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd68d9331f1f044b4&redirect_uri=http://8.136.235.141/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        String longUrl = "www.baidu.com";

        System.out.println(getShortURL(longUrl));

    }

}
