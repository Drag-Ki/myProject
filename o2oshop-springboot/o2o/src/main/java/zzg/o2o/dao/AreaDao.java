package zzg.o2o.dao;

import org.springframework.stereotype.Repository;
import zzg.o2o.entity.Area;

import java.util.List;

@Repository("areaDao")
public interface AreaDao {
    List<Area> queryArea();
    int insertArea(Area area);
    int updateArea(Area area);
    int deleteArea(long areaId);
    int batchDeleteArea(List<Long> areaIdList);
}
