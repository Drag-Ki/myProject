package zzg.o2o.service;

import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ShopCategoryExecution;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.util.ImageUtil;

import java.awt.*;
import java.util.List;

public interface ShopCategoryService {
    public static final String SCLISTKEY="shopcategorylist";
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
    ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);
    ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);
    ShopCategory getShopCategoryById(Long shopCategoryId);
}
