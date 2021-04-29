package com.ike.o2o.service.impl;

import com.ike.o2o.dao.AwardDao;
import com.ike.o2o.dto.AwardExecution;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.entity.Award;
import com.ike.o2o.enums.AwardStateEnum;
import com.ike.o2o.exception.AwardOperationException;
import com.ike.o2o.service.AwardService;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PageCalculator;
import com.ike.o2o.until.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(AwardServiceImpl.class);
    @Autowired
    private AwardDao awardDao;

    /**
     * 获取奖品列表分页数据
     *
     * @param awardCondition 查询条件实体
     * @param pageIndex      页码
     * @param pageSize       size
     * @return AwardExecution
     */
    @Override
    public AwardExecution queryAwardListByShop(Award awardCondition, Integer pageIndex, Integer pageSize) {
        //非空判断
        if (awardCondition != null && pageIndex != null && pageSize != null) {
            //页码转为索引码
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<Award> awardList;
            int count = -1;
            try {
                //获取对象列表
                awardList = awardDao.queryAward(awardCondition, rowIndex, pageSize);
                //获取长度
                count = awardDao.queryAwardCount(awardCondition);

            } catch (Exception e) {
                logger.error("获取奖品列表或获取列表对象长度出现异常!:" + e.getMessage());
                throw new AwardOperationException(e.getMessage());
            }
            //封装数据
            AwardExecution awardExecution = new AwardExecution();
            awardExecution.setAwardList(awardList);
            awardExecution.setCount(count);
            //成功标志
            awardExecution.setState(AwardStateEnum.SUCCESS.getState());
            //返回DTO
            return awardExecution;
        }
        return new AwardExecution();
    }

    @Override
    public Award queryAwardByAwardId(long awardId) {
        //基本判断
        if (awardId > 0) {
            return awardDao.queryAwardById(awardId);
        }
        return null;
    }

    @Override
    public AwardExecution addAward(Award award, ImageHolder awardImg) throws AwardOperationException {
        AwardExecution awardExecution = new AwardExecution();
        //非空校验
        if (award != null && award.getShopId() != null) {
            //默认值
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            award.setEnableStatus(0);//默认不可用,需要管理员审核
            //详情图片处理
            String imgAddress = ImageUtil.generateNormalImg(awardImg, PathUtil.getAwardPath());
            award.setAwardImg(imgAddress);

            //添加对象
            int affect = -1;
            try {
                affect = awardDao.insertAward(award);
            } catch (Exception e) {
                //记录日志
                logger.error(e.getMessage());
                throw new AwardOperationException("添加奖品对象时出现异常!");
            }
            if (affect > 0) {
                awardExecution.setAward(award);
                awardExecution.setState(AwardStateEnum.SUCCESS.getState());
            } else {
                //返回错误对象
                awardExecution.setState(AwardStateEnum.INNER_ERROR.getState());
            }
        } else {
            awardExecution.setState(AwardStateEnum.EMPTY.getState());
        }
        return awardExecution;
    }

    /**
     * 修改奖品对象
     *
     * @param award    奖品对象实体
     * @param awardImg 图片对象
     * @return
     */
    @Override
    public AwardExecution editAward(Award award, ImageHolder awardImg) throws AwardOperationException {
        AwardExecution awardExecution = new AwardExecution();
        if (award != null && award.getAwardId() != null && award.getShopId() != null) {
            int affect = -1;
            //判断图片对象是否需要处理
            if (awardImg != null && awardImg.getImageName() != null && awardImg.getImage() != null) {
                //获取当前商品对象的图片路径
                Award oldAward = awardDao.queryAwardById(award.getAwardId());
                //删除图片
                if (oldAward.getAwardImg() != null && "".equals(oldAward.getAwardImg().trim())) {
                    ImageUtil.deleteFileOrPath(oldAward.getAwardImg());
                }
                //添加新的图片
                String newImgAddress = ImageUtil.generateNormalImg(awardImg, PathUtil.getAwardPath());
                //将新的图片路径赋值给award
                award.setAwardImg(newImgAddress);
            }
            try {
                //修改对象
                affect = awardDao.updateAward(award);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new AwardOperationException("奖品更新出现异常" + e.getMessage());
            }
            if (affect > 0) {
                //状态标识
                awardExecution.setState(AwardStateEnum.SUCCESS.getState());
                awardExecution.setAward(award);
            } else {
                awardExecution.setState(AwardStateEnum.INNER_ERROR.getState());
            }
        } else {
            awardExecution.setState(AwardStateEnum.EMPTY.getState());
        }
        return awardExecution;
    }

    /**
     * 删除奖品:状态设置为不可用
     *
     * @param shopId  shopId
     * @param awardId awardId
     * @return
     * @throws AwardOperationException
     */
    @Override
    public AwardExecution removeAward(long shopId, long awardId) throws AwardOperationException {
        AwardExecution awardExecution = new AwardExecution();
        if (shopId > 0 && awardId > 0) {
            try {
                int affect = awardDao.deleteAward(awardId, shopId);
                if (affect > 0) {
                    awardExecution.setState(AwardStateEnum.SUCCESS.getState());
                } else {
                    awardExecution.setState(AwardStateEnum.INNER_ERROR.getState());
                }
            } catch (Exception e) {
                logger.error("删除奖品对象出现异常");
                throw new AwardOperationException("删除奖品出现异常:" + e.getMessage());
            }
        }
        return awardExecution;
    }
}
