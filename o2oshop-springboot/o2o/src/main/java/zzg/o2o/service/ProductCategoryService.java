package zzg.o2o.service;

import zzg.o2o.dto.ProductCategoryExecution;
import zzg.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(long shopId);
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList);
    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId);
}
