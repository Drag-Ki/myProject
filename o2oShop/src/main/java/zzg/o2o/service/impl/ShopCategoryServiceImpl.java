package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzg.o2o.dao.ShopCategoryDao;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.service.ShopCategoryService;

import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }
}
