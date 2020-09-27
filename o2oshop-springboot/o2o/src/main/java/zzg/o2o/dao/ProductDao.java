package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.Product;

import java.util.List;

@Repository
public interface ProductDao {
    List<Product> queryProductList(@Param("productCondition")Product productCondition,@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    int queryProductCount(@Param("productCondition")Product productCondition);

    Product queryProductById(long productId);

    int insertProduct(Product product);

    int updateProduct(Product product);

    int updateProductCategoryToNull(long productCategoryId);

    int deleteProduct(@Param("productId")long productId,@Param("shopId")long shopId);
}
