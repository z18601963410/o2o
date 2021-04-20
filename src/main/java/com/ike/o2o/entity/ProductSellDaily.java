package com.ike.o2o.entity;

import java.util.Date;

//顾客消费的商品映射
public class ProductSellDaily {
    //主键Id
    private Long productSellDailyId;
    //哪天的销量，精确到天
    private Date createTime;
    //销量
    private Integer total;
    //商品实体对象
    private Product product;
    //店铺实体对象
    private Shop shop;

    @Override
    public String toString() {
        return "ProductSellDaily{" +
                "productSellDailyId=" + productSellDailyId +
                ", createTime=" + createTime +
                ", total=" + total +
                ", product=" + product +
                ", shop=" + shop +
                '}';
    }

    public Long getProductSellDailyId() {
        return productSellDailyId;
    }

    public void setProductSellDailyId(Long productSellDailyId) {
        this.productSellDailyId = productSellDailyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
