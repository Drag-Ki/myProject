package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.Award;

import java.util.List;

@Repository
public interface AwardDao {
    List<Award> queryAwardList(@Param("awardCondition") Award awardCondition, @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    Award queryAwardByAwardId(long awardId);

    int insertAward(Award award);

    int updateAward(Award award);

    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
}
