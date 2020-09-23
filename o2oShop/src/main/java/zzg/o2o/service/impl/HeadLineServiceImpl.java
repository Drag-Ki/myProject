package zzg.o2o.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.entity.HeadLine;
import zzg.o2o.service.HeadLineService;

import java.util.List;
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        String key=HLLISTKEY;
        List<HeadLine> headLineList=null;
        ObjectMapper mapper=new ObjectMapper();
        if(headLineCondition!=null&&headLineCondition.getEnableStatus()!=null){
            key=key+"_"+headLineCondition.getEnableStatus();
        }
        return null;
    }
}
