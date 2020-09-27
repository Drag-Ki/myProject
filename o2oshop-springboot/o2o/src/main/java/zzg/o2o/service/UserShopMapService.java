package zzg.o2o.service;

import zzg.o2o.dto.UserShopMapExecution;
import zzg.o2o.entity.UserShopMap;

public interface UserShopMapService {

    UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize);

    UserShopMap getUserShopMap(long userId, long shopId);

}
