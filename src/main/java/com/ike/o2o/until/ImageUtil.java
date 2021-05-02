package com.ike.o2o.until;

import com.ike.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    //private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Random r = new Random();
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转换成File类
     *
     * @param cFile CommonsMultipartFile
     * @return File
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        String originalFileName = cFile.getOriginalFilename();
        if (originalFileName != null) {
            File newFile = null;
            try {
                newFile = new File(originalFileName);
                cFile.transferTo(newFile);
            } catch (Exception e) {
                logger.error(e.toString());
                e.printStackTrace();
            }
            return newFile;
        }
        return null;
    }

    /**
     * 处理缩略图，并返回新生成图片的相对值路径
     *
     * @param thumbnailInputStream 输入流
     * @param targetAddr           存储路径PathUtil获取
     * @return 文件相对路径地址
     */
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr) {
        // 获取不重复的随机名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名如png,jpg等
        String extension = getFileExtension(fileName);
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        // 获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        //System.out.println("文件全路径:" + PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        //logger.debug("basePath is :" + basePath);
        // 调用Thumbnails生成带有水印的图片
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200)      // /Users/baidu/work/image   D:/workspace/image  System.getProperty("file.separator")
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(PathUtil.getImgBasePath() + System.getProperty("file.separator").trim() + "watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        // 返回图片相对路径地址
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg, 那么 home work xiangze
     * 这三个文件夹都得自动创建
     *
     * @param targetAddr 文件相对路径
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    private static String getFileExtension(String fileName) {
        //return fileName.substring(fileName.lastIndexOf("."));
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return 文件的随机名
     */
    public static String getRandomFileName() {
        // 获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    /**
     * storePath时文件的路径或者目录的路径,如果是文件路径则删除文件
     * 如果是目录路径则删除目录下的所有文件
     *
     * @param storePath 文件路径或文件目录路径
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {//判断路径是否存在
            if (fileOrPath.isDirectory()) {//判断是否为目录
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();//判断不是目录则直接删除文件
        }
    }

    /**
     * 详情图处理，并返回新生成图片的相对值路径
     *
     * @param thumbnail  ImageHolder(文件名称+文件流)
     * @param targetAddr 目标地址>PathUtil获取
     * @return 图片的相对路径地址
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        // 获取不重复的随机名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名如png,jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        // 获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        // 调用Thumbnails生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(PathUtil.getImgBasePath() + System.getProperty("file.separator").trim() + "watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("创建缩图片失败：" + e.toString());
        }
        // 返回图片相对路径地址
        return relativeAddr;
    }

    /**
     * 获取request中图片流对象
     *
     * @param request   request对象
     * @param fieldName 文件流对应的字段名称
     * @return ImageHolder(文件流和文明名称的对象组合)
     */
    public static ImageHolder getImageHolder(HttpServletRequest request, String fieldName) {
        //解析request
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断是否包含文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //类型转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //根据特定字段,提取文件流,将上传流信息封装套 shopImg中
            CommonsMultipartFile imgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile(fieldName);
            if (imgFile != null) {
                try {
                    return new ImageHolder(imgFile.getOriginalFilename(), imgFile.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        return null;
    }
}
