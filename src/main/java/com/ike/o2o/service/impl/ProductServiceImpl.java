package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ProductDao;
import com.ike.o2o.dao.ProductImgDao;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.dto.ProductExecution;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductImg;
import com.ike.o2o.enums.ProductStateEnum;
import com.ike.o2o.exception.ProductImgOperationException;
import com.ike.o2o.exception.ProductOperationException;
import com.ike.o2o.service.ProductService;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 更新商品信息
     *
     * @param product 商品对象
     * @param shopId  店铺ID
     * @return DTO
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, long shopId, ImageHolder productThumbnailImageHolder,
                                          List<ImageHolder> productImgListImageHolder) throws ProductOperationException {

        //1.非空判断
        if (product == null || product.getProductId() == null || shopId < 0) {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
        boolean affect = false;
        //设置默认属性
        product.setLastEditTime(new Date());

        //2.判断缩略图是否需要处理
        if (productThumbnailImageHolder != null) {

            //删除缩略图
            File file = new File(PathUtil.getImgBasePath()
                    + productDao.selectProductByProductId(product.getProductId()).getImgAddr());
            affect = file.delete();
            //添加缩略图
            //处理新图片  三个参数: 缩略图文件流,缩略图文件名称,缩略图保存路径
            String imgAddr = ImageUtil.generateThumbnail(productThumbnailImageHolder.getImage(), productThumbnailImageHolder.getImageName(),
                    PathUtil.getShopImagePath(shopId));
            //将图片地址保存到对象中- 更新库中的缩略图地址
            product.setImgAddr(imgAddr);
        }
        //3.判断详情图片是否需要处理
        //查询已有的详情图片并删除
        if (productImgListImageHolder != null && productImgListImageHolder.size() > 0) {
            List<ProductImg> oldProductImgList = null;
            List<ProductImg> newProductImgList = null;
            //删除已有的详情图片
            oldProductImgList = productImgDao.selectProductImgList(product.getProductId());
            if (oldProductImgList != null && oldProductImgList.size() > 0) {
                for (ProductImg productImg : oldProductImgList) {
                    //删除图片文件和路径
                    File file = new File(PathUtil.getImgBasePath() + productImg.getImgAddr());
                    affect = file.delete();
                }
            }
            //详情图片文件流处理
            newProductImgList = new ArrayList<>();
            //遍历前端传入的文件流
            for (ImageHolder imageHolder : productImgListImageHolder) {
                ProductImg productImg = new ProductImg();
                //图片处理及存储
                String imageAddress = ImageUtil.generateNormalImg(imageHolder, PathUtil.getShopImagePath(shopId));
                //初始化图片信息 图片地址,创建时间,所属商品ID
                productImg.setImgAddr(imageAddress);
                productImg.setCreateTime(new Date());
                productImg.setProductID(product.getProductId());
                newProductImgList.add(productImg);
            }
            try {
                //删除库中详情图片
                productImgDao.deleteProductImgByProductId(product.getProductId());
            } catch (Exception e) {
                throw new ProductOperationException("详情图片删除失败 error:" + e.getMessage());
            }
            try {
                //插入新图片
                int effectedNum = productImgDao.insertProductImg(newProductImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败 error:" + e.getMessage());
            }
        }
        //4.更新数据
        try {
            int affectInt = productDao.updateProduct(product, shopId);
            //根据受影响行数返回不同的处理结果
            if (affectInt > 0) {
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } else {
                throw new ProductOperationException("更新图片失败");
            }
        } catch (Exception e) {
            throw new ProductOperationException("更新商品信息失败:" + e.getMessage());
        }
    }

    /**
     * 根据条件查询商品列表
     *
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ProductExecution queryProductList(Product productCondition, int pageIndex, int pageSize) {
        try {
            List<Product> productList = productDao.selectProductList(productCondition, pageIndex, pageSize);
            if (productList.size() > 0) {
                ProductExecution productExecution = new ProductExecution(ProductStateEnum.SUCCESS, productList);
                productExecution.setCount(productList.size());
                return productExecution;
            } else {
                return new ProductExecution(ProductStateEnum.INNER_ERROR);
            }
        } catch
        (ProductOperationException e) {
            throw new ProductOperationException("商品信息查询失败:" + e.getMessage());
        }
    }

    /**
     * 根据ID查询商品信息
     *
     * @param productId 商品ID
     * @return DTO
     */
    @Override
    public ProductExecution queryProductById(long productId) {
        //商品ID的非空判断
        if (productId < 0) {
            throw new ProductOperationException("productId is error");
        }
        Product product = null;
        //捕获异常
        try {
            //查询商品
            product = productDao.selectProductByProductId(productId);
            //封装商品返回数据
            if (product != null) {
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } else {
                return new ProductExecution(ProductStateEnum.ILLEGAL_PARAM);
            }
        } catch (Exception e) {
            throw new ProductOperationException("商品查询失败:" + e.getMessage());
        }
    }

    /**
     * 根据shopId获取店铺下商品列表
     *
     * @param shopId 店铺ID
     * @return 店铺列表
     */
    @Override
    public ProductExecution queryProductList(long shopId) {
        //1.shopId的非空判断
        if (shopId < 1) {
            return new ProductExecution(ProductStateEnum.ILLEGAL_PARAM);
        }
        List<Product> productList = null;
        try {
            //2.获取productList
            productList = productDao.selectProductByShopId(shopId);
            //3.返回productList

        } catch (Exception e) {
            throw new ProductOperationException("商品查询失败:" + e.getMessage());
        }
        return new ProductExecution(ProductStateEnum.SUCCESS, productList);
    }

    /**
     * 添加商品方法
     *
     * @param product                     商品对象
     * @param productThumbnailImageHolder 商品缩略图对象
     * @param productImgListImageHolder   商品详情图片对象
     * @return 商品状态
     * @throws ProductOperationException 运行时异常
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder productThumbnailImageHolder, List<ImageHolder> productImgListImageHolder)
            throws ProductOperationException {
        //product空值判断
        if (product == null || product.getShop() == null || product.getShop().getShopId() == null) {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
        //商品信息初始化
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);
        //缩略图处理
        if (productThumbnailImageHolder != null) {
            //添加商品缩略图
            addProductThumbnail(product, productThumbnailImageHolder);
        }
        try {
            //添加商品对象
            int affect = productDao.insertProduct(product);
            if (affect < 1) {
                //插入失败
                throw new ProductOperationException("商品创建失败");
            }
        } catch (Exception e) {
            throw new ProductOperationException("商品创建失败:" + e.getMessage());
        }
        //若商品详情图片不为空,则更新商品详情图片列表
        if (productImgListImageHolder != null && productImgListImageHolder.size() > 0) {
            addProductImageList(product, productImgListImageHolder);
        }
        //商品操作成功
        return new ProductExecution(ProductStateEnum.SUCCESS, product);
    }

    /**
     * 插入商品缩略图
     *
     * @param product                     商品对象
     * @param productThumbnailImageHolder 商品缩略图对象
     */
    private void addProductThumbnail(Product product, ImageHolder productThumbnailImageHolder) {
        //商品缩略图保存在商铺目录下
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        //图片处理
        String productThumbnailAddress = ImageUtil.generateThumbnail(productThumbnailImageHolder.getImage(),
                productThumbnailImageHolder.getImageName(), dest);
        //设置product属性
        product.setImgAddr(productThumbnailAddress);
    }

    /**
     * 批量插入商品详情图片
     *
     * @param product                   商品对象
     * @param productImgListImageHolder 上图详情图片对象list
     */
    private void addProductImageList(Product product, List<ImageHolder> productImgListImageHolder) {
        //商品详情图片保存在店铺路径下
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        for (ImageHolder imageHolder : productImgListImageHolder) {
            //处理图片
            String imageAddress = ImageUtil.generateNormalImg(imageHolder, dest);
            //新建图片对象
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imageAddress);
            //商品ID
            productImg.setProductID(product.getProductId());
            productImg.setCreateTime(new Date());
            //将图片对象存放到list中
            productImgList.add(productImg);
        }
        //判断图片列表是否为空
        if (productImgList.size() > 0) {
            try {
                int affectNum = productImgDao.insertProductImg(productImgList);
                if (affectNum < 0) {
                    throw new ProductImgOperationException("创建商品详情图片失败");
                }
                product.setProductImgList(productImgList);
            } catch (Exception e) {
                throw new ProductImgOperationException("创建商品详情图片失败:" + e.getMessage());
            }
        }
    }
}
