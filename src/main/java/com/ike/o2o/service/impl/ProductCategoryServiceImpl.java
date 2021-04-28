package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ProductCategoryDao;
import com.ike.o2o.dao.ProductDao;
import com.ike.o2o.dto.ProductCategoryExecution;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.enums.ProductCategoryStateEnum;
import com.ike.o2o.exception.ProductCategoryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements com.ike.o2o.service.ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 获取店铺商品类型列表
     *
     * @param currentShopId
     * @return
     */
    @Override
    public List<ProductCategory> getProductCategoryList(long currentShopId) {
        return productCategoryDao.queryProductCategoryList(currentShopId);
    }

    /**
     * 添加商铺商品信息
     *
     * @param productCategories 商铺商品类型信息列表
     * @return 收影响行数
     */
    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories, long ShopId) {

        for (ProductCategory productCategory : productCategories
        ) {
            //判断添加的商品分类的商铺ID是否非法
            if (productCategory.getShopId() != ShopId) {
                return new ProductCategoryExecution(ProductCategoryStateEnum.OFFLINE);
            }
        }
        //执行插入
        try {
            int affect = productCategoryDao.insertProductCategoryList(productCategories);
            if (affect > 0) {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategories);
            }else{
                throw new ProductCategoryOperationException("商品分类新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductCategoryOperationException("商品分类插入失败");
        }
    }

    /**
     * 删除商铺商品分类
     *
     * @return 返回删除结果
     */
    @Override
    @Transactional
    public ProductCategoryExecution removeProductCategory(ProductCategory productCategory) throws ProductCategoryOperationException {
        try {
            //将该商品分类的商品的productCategoryId置为null
            int pdAffect = productDao.updateProductCategoryIdToNull(productCategory);
            //操作成功时不直接提交,需要if判断成功后才能提交,分两步进行操作,使用事务进行管理
            int affect = productCategoryDao.deleteProductCategory(productCategory);
            if (affect > 0) {
                //返回操作成功
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            } else {
                throw new ProductCategoryOperationException("商品类别删除失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("removeProductCategory error:" + e.getMessage());
        }
    }
}
