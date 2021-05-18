package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ShopAuthMapDao;
import com.ike.o2o.dao.ShopDao;
import com.ike.o2o.dto.ShopCategoryExecution;
import com.ike.o2o.dto.ShopExecution;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.enums.ShopStateEnum;
import com.ike.o2o.exception.ShopOperationException;
import com.ike.o2o.service.ShopService;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PageCalculator;
import com.ike.o2o.until.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public Shop getByShopId(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {

        //判断shop对象是否为空,空则直接返回
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);//返回shop的DTO对象并将状态填充进去
        }

        try {
            //1.判断是否需要图片处理,将原有图片删除操作
            if (shopImgInputStream != null && !"".equals(fileName)) {
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                if (tempShop.getShopImg() != null) {//数据库查询对象后判断图片地址是否为空,非空则删除图片路径或文件
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());//删除本地文件活路径
                }
                addShopImg(shop, shopImgInputStream, fileName);//更新数据图片信息
            }

            //2.更新店铺信息
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);//更新shop对象
            if (effectedNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);//更新操作受影响行数小于等于0则 返回出入错误状态
            } else {
                shop = shopDao.queryByShopId(shop.getShopId());//查询进行了更新操作的shop对象
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);//店铺更新标识为success,并返回对象
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopOperationException("modify shop error:" + e.getMessage());
        }
    }

    @Override
    @Transactional //开启事务
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {
        //如果店铺为空直接返回
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //设置店铺状态
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //设置店铺出师状态
            shop.setAdvice(ShopStateEnum.stateOf(0).getStateInfo());
            //执行插入操作
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            } else {
                //更新图片
                if (shopImgInputStream != null) {
                    addShopImg(shop, shopImgInputStream, fileName);
                }
                shopDao.updateShop(shop);
                //添加店铺授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                shopAuthMap.setCreateTime(new Date());
                shopAuthMap.setLastEditTime(new Date());
                shopAuthMap.setTitleFlag(0);
                shopAuthMap.setTitle("店家");
                shopAuthMap.setEnableStatus(1);
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(shop.getOwner());
                shopAuthMapDao.insertShopAuth(shopAuthMap);
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        //返回店铺信息
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /**
     * 查询商铺列表
     *
     * @param shopCondition 查询条件
     * @param pageIndex     页码
     * @param pageSize      每页行数
     * @return ShopExecution
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //获取分页码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        //根据分页码获取shopList
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        //获取店铺总数(根据shop对象所属对象查询)
        int shopCount = shopDao.queryShopCount(shopCondition);
        //shop的DTO对象-->封装shop对象和添加其他附加信息
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            //将shopList封装到DTO对象中
            se.setShopList(shopList);
            //将店铺总量封装到DTO对象中
            se.setCount(shopCount);
            //设置状态
            se.setState(ShopStateEnum.SUCCESS.getState());
        } else {
            //查询失败返回系统错误
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) throws IOException {

        //获取shop图片目录的相对路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());

        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);

        shop.setShopImg(shopImgAddr);
    }
}
