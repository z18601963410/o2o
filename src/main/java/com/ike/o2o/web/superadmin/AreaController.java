package com.ike.o2o.web.superadmin;

import com.ike.o2o.entity.Area;
import com.ike.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller //在spring容器中注册为控制器
@RequestMapping("/superAdmin") //访问路由
public class AreaController {
    Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listArea", method = RequestMethod.GET)//方法的请求路由和请求方式
    @ResponseBody //json形式返回
    private Map<String, Object> listArea() {
        //日志
        logger.info("===start===");
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list = new ArrayList<>();
        try {
            list = areaService.getAreaList();
            modelMap.put("row", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.getStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMg", e.getMessage());
        }
        //日志
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - startTime);
        logger.info("===end===");
        return modelMap;
    }
}
