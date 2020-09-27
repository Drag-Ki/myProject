package zzg.o2o.web.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.service.AreaService;
import zzg.o2o.service.CacheService;
import zzg.o2o.service.HeadLineService;
import zzg.o2o.service.ShopCategoryService;
@Controller
public class CacheController {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    @RequestMapping(value="/clearcache4area",method = RequestMethod.GET)
    private String clearCache4Area(){
        cacheService.removeFromCache(areaService.AREALISTKEY);
        return "shop/operationsuccess";
    }

    @RequestMapping(value = "/clearcache4headline",method = RequestMethod.GET)
    private String clearCache4Headline(){
        cacheService.removeFromCache(headLineService.HLLISTKEY);
        return "shop/operationsuccess";
    }

    @RequestMapping(value = "/clearcache4shopcategory",method = RequestMethod.GET)
    private String clearCache4ShopCategory(){
        cacheService.removeFromCache(shopCategoryService.SCLISTKEY);
        return "shop/operationsuccess";
    }
}
