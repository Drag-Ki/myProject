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
import zzg.o2o.dao.HeadLineDao;
import zzg.o2o.dto.HeadLineExecution;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.entity.HeadLine;
import zzg.o2o.enums.HeadLineStateEnum;
import zzg.o2o.exceptions.HeadLineOperationException;
import zzg.o2o.service.HeadLineService;
import zzg.o2o.util.ImageUtil;
import zzg.o2o.util.PathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    private static Logger logger= LoggerFactory.getLogger(HeadLineServiceImpl.class);
    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
        String key=HLLISTKEY;
        List<HeadLine> headLineList=null;
        ObjectMapper mapper=new ObjectMapper();
        if(headLineCondition!=null&&headLineCondition.getEnableStatus()!=null){
            key=key+"_"+headLineCondition.getEnableStatus();
        }
        if(!jedisKeys.exists(key)){
            headLineList=headLineDao.queryHeadLine(headLineCondition);
            String jsonString;
            try{
                jsonString=mapper.writeValueAsString(headLineList);
            }catch (JsonProcessingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else{
            String jsonString=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,HeadLine.class);
            try{
                headLineList=mapper.readValue(jsonString,javaType);
            }catch (JsonParseException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }catch(JsonMappingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }catch (IOException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }

    @Override
    @Transactional
    public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        if(headLine!=null){
            headLine.setCreateTime(new Date());
            headLine.setLastEditTime(new Date());
            if(thumbnail!=null){
                addThumbnail(headLine,thumbnail);
            }
            try{
                int effectedNum=headLineDao.insertHeadLine(headLine);
                if(effectedNum>0){
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS,headLine);
                }else{
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            }catch(Exception e){
                throw new HeadLineOperationException("添加区域信息失败："+e.toString());
            }
        }else {
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    private void deleteRedis4HeadLine() {
        String prefix=HLLISTKEY;
        Set<String> keySet=jedisKeys.keys(prefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }

    private void addThumbnail(HeadLine headLine, ImageHolder thumbnail) {
        String dest= PathUtil.getHeadLineImagePath();
        String thumbnailAddr= ImageUtil.generateNormalImg(thumbnail,dest);
        headLine.setLineImg(thumbnailAddr);
    }

    @Override
    @Transactional
    public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail) {
        if(headLine.getLineId()!=null&&headLine.getLineId()>0){
            headLine.setLastEditTime(new Date());
            if(thumbnail!=null){
                HeadLine tempHeadLine=headLineDao.queryHeadLineById(headLine.getLineId());
                if(tempHeadLine.getLineImg()!=null){
                    ImageUtil.deleteFileOrPath(tempHeadLine.getLineImg());
                }
                addThumbnail(headLine,thumbnail);
            }
            try{
                int effectedNum=headLineDao.updateHeadLine(headLine);
                if(effectedNum>0){
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS,headLine);
                }else{
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new HeadLineOperationException("更新头条信息失败："+e.toString());
            }
        }else{
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public HeadLineExecution removeHeadLine(long headlineId) {
        if(headlineId>0){
            try{
                HeadLine tempHeadLine=headLineDao.queryHeadLineById(headlineId);
                if(tempHeadLine.getLineImg()!=null){
                    ImageUtil.deleteFileOrPath(tempHeadLine.getLineImg());
                }
                int effectedNum=headLineDao.deleteHeadLine(headlineId);
                if(effectedNum>0){
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                }else{
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new HeadLineOperationException("删除头条信息失败："+e.toString());
            }
        }else{
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
        if(headLineIdList!=null&&headLineIdList.size()>0){
            try{
                List<HeadLine> headLineList=headLineDao.queryHeadLineByIds(headLineIdList);
                for(HeadLine headLine:headLineList){
                    if(headLine.getLineImg()!=null){
                        ImageUtil.deleteFileOrPath(headLine.getLineImg());
                    }
                }
                int effectedNum=headLineDao.batchDeleteHeadLine(headLineIdList);
                if(effectedNum>0){
                    deleteRedis4HeadLine();
                    return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
                }else{
                    return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new HeadLineOperationException("删除头条信息失败："+e.toString());
            }
        }else{
            return new HeadLineExecution(HeadLineStateEnum.EMPTY);
        }
    }
}
