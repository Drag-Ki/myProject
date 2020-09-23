package zzg.o2o.service;

import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ShopExecution;
import zzg.o2o.entity.Shop;

import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);
    Shop getByShopId(long shopId);
    ShopExecution modifyShop(Shop shop,ImageHolder thumbnail);
    ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
}
