package zzg.o2o.service;

import zzg.o2o.dto.PersonInfoExecution;
import zzg.o2o.entity.PersonInfo;

public interface PersonInfoService {
    PersonInfo getPersonInfoById(Long userId);

    PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition,int pageIndex,int pageSize);

    PersonInfoExecution modifyPersonInfo(PersonInfo pi);
}
