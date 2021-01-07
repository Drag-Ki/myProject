package zzg.o2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.cache.JedisUtil;
import zzg.o2o.dao.AreaDao;
import zzg.o2o.dto.AreaExecution;
import zzg.o2o.entity.Area;
import zzg.o2o.enums.AreaStateEnum;
import zzg.o2o.exceptions.AreaOperationException;
import zzg.o2o.service.AreaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger= LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key=AREALISTKEY;
        List<Area> areaList=null;
        ObjectMapper mapper=new ObjectMapper();
        if(!jedisKeys.exists(key)){
            areaList=areaDao.queryArea();
            String jsonString;
            try{
                jsonString=mapper.writeValueAsString(areaList);
            }catch (JsonProcessingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else{
            String jsonString=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try{
                areaList=mapper.readValue(jsonString,javaType);
            }catch (JsonParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new AreaOperationException(e.getMessage());
        } catch (JsonMappingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new AreaOperationException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new AreaOperationException(e.getMessage());
        }

    }

        return areaList;
    }

    @Override
    public AreaExecution addArea(Area area) {
        if(area.getAreaName()!=null&&!"".equals(area.getAreaName())){
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try{
                int effectedNum=areaDao.insertArea(area);
                if(effectedNum>0){
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS,area);
                }else{
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new AreaOperationException("添加区域信息失败"+e.toString());
            }
        }else{
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    private void deleteRedis4Area() {
        String key=AREALISTKEY;
        if(jedisKeys.exists(key)){
            jedisKeys.del(key);
        }
    }

    @Override
    public AreaExecution modifyArea(Area area) {
        if(area.getAreaId()!=null&&area.getAreaId()>0){
            area.setLastEditTime(new Date());
            try{
                int effectedNum=areaDao.updateArea(area);
                if(effectedNum>0){
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS,area);
                }else{
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            }catch(Exception e){
                throw new AreaOperationException("更新区域信息失败:"+e.toString());
            }
        }else{
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }
}
