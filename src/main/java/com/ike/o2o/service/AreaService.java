package com.ike.o2o.service;

import com.ike.o2o.entity.Area;

import java.util.List;

/**
 * 获取区域列表接口
 */
public interface AreaService {
    List<Area> getAreaList();

    boolean editArea(Area area);

    boolean addArea(Area area);
}
