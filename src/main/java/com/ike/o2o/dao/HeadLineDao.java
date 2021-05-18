package com.ike.o2o.dao;



import com.ike.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {
    /**
     * 插入头条
     *
     * @param headLine 头条对象
     * @return 受影响行数
     */
    int insertHeadLine(HeadLine headLine);

    /**
     * 批量插入 头条
     *`
     * @param headLineList 头条列表
     * @return 受影响行数
     */
    int insertHeadLineList(List<HeadLine> headLineList);

    /**
     * 更新头条
     *
     * @param headLine 头条对象
     * @return 受影响行数
     */
    int updateHeadLine(@Param("headLineParam") HeadLine headLine);

    /**
     * 根据ID删除头条对象
     *
     * @param headLineId 头条ID
     * @return 受影响行数
     */
    int deleteHeadLine(long headLineId);

    /**
     * 根据条件查询头条
     *
     * @param headLineCondition 包含条件的头条对象 头条名模糊查询 和 状态查询
     * @return 头条列表
     */
    List<HeadLine> queryHeadLineList(HeadLine headLineCondition);

    /**
     * 根据头条ID查询对象
     *
     * @param headLineId 头条ID
     * @return 头条对象
     */
    HeadLine queryHeadLineByHeadLineId(long headLineId);

    /**
     * 根据头条ID集合查询对应头条集合
     *
     * @param headLineIdList 头条ID集合
     * @return 头条对象集合
     */
    List<HeadLine> queryHeadLineByHeadLineIdList(List<Long> headLineIdList);

    /**
     * 查询头条长度
     * @param headLineCondition  查询条件
     * @return count
     */
    int queryHeadLineListCount(HeadLine headLineCondition);
}
