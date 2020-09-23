package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.ShopDao;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ShopExecution;
import zzg.o2o.entity.Shop;
import zzg.o2o.enums.ShopStateEnum;
import zzg.o2o.exceptions.ShopOperationException;
import zzg.o2o.service.ShopService;
import zzg.o2o.util.ImageUtil;
import zzg.o2o.util.PageCalculator;
import zzg.o2o.util.PathUtil;

import java.awt.*;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        if(shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectedNum=shopDao.insertShop(shop);
            if(effectedNum<=0)
                throw new ShopOperationException("店铺创建失败");
            else{
                if(thumbnail.getImage()!=null){
                    try{
                        addShopImg(shop,thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:"+e.getMessage());
                    }
                    effectedNum= shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (ShopOperationException e) {
            throw new ShopOperationException("addShop error:"+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
        if(shop==null||shop.getShopId()==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            try {
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);
                }
                shop.setLastEditTime(new Date());
                int effectNum = shopDao.updateShop(shop);
                if (effectNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error:"+e.getMessage());
            }
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex= PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList=shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        int count =shopDao.queryShopCount(shopCondition);
        ShopExecution se=new ShopExecution();
        if(shopList!=null){
            se.setShopList(shopList);
            se.setCount(count);
        }else{
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        String dest= PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr= ImageUtil.generateThumbnail(thumbnail,dest);
        shop.setShopImg(shopImgAddr);
    }
}
