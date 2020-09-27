package zzg.o2o.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzg.o2o.dao.ProductSellDailyDao;
import zzg.o2o.entity.ProductSellDaily;
import zzg.o2o.service.ProductSellDailyService;

import java.util.Date;
import java.util.List;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
                                                       Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }

    @Override
    public void dailyCalculate() {
        log.info("Quartz Running!");
        productSellDailyDao.insertProductSellDaily();
        productSellDailyDao.insertDefaultProductSellDaily();
    }
}

