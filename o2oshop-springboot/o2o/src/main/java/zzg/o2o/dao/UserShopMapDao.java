package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.UserShopMap;

import java.util.List;

@Repository
public interface UserShopMapDao {
    List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition,
                                           @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

    UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);

    int insertUserShopMap(UserShopMap userShopMap);

    int updateUserShopMapPoint(UserShopMap userShopMap);

}

