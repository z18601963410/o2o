package com.ike.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.cache.JedisUtil;
import com.ike.o2o.dao.ShopCategoryDao;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.dto.ShopCategoryExecution;
import com.ike.o2o.entity.ShopCategory;
import com.ike.o2o.enums.ShopCategoryStateEnum;
import com.ike.o2o.exception.ShopCategoryOperationException;
import com.ike.o2o.service.ShopCategoryService;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    private ShopCategoryDao shopCategoryDao;
    //redis数据管理
    private JedisUtil.Keys jedisKeys;
    private JedisUtil.Strings jedisStrings;

    @Autowired
    public ShopCategoryServiceImpl(ShopCategoryDao shopCategoryDao, JedisUtil.Keys jedisKeys, JedisUtil.Strings jedisStrings) {
        this.shopCategoryDao = shopCategoryDao;
        this.jedisKeys = jedisKeys;
        this.jedisStrings = jedisStrings;
    }

    //数据转换对象
    private ObjectMapper objectMapper = new ObjectMapper();
    //设置key值
    private static String SHOP_CATEGORY_LIST_KEY = "shopcategorykey";
    //日志对象
    private Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    /**
     * 获取商铺分类信息
     *
     * @param shopCategoryConditionParam 筛选条件
     * @return dto
     */
    @Override
    public ShopCategoryExecution getShopCategoryList(ShopCategory shopCategoryConditionParam) {
        //key
        String key = SHOP_CATEGORY_LIST_KEY;

        if (shopCategoryConditionParam != null && shopCategoryConditionParam.getParent() == null) {
            key = key + "_" + "parent_null";
        } else if (shopCategoryConditionParam != null && shopCategoryConditionParam.getParent() != null
                && shopCategoryConditionParam.getParent().getShopCategoryId() == null) {
            key = key + "_" + "parent_categoryId_null";
        } else if (shopCategoryConditionParam != null && shopCategoryConditionParam.getParent() != null
                && shopCategoryConditionParam.getParent().getShopCategoryId() != null) {
            key = key + "_" + "parent_categoryId_" + shopCategoryConditionParam.getParent().getShopCategoryId();
        }

        //检查key是否存在  jedisKeys.exists(key)  禁用redis
        if (jedisKeys.exists(key)) {
            //从redis中提取数据
            String stringObj = jedisStrings.get(key);
            //对象转换
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                //String->List<ShopCategory>
                List<ShopCategory> shopCategoryList = objectMapper.readValue(stringObj, javaType);
                //返回数据
                return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());//触发回滚
            }
        } else {
            //从数据库提取数据
            List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryConditionParam);
            try {
                //数据转换
                String listToString = objectMapper.writeValueAsString(shopCategoryList);
                //保存到redis中
                jedisStrings.setEx(key, 1000, listToString);
                //返回数据
                return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());//触发回滚
            }
        }
    }

    /**
     * 新增商铺分类对象
     *
     * @param shopCategory    shopCategory
     * @param shopCategoryImg 图片对象
     * @return ShopCategoryExecution
     */
    @Override
    @Transactional
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder shopCategoryImg) {
        ShopCategoryExecution shopCategoryExecution = new ShopCategoryExecution();
        //非空判断
        if (shopCategory != null && shopCategoryImg != null) {
            //初始化参数
            shopCategory.setCreateTime(new Date());
            shopCategory.setLastEditTime(new Date());
            //执行添加
            int affect;
            try {
                //图片添加
                String imgAddress = ImageUtil.generateNormalImg(shopCategoryImg, PathUtil.getShopCategoryPath());
                if (imgAddress != null) {
                    shopCategory.setShopCategoryImg(imgAddress);
                } else {
                    shopCategoryExecution.setState(ShopCategoryStateEnum.INNER_ERROR.getState());
                    shopCategoryExecution.setStateInfo("图片地址为空!");
                }
                //添加分类
                affect = shopCategoryDao.insertShopCategory(shopCategory);
                if (affect > 0) {
                    shopCategoryExecution.setState(ShopCategoryStateEnum.SUCCESS.getState());
                    //清除redis缓存数据>>使用key的匹配模式
                    for (String key : jedisKeys.keys(SHOP_CATEGORY_LIST_KEY + "*")
                    ) {
                        jedisKeys.del(key);
                    }
                } else {
                    shopCategoryExecution.setState(ShopCategoryStateEnum.INNER_ERROR.getState());
                    shopCategoryExecution.setStateInfo("对象插入影响行数为小于1");
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("添加商铺分类对象出现异常:" + e.getMessage());
            }
        } else {
            shopCategoryExecution.setState(ShopCategoryStateEnum.NULL_SHOP.getState());
        }
        return shopCategoryExecution;
    }

    /**
     * 编辑商铺分类对象
     *
     * @param shopCategory    shopCategory
     * @param shopCategoryImg ImageHolder
     * @return dto
     */
    @Override
    @Transactional
    public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder shopCategoryImg) {
        ShopCategoryExecution shopCategoryExecution = new ShopCategoryExecution();
        //非空判断
        if (shopCategory != null && shopCategory.getShopCategoryId() != null) {
            try {
                //判断是否需要进行图片处理
                if (shopCategoryImg != null) {
                    shopCategory.setShopCategoryImg(ImageUtil.generateNormalImg(shopCategoryImg, PathUtil.getShopCategoryPath()));
                }
                //更新对象
                int affect = shopCategoryDao.updateShopCategory(shopCategory);
                if (affect > 0) {
                    shopCategoryExecution.setState(ShopCategoryStateEnum.SUCCESS.getState());
                    //清除redis缓存数据>>使用key的匹配模式
                    for (String key : jedisKeys.keys(SHOP_CATEGORY_LIST_KEY + "*")
                    ) {
                        jedisKeys.del(key);
                    }
                } else {
                    shopCategoryExecution.setState(ShopCategoryStateEnum.INNER_ERROR.getState());
                    shopCategoryExecution.setStateInfo("对象更新影响行数为小于1");
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("添加商铺分类对象出现异常:" + e.getMessage());
            }
        } else {
            shopCategoryExecution.setState(ShopCategoryStateEnum.NULL_SHOP.getState());
            shopCategoryExecution.setStateInfo("shopCategory or shopCategory.getShopCategoryId() is null !");
        }
        return shopCategoryExecution;
    }


}
