package com.ike.o2o.dao;

import com.ike.o2o.entity.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductImgDao {
    /**
     * 更新店铺的Img信息
     *
     * @return 返回受影响行数
     */
    int insertProductImg(List<ProductImg> productImgList);

    /**
     * 查询图片列表
     *
     * @param productId 商品ID
     * @return 返回商品的所有详情图片
     */
    List<ProductImg> selectProductImgList(long productId);

    /**
     * 根据商品ID删除详情图片
     *
     * @param productId 商品ID
     * @return 受影响行数
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 更新详情图片
     *
     * @param productId         商品ID
     * @param newProductImgList 新的商品图片
     * @return 返回受影响行数
     */
    int updateProductImgByProductId(@Param("newProductImgListParam") List<ProductImg> newProductImgList, @Param("productIdParam") long productId);

}
