package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.UserProductMap;

import java.util.List;

@Repository
public interface UserProductMapDao {
    List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition,
                                                 @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
    int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

    int insertUserProductMap(UserProductMap userProductMap);
}

