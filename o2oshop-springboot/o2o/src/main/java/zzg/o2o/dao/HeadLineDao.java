package zzg.o2o.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzg.o2o.entity.HeadLine;

import java.util.List;

@Repository
public interface HeadLineDao {
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

    HeadLine queryHeadLineById(long lineId);

    List<HeadLine> queryHeadLineByIds(List<Long> lineIdList);

    int insertHeadLine(HeadLine headLine);

    int updateHeadLine(HeadLine headLine);

    int deleteHeadLine(long headLineId);

    int batchDeleteHeadLine(List<Long> lineIdList);
}
