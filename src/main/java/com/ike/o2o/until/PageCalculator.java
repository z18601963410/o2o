package com.ike.o2o.until;

//分页工具
public class PageCalculator {
    /**
     * 根据页码和pageSize返回rowIndex,数据库查询使用
     *
     * @param pageIndex pageIndex
     * @param pageSize  pageSize
     * @return rowIndex
     */
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
