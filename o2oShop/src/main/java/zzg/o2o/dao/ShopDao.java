package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.Shop;

import java.util.List;

@Repository
public interface ShopDao {
    int insertShop(Shop shop);
    int updateShop(Shop shop);
    Shop queryByShopId(long shopId);
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
    int queryShopCount(@Param("shopCondition")Shop shopCondition);

}
