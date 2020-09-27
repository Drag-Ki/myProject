package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.ProductCategory;

import java.util.List;

@Repository
public interface ProductCategoryDao {
    List<ProductCategory> queryProductCategoryList(long shopId);

    int  batchInsertProductCategory(List<ProductCategory> productCategoryList);

    int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
}
