package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductSellDailyDao {
    List<ProductSellDaily> queryProductSellDailyList(
            @Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int insertProductSellDaily();

    int insertDefaultProductSellDaily();
}
