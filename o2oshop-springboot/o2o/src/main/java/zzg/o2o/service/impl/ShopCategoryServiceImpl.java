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
import zzg.o2o.dao.ShopCategoryDao;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ShopCategoryExecution;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.enums.ShopCategoryStateEnum;
import zzg.o2o.exceptions.ShopCategoryOperationException;
import zzg.o2o.service.ShopCategoryService;
import zzg.o2o.util.ImageUtil;
import zzg.o2o.util.PathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger= LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key=SCLISTKEY;
        List<ShopCategory> shopCategoryList=null;
        ObjectMapper mapper=new ObjectMapper();
        if(shopCategoryCondition==null){
            key=key+"_allfirstlevel";
        }else if(shopCategoryCondition!=null&&shopCategoryCondition.getParent()!=null&&shopCategoryCondition.getParent().getShopCategoryId()!=null){
            key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
        }else if(shopCategoryCondition!=null){
            key=key+"_allsecondlevel";
        }
        if(!jedisKeys.exists(key)){
            shopCategoryList=shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try{
                jsonString=mapper.writeValueAsString(shopCategoryList);
            }catch(JsonProcessingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else{
            String jsonString=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);
            try{
                shopCategoryList=mapper.readValue(jsonString,javaType);
            }catch (JsonParseException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }catch(JsonMappingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }catch(IOException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }

    @Override
    @Transactional
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        if(shopCategory!=null){
            shopCategory.setCreateTime(new Date());
            shopCategory.setLastEditTime(new Date());
            if(thumbnail!=null){
                addThumbnail(shopCategory,thumbnail);
            }
            try{
                int effectedNum=shopCategoryDao.insertShopCategory(shopCategory);
                if(effectedNum>0){
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS,shopCategory);
                }else{
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new ShopCategoryOperationException("添加店铺类别信息失败："+e.toString());
            }
        }else{
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    private void deleteRedis4ShopCategory() {
        String prefix=SCLISTKEY;
        Set<String> keySet=jedisKeys.keys(prefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }

    private void addThumbnail(ShopCategory shopCategory, ImageHolder thumbnail) {
        String dest= PathUtil.getShopCategoryPath();
        String thumbnailAddr= ImageUtil.generateNormalImg(thumbnail,dest);
        shopCategory.setShopCategoryImg(thumbnailAddr);
    }

    @Override
    @Transactional
    public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        if(shopCategory.getShopCategoryId()!=null&&shopCategory.getShopCategoryId()>0){
            shopCategory.setLastEditTime(new Date());
            if(thumbnail!=null){
                ShopCategory tempShopCategory=shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
                if(tempShopCategory.getShopCategoryImg()!=null){
                    ImageUtil.deleteFileOrPath(tempShopCategory.getShopCategoryImg());
                }
                addThumbnail(shopCategory,thumbnail);
            }
            try{
                int effectedNum=shopCategoryDao.updateShopCategory(shopCategory);
                if(effectedNum>0){
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS,shopCategory);
                }else{
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            }catch (Exception e){
                throw new ShopCategoryOperationException("更新店铺类别信息失败："+e.toString());
            }
        }else{
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    @Override
    public ShopCategory getShopCategoryById(Long shopCategoryId) {
        return shopCategoryDao.queryShopCategoryById(shopCategoryId);
    }
}
