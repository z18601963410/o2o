package com.ike.o2o.web.shopadmin;

import com.ike.o2o.dto.EchartSeries;
import com.ike.o2o.dto.EchartXAxis;
import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.*;
import com.ike.o2o.enums.UserProductMapStateEnum;
import com.ike.o2o.service.ProductSellDailyService;
import com.ike.o2o.service.UserProductMapService;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/shopAdmin")
public class UserProductMapManagementController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;

    /**
     * 获取当前店铺下的商品消费记录
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前店铺信息和分页数据
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        //非空判断
        if (currentShop != null && pageIndex > -1 && pageSize > -1) {
            UserProductMap userProductMap = new UserProductMap();
            userProductMap.setShop(currentShop);
            //商品名模糊查找条件
            if (productName != null) {
                userProductMap.setProduct(new Product(productName));
            }
            UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductMap(userProductMap, pageIndex, pageSize);
            if (UserProductMapStateEnum.SUCCESS.getState() == userProductMapExecution.getState()) {
                modelMap.put("success", true);
                modelMap.put("userProductMapList", userProductMapExecution.getUserProductMapList());
                return modelMap;
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "查询失败");
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "查询条件错误");
            return modelMap;
        }
    }

    /**
     * 查询最近一周商品销量
     *
     * @param request request
     * @return modelMap
     */
    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取当前商铺对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取需要模糊查找的商品名称
        String productName = HttpServletRequestUtil.getString(request, "productName");
        if (currentShop != null && currentShop.getShopId() != null) {
            //条件对象
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            //判断是否有商品ID
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);

                productSellDailyCondition.setProduct(product);
            }
            //一周时间间隔计算
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            calendar.add(Calendar.DATE, -6);
            Date startTime = calendar.getTime();
            //查询符合条件的数据
            List<ProductSellDaily> productSellDailyListOfWeek = productSellDailyService.queryProductSellDaily(productSellDailyCondition, startTime, endTime);

            //统一日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名称列表,唯一性
            HashSet<String> productNameSet = new LinkedHashSet<>();
            //日期列表,唯一性
            HashSet<String> dateSet = new LinkedHashSet<>();
            //销量列表
            List<Integer> totalList = new ArrayList<>();

            // 定义series>>迎合echart里的series项,前端JS解析该列表展示数据: 每个元素包含了商品名称和对应的销量
            List<EchartSeries> series = new ArrayList<>();

            //当前商品名称,默认为空
            String currentProductName = "";

            for (int i = 0; i < productSellDailyListOfWeek.size(); i++) {
                ProductSellDaily temp = productSellDailyListOfWeek.get(i);
                //读取列表中的商品名称,利用set特性去重
                productNameSet.add(temp.getProduct().getProductName());
                //日期去重
                dateSet.add(sdf.format(temp.getCreateTime()));

                //currentProductName与当前元素的productName是否相同(取非),并且currentProductName不能为空>>首次执行回进入该函数
                if (!currentProductName.equals(temp.getProduct().getProductName()) && !"".equals(currentProductName)) {
                    EchartSeries es = new EchartSeries();
                    //插入商品名称(将上一个商品商品名称进行插入,商品A和商品B交界时执行这个函数)>>数据库数据以ID和时间为顺序排列
                    es.setName(currentProductName);
                    //插入商品销量数据 List ,商品A和商品B交界时执行这个函数)
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    //清空list中数据
                    totalList = new ArrayList<Integer>();
                    //currentProductName设置为当前商品名称
                    currentProductName = temp.getProduct().getProductName();
                    //将当前商品销量加入到list中
                    totalList.add(temp.getTotal());
                } else {
                    //如果当前商品名称与currentProductName相同则执行
                    //添加商品销量
                    totalList.add(temp.getTotal());
                    currentProductName = temp.getProduct().getProductName();
                }
                //添加队列最后一个元素的信息
                if (i == productSellDailyListOfWeek.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            //数据添加到map中
            modelMap.put("series", series);
            modelMap.put("legendData", productNameSet);//商品名称列表,图例说明
            // 拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(dateSet);//日期列表
            xAxis.add(exa);

            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "currentShopId is empty!");
            return modelMap;
        }
    }

}
