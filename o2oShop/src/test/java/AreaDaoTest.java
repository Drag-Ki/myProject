import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zzg.o2o.dao.AreaDao;
import zzg.o2o.entity.Area;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class AreaDaoTest {
    @Autowired
    AreaDao areaDao;
    @Test
    public void test(){
        List<Area> areaList=areaDao.queryArea();
        assertEquals(2,areaList.size());
    }
}
