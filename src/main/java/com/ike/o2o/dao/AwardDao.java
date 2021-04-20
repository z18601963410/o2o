package com.ike.o2o.dao;

import com.ike.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 礼品对象Dao
 */
public interface AwardDao {

    /**
     * 新增礼品对象
     *
     * @param award 新增对象
     * @return 受影响行数
     */
    int insertAward(Award award);

    /**
     * 删除礼品对象
     *
     * @param awardId 礼品对象ID
     * @param shopId  礼品对象所属商铺ID
     * @return 受影响行数
     */
    int deleteAward(@Param("awardIdCondition") Long awardId, @Param("shopIdCondition") Long shopId);

    /**
     * 更新礼品对象
     *
     * @param award  礼品对象实体
     * @param shopId 商铺ID
     * @return 受影响行数
     */
    int updateAward(@Param("awardCondition") Award award, @Param("shopIdCondition") Long shopId);

    /**
     * 根据条件查询符合要求礼品对象
     *
     * @param award 包含条件的礼品对象实体
     * @return 符合条件的礼品对象列表
     */
    List<Award> queryAward(@Param("awardCondition") Award award,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);

    /**
     * 根据ID查询礼品对象
     *
     * @param awardId 礼品对象ID
     * @return 礼品对象实体
     */
    Award queryAwardById(Long awardId);

    /**
     * 查询符合条件的礼品对象长度 配合List<Award> queryAward使用
     *
     * @param award 条件实体
     * @return 长度
     */
    int queryAwardCount(@Param("awardCondition") Award award);
}
