package com.ike.o2o.service;

import com.ike.o2o.dto.AwardExecution;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.entity.Award;

/**
 * 奖品管理:  list列表/指定ID预览/添加/删除/编辑
 */
public interface AwardService {

    /**
     * 查询店铺下所有的奖品
     *
     * @param awardCondition 查询条件实体
     * @return dto对象
     */
    AwardExecution queryAwardListByShop(Award awardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 获取店铺下指定奖品
     *
     * @param awardId awardId
     * @return Award
     */
    Award queryAwardByAwardId(long awardId);

    /**
     * 添加奖品对象
     *
     * @param award 奖品对象实体
     * @return dto
     */
    AwardExecution addAward(Award award, ImageHolder awardImg);

    /**
     * 编辑奖品对象
     *
     * @param award 奖品对象实体
     * @return dto
     */
    AwardExecution editAward(Award award, ImageHolder awardImg);

    /**
     * 删除奖品对象
     *
     * @param shopId  shopId
     * @param awardId awardId
     * @return dto
     */
    AwardExecution removeAward(long shopId, long awardId);
}
