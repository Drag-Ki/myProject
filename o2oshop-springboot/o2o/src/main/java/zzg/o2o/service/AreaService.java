package zzg.o2o.service;

import org.springframework.stereotype.Service;
import zzg.o2o.dto.AreaExecution;
import zzg.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREALISTKEY="arealist";
    List<Area> getAreaList();

    AreaExecution addArea(Area area);

    AreaExecution modifyArea(Area area);
}
