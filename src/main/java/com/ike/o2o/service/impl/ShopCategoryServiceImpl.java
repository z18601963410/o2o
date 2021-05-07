package com.ike.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.cache.JedisUtil;
import com.ike.o2o.dao.ShopCategoryDao;
import com.ike.o2o.dto.ShopCategoryExecution;
import com.ike.o2o.entity.ShopCategory;
import com.ike.o2o.enums.ShopCategoryStateEnum;
import com.ike.o2o.exception.ShopCategoryOperationException;
import com.ike.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    //redis数据管理
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    //数据转换对象
    private ObjectMapper objectMapper = new ObjectMapper();
    //设置key值
    private static String SHOP_CATEGORY_LIST_KEY = "shopcategorykey";
    //日志对象
    private Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);


    @Override
    public ShopCategoryExecution getShopCategoryList(ShopCategory shopCategoryConditionParam) {
        //key
        String key = SHOP_CATEGORY_LIST_KEY;

        if (shopCategoryConditionParam!=null && shopCategoryConditionParam.getParent() == null) {
            key = key + "_" + "parent_null";
        } else if (shopCategoryConditionParam!=null &&shopCategoryConditionParam.getParent() != null
                && shopCategoryConditionParam.getParent().getShopCategoryId() == null) {
            key = key + "_" + "parent_categoryId_null";
        } else if (shopCategoryConditionParam!=null &&shopCategoryConditionParam.getParent() != null
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
                jedisStrings.setEx(key,1000,listToString);
                //返回数据
                return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());//触发回滚
            }
        }
    }
}
