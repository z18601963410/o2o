package com.ike.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.cache.JedisUtil;
import com.ike.o2o.dao.HeadLineDao;
import com.ike.o2o.dto.HeadLineExecution;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.entity.HeadLine;
import com.ike.o2o.enums.HeadLineStateEnum;
import com.ike.o2o.exception.HeadLineOperationException;
import com.ike.o2o.service.HeadLineService;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PathUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    //日志对象
    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);


    //redis操作对象
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    //redis存储头条数据的key值
    private static String HEAD_LINE_KEY = "headlinekey";
    //数据转换对象
    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Override
    public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder headLineImage) throws HeadLineOperationException {
        //非空判断
        if (headLine != null && headLineImage != null) {
            //初始化参数
            headLine.setEnableStatus(1);
            headLine.setCreateTime(new Date());
            headLine.setLastEditTime(new Date());
            //图片处理
            String dest = PathUtil.getHeadLineImagePath();
            String imgAddress = ImageUtil.generateNormalImg(headLineImage, dest);
            headLine.setLineImg(imgAddress);
            //对象处理
            try {
                int affect = headLineDao.insertHeadLine(headLine);
                if (affect > 0) {
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
                } else {
                    throw new HeadLineOperationException("头条创建失败");
                }
            } catch (HeadLineOperationException e) {
                throw new HeadLineOperationException("头条插入失败:" + e.getMessage());
            }
        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }

    @Transactional
    @Override
    public HeadLineExecution addHeadLineList(List<HeadLine> headLineList, List<ImageHolder> imageHolderList) throws HeadLineOperationException {
        //非空判断
        if (headLineList != null && headLineList.size() > 0 &&
                imageHolderList != null && imageHolderList.size() > 0) {
            List<HeadLine> tempList = new ArrayList<>();
            String dest = PathUtil.getHeadLineImagePath();
            for (int i = 0; i < headLineList.size(); i++) {
                if (headLineList.get(i) != null) {
                    //初始化参数
                    HeadLine headLineTemp = headLineList.get(i);
                    headLineTemp.setEnableStatus(1);
                    headLineTemp.setCreateTime(new Date());
                    headLineTemp.setLastEditTime(new Date());
                    //图片处理
                    String imgAddress = ImageUtil.generateNormalImg(imageHolderList.get(i), dest);
                    headLineTemp.setLineImg(imgAddress);
                    //将头条对象添加到集合中
                    tempList.add(headLineTemp);
                }
            }
            if (tempList.size() > 0) {
                try {
                    int affect = headLineDao.insertHeadLineList(tempList);
                    if (affect > 0) {
                        return new HeadLineExecution(HeadLineStateEnum.SUCCESS, tempList);
                    } else {
                        throw new HeadLineOperationException("头条批量插入失败");
                    }
                } catch (HeadLineOperationException e) {
                    throw new HeadLineOperationException("头条插入失败:" + e.getMessage());
                }
            } else {
                throw new HeadLineOperationException("头条批量插入失败:tempList.size() < 0");
            }
        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }

    @Transactional
    @Override
    public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder headLineImage) throws HeadLineOperationException {
        if (headLine != null && headLine.getLineId() != null) {
            //判断是否需要处理图片
            if (headLineImage != null) {
                long lineId = headLine.getLineId();
                //删除已有图片
                boolean isTrue = new File(PathUtil.getHeadLineImagePath() +
                        queryHeadLineByHeadLineId(lineId).getHeadLine().getLineImg()).delete();
                //重置图片地址
                headLine.setLineImg(ImageUtil.generateNormalImg(headLineImage, PathUtil.getHeadLineImagePath()));
            }
            //更新图片信息
            try {
                int affect = headLineDao.updateHeadLine(headLine);
                if (affect > 0) {
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            } catch (HeadLineOperationException e) {
                throw new HeadLineOperationException("头条更新失败:" + e.getMessage());
            }

        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }

    @Transactional
    @Override
    public HeadLineExecution removeHeadLine(long headLineId) throws HeadLineOperationException {
        if (headLineId > 0) {
            try {
                int affect = headLineDao.deleteHeadLine(headLineId);
                if (affect > 0) {
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, new HeadLine());
                } else {
                    throw new HeadLineOperationException("删除头条失败");
                }
            } catch (HeadLineOperationException e) {
                throw new HeadLineOperationException(e.toString());
            }

        }
        return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
    }

    @Override
    @Transactional
    public HeadLineExecution queryHeadLineList(HeadLine headLineCondition) throws HeadLineOperationException {
        if (headLineCondition != null) {
            //redis数据存储的key
            String key = HEAD_LINE_KEY;
            //封装数据
            List<HeadLine> headLineList;
            //headLineCondition对查询条件做了限定此处对key也要做限制
            if (headLineCondition.getEnableStatus() != null) {
                //将key值附加上查询可用状态条件的限制: 0表示不可用,1表示可用 >HEAD_LINE_KEY_0
                key = key + "_" + headLineCondition.getEnableStatus();
            }
            //判断redis是否存在该key  !jedisKeys.exists(key)  禁用redis
            if (!jedisKeys.exists(key)) {
                //如果不存在key则从数据库提取数据
                headLineList= headLineDao.selectHeadLineList(headLineCondition);

                if (headLineList != null && headLineList.size() > 0) {
                    try {
                        //将list转换为String
                        String headLineListAsString = objectMapper.writeValueAsString(headLineList);
                        //保存到redis
                        jedisStrings.set(key, headLineListAsString);

                    } catch (Exception e) {
                        e.printStackTrace();
                        //记录错误日志
                        logger.error(e.getMessage());
                        //返回异常,回滚数据
                        throw new HeadLineOperationException(e.getMessage());
                    }
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLineList);
                } else {
                    return new HeadLineExecution(HeadLineStateEnum.EMPTY);
                }
            } else {
                //如果查询key为true则从redis中提取数据
                String jsonString = jedisStrings.get(key);
                //数据转换
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
                try {
                    headLineList= objectMapper.readValue(jsonString, javaType);
                }catch (Exception e){
                    e.printStackTrace();
                    //记录错误日志
                    logger.error(e.getMessage());
                    //返回异常,回滚数据
                    throw new HeadLineOperationException(e.getMessage());
                }
                return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLineList);
            }
        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }

    @Override
    public HeadLineExecution queryHeadLineByHeadLineId(long headLineId) {
        if (headLineId > 0) {
            HeadLine headLine = headLineDao.selectHeadLineByHeadLineId(headLineId);
            if (headLine != null) {
                return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
            }
            return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }

    @Override
    public HeadLineExecution queryHeadLineByHeadLineIdList(List<Long> headLineIdList) {
        if (headLineIdList != null && headLineIdList.size() > 0) {
            List<HeadLine> headLineList = headLineDao.selectHeadLineByHeadLineIdList(headLineIdList);
            if (headLineList != null && headLineList.size() > 0) {
                return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLineList);
            }
            return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
        }
        return new HeadLineExecution(HeadLineStateEnum.EMPTY);
    }
}
