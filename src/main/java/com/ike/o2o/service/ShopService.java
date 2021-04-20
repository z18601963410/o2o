package com.ike.o2o.service;

import com.ike.o2o.dto.ShopExecution;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.exception.ShopOperationException;
import jdk.internal.util.xml.impl.Input;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    //添加shop对象
    ShopExecution addShop(Shop shop, InputStream shopImg, String fileName) throws ShopOperationException;

    //获取店铺列表
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    //获取指定ID的shop对象
    Shop getByShopId(Long shopId);

    //修改shop对象
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
