package zzg.o2o.service;

import zzg.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {
    void dailyCalculate();

    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
                                                Date endTime);
}

