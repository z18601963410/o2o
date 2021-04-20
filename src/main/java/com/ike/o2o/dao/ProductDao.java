package com.ike.o2o.dao;

import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /**
     * 添加商品
     *
     * @param product 商品对象
     * @return 返回受影响行数
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product 商品对象
     * @param shopId  商铺ID
     * @return 受影响行数
     */
    int updateProduct(@Param("productParam") Product product, @Param("shopIdParam") long shopId);

    /**
     * 查询商品列表
     *
     * @param shopId 商铺ID
     * @return 商品列表
     */
    List<Product> selectProductByShopId(long shopId);

    /**
     * 查询商品对象
     *
     * @param productId 商品ID
     * @return 商品对象
     */
    Product selectProductByProductId(@Param("productIdParam") long productId);

    /**
     * 删除商品对象
     *
     * @param productId 商品ID
     * @param shopId    所属商铺ID
     * @return 受影响行数
     */
    int deleteProduct(@Param("productIdParam") long productId, @Param("shopIdParam") long shopId);

    int updateProductCategoryIdToNull(ProductCategory productCategory);

    /**
     * 根据条件哦查询符合要求的商品(分页查询)
     *
     * @param productCondition 商品条件
     * @param pageIndex        页码
     * @param pageSize         每页条件
     * @return 商品列表
     */
    List<Product> selectProductList(@Param("productConditionParam") Product productCondition,
                                    @Param("pageIndexParam") int pageIndex, @Param("pageSizeParam") int pageSize);
}
