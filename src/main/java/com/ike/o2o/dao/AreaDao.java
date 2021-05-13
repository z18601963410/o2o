package com.ike.o2o.dao;


import com.ike.o2o.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaDao {
    /**
     * 获取区域列表
     *
     * @return 区域列表
     */
    List<Area> queryArea();

    /**
     * 新增区域类别
     *
     * @param area 新增对象
     * @return 受影响行数
     */
    int insertArea(Area area);

    /**
     * 修改区域对象
     *
     * @param area 区域对象
     * @return 受影响行数
     */
    int updateArea(@Param("areaCondition") Area area);

    /**
     * 删除区域对象
     *
     * @param areaId 区域对象ID
     * @return 受影响行数
     */
    int deleteArea(long areaId);

}
