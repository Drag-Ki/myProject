package zzg.o2o.service;

import zzg.o2o.dto.UserAwardMapExecution;
import zzg.o2o.entity.UserAwardMap;
import zzg.o2o.exceptions.UserAwardMapOperationException;

public interface UserAwardMapService {

    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);


    UserAwardMapExecution listReceivedUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

    UserAwardMap getUserAwardMapById(long userAwardMapId);

    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

}

