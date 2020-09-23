package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.AreaDao;
import zzg.o2o.entity.Area;
import zzg.o2o.service.AreaService;

import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    AreaDao areaDao;
    @Override
    @Transactional
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
