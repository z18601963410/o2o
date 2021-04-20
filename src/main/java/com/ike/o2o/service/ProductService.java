package com.ike.o2o.service;

import com.ike.o2o.dao.ProductDao;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.dto.ProductExecution;
import com.ike.o2o.entity.Product;
import com.ike.o2o.exception.ProductOperationException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ProductService {
    ProductExecution addProduct(Product product, ImageHolder productThumbnailImageHolder,
                                List<ImageHolder> productImgListImageHolder) throws ProductOperationException;

    ProductExecution queryProductList(long shopId);

    ProductExecution queryProductById(long productId);

    ProductExecution modifyProduct(Product product, long shopId, ImageHolder productThumbnailImageHolder,
                                   List<ImageHolder> productImgListImageHolder) throws ProductOperationException;

    ProductExecution queryProductList(Product productCondition, int pageIndex, int pageSize);
}
