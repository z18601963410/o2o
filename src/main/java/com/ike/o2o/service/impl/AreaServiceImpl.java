package com.ike.o2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.cache.JedisUtil;
import com.ike.o2o.dao.AreaDao;
import com.ike.o2o.entity.Area;
import com.ike.o2o.exception.AreaOperationException;
import com.ike.o2o.service.AreaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Qualifier
    private AreaDao areaDao;

    @Autowired
    public AreaServiceImpl(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    // redis 数据类型为key的工具类,该对象负责与redis服务器通信,完成数据交换工作
    @Autowired
    private JedisUtil.Keys jedisKeys;
    // redis 数据类型为String的工具类,该对象负责与redis服务器通信,完成数据交换工作
    @Autowired
    private JedisUtil.Strings jedisStrings;

    //redis服务器保存areaList所使用的key
    private static String AREA_LIST_KEY = "arealist";

    //日志对象
    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional//添加事务管理,涉及到redis服务器数据的读写
    public List<Area> getAreaList() {
        //定义redis服务器中的key
        String key = AREA_LIST_KEY;
        //定义接收对象
        List<Area> areaList = null;
        //定义jackson转换对象
        ObjectMapper objectMapper = new ObjectMapper();

        //工具类检查是否redis服务器中是否存在key  !jedisKeys.exists(key)  --禁用redis
        if (!jedisKeys.exists(key)) {
            //如果不存在指定的key,到数据库查询数据
            areaList = areaDao.queryArea();
            //将数据保存到redis服务器中->后续则从redis服务器中提取数据
            String jsonString;
            try {
                //将List对象转换为String对象
                jsonString = objectMapper.writeValueAsString(areaList);
            } catch (Exception e) {
                e.printStackTrace();
                //记录日志
                //返回异常,停止程序执行
                throw new AreaOperationException(e.getMessage());
            }
            //将数据插入到redis中 key=AREA_LIST_KEY
            jedisStrings.setEx(key, 1000, jsonString);

        } else {
            //若key存在,则直接从redis中提取数据
            String jsonString = jedisStrings.get(key);
            //构建一个指定的集合类型List<Area>
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                //将String转换为指定的集合类型对象 String->List<Area>
                areaList = objectMapper.readValue(jsonString, javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }


    @Override
    @Transactional
    public boolean editArea(Area area) {
        if (area != null && area.getAreaId() != null) {
            int affect = areaDao.updateArea(area);
            //清除缓存
            jedisKeys.del(AREA_LIST_KEY);
            return affect > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean addArea(Area area) {
        if (area != null) {
            //初始化参数
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            int affect = areaDao.insertArea(area);
            //清除缓存
            jedisKeys.del(AREA_LIST_KEY);
            return affect > 0;
        }
        return true;
    }


}
