package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.UserAwardMap;

import java.util.List;

@Repository
public interface UserAwardMapDao {
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    List<UserAwardMap> queryReceivedUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
                                                     @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

    UserAwardMap queryUserAwardMapById(long userAwardId);

    int insertUserAwardMap(UserAwardMap userAwardMap);

    int updateUserAwardMap(UserAwardMap userAwardMap);
}

