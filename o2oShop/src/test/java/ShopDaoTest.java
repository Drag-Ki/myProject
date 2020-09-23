import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zzg.o2o.dao.ShopDao;
import zzg.o2o.entity.Area;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.entity.Shop;
import zzg.o2o.entity.ShopCategory;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;
    @Test
    public void testInsertShop(){
        Shop shop=new Shop();
        PersonInfo owner=new PersonInfo();
        Area area=new Area();
        ShopCategory shopCategory=new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("test");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("test");
        int effectedNum=shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
    }
    @Test
    public void testUpdateShop(){
        Shop shop=new Shop();
        shop.setShopId(45L);
        PersonInfo owner=new PersonInfo();
        Area area=new Area();
        ShopCategory shopCategory=new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        int i=shopDao.updateShop(shop);
        assertEquals(1,i);
    }
}
