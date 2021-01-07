package zzg.o2o.service;

import zzg.o2o.dto.HeadLineExecution;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {
    public static final String HLLISTKEY="headlinelist";

    List<HeadLine> getHeadLineList(HeadLine headLineCondition);
    HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail);
    HeadLineExecution modifyHeadLine(HeadLine headLine,ImageHolder thumbnail);
    HeadLineExecution removeHeadLine(long headlineId);
    HeadLineExecution removeHeadLineList(List<Long> headLineIdList);
}
