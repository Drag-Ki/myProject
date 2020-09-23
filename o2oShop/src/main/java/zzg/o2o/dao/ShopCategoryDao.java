package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.ShopCategory;

import java.util.List;
@Repository
public interface ShopCategoryDao {
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);
}
