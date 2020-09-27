package zzg.o2o.service;

import zzg.o2o.dto.UserProductMapExecution;
import zzg.o2o.entity.UserProductMap;
import zzg.o2o.exceptions.UserProductMapOperationException;

public interface UserProductMapService {

    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
                                               Integer pageSize);


    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;
}

