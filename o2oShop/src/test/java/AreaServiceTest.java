import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zzg.o2o.entity.Area;
import zzg.o2o.service.AreaService;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-service.xml","classpath:spring/spring-dao.xml"})
public class AreaServiceTest {
    @Autowired
    AreaService areaService;
    @Test
    public void test(){
        List<Area> areaList=areaService.getAreaList();
        assertEquals("西苑",areaList.get(0).getAreaName());
    }

}
