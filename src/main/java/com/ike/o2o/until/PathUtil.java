package com.ike.o2o.until;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    //当前操作系统的路径分隔符
    private static final String separator = System.getProperty("file.separator");

    private static String winPath;
    private static String linuxPath;
    private static String shopPath;
    private static String headLinePath;
    private static String shopCategoryPath;
    private static String awardPath;

    /**
     * 获取根路径
     *
     * @return 根据不同操作系统返回响应的根路径
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath;
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", separator);
        return basePath.trim();
    }

    @Value("${awardPath}")
    public void setAwardPath(String awardPath) {
        PathUtil.awardPath = awardPath;
    }

    @Value("${winPath}")
    public void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }

    @Value("${linuxPath}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }

    @Value("${shopPath}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    @Value("${headLinePath}")
    public void setHeadLinePath(String headLinePath) {
        PathUtil.headLinePath = headLinePath;
    }

    @Value("${shopCategoryPath}")
    public void setShopCategoryPath(String shopCategoryPath) {
        PathUtil.shopCategoryPath = shopCategoryPath;
    }


    /**
     * 获取商铺图片对象的相对路径部分
     *
     * @return 相对路径
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = shopPath + shopId + separator;
        return imagePath.replace("/", separator);
    }

    /**
     * 获取头条对象的相对路径部分
     *
     * @return 相对路径
     */
    public static String getHeadLineImagePath() {
        String imagePath = headLinePath;
        return imagePath.replace("/", separator);
    }

    /**
     * 获取商品分类对象的相对路径部分
     *
     * @return 相对路径
     */
    public static String getShopCategoryPath() {
        String imagePath = shopCategoryPath;
        return imagePath.replace("/", separator);
    }

    /**
     * 获取奖品对象的相对路径部分
     *
     * @return 相对路径
     */
    public static String getAwardPath() {
        String imagePath = awardPath;
        return imagePath.replace("/", separator);
    }

}
