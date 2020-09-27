package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.PersonInfo;

import java.util.List;

@Repository
public interface PersonInfoDao {
    List<PersonInfo> queryPersonInfoList(@Param("personInfoCondition")PersonInfo personInfoCondition,@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    int queryPersonInfoCount(@Param("personInfoCondition")PersonInfo personInfoCondition);

    PersonInfo queryPersonInfoById(long userId);

    PersonInfo queryPersonInfoByName(String userName);

    int insertPersonInfo(PersonInfo personInfo);

    int updatePersonInfo(PersonInfo personInfo);
}
