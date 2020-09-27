package zzg.o2o.service;

import zzg.o2o.dto.ShopAuthMapExecution;
import zzg.o2o.entity.ShopAuthMap;
import zzg.o2o.exceptions.ShopAuthMapOperationException;

public interface ShopAuthMapService {
    ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

    ShopAuthMap getShopAuthMapById(Long shopAuthId);

    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

}

