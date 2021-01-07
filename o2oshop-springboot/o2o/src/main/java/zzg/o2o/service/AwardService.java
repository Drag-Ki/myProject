package zzg.o2o.service;

import zzg.o2o.dto.AwardExecution;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.entity.Award;

public interface AwardService {

    AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);

    Award getAwardById(long awardId);

    AwardExecution addAward(Award award, ImageHolder thumbnail);

    AwardExecution modifyAward(Award award, ImageHolder thumbnail);

}

