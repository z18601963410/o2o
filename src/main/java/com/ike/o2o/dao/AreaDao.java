package com.ike.o2o.dao;



import com.ike.o2o.entity.Area;

import java.util.List;

public interface AreaDao {
    /**
     * 获取区域列表
     * @return 区域列表
     */
    List<Area> queryArea();
}
