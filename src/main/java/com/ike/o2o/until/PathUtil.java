package com.ike.o2o.until;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    private static String seperator = System.getProperty("file.separator");

    private static String winPath;
    private static String linuxPath;
    private static String shopPath;
    private static String headLinePath;
    private static String shopCategoryPath;

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
     * 获取根路径
     *
     * @return 根据不同操作系统返回响应的根路径
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", seperator);
        return basePath.trim();
    }

    public static String getShopImagePath(long shopId) {
        String imagePath = shopPath + shopId + seperator;
        return imagePath.replace("/", seperator);
    }

    public static String getHeadLineImagePath() {
        String imagePath = headLinePath;
        return imagePath.replace("/", seperator);
    }

    public static String getShopCategoryPath() {
        String imagePath = shopCategoryPath;
        return imagePath.replace("/", seperator);
    }

}
