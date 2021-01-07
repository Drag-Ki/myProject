package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.LocalAuth;

import java.util.Date;

@Repository
public interface LocalAuthDao {
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username,@Param("password")String password);

    LocalAuth queryLocalByUserId(@Param("userId")long userId);

    int insertLocalAuth(LocalAuth localAuth);

    int updateLocalAuth(@Param("userId")Long userId, @Param("username")String username, @Param("password")String password, @Param("newPassword")String newPassword, @Param("lastEditTime")Date lastEditTime);
}
