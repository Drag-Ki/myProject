package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.ProductDao;
import zzg.o2o.dao.ProductImgDao;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ProductExecution;
import zzg.o2o.entity.Product;
import zzg.o2o.entity.ProductImg;
import zzg.o2o.enums.ProductStateEnum;
import zzg.o2o.exceptions.ProductOperationException;
import zzg.o2o.service.ProductService;
import zzg.o2o.util.ImageUtil;
import zzg.o2o.util.PageCalculator;
import zzg.o2o.util.PathUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;
    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex= PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList=productDao.queryProductList(productCondition,rowIndex,pageSize);
        int count=productDao.queryProductCount(productCondition);
        ProductExecution pe=new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHoldList) {
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if(thumbnail!=null){
                addThumbnail(product,thumbnail);
            }
            try{
                int effectNum=productDao.insertProduct(product);
                if(effectNum<=0){
                    throw new ProductOperationException("创建商品失败");
                }
            }catch(Exception e){
                throw new ProductOperationException("创建商品失败"+e.toString());
            }
            if(productImgHoldList!=null&&productImgHoldList.size()>0){
                addProductImgList(product,productImgHoldList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    private void addProductImgList(Product product, List<ImageHolder> productImgHoldList) {
        String dest=PathUtil.getShopImagePath((product.getShop().getShopId()));
        List<ProductImg> productImgList=new ArrayList<>();
        for(ImageHolder productImgHolder:productImgHoldList){
            String imgAddr=ImageUtil.generateNormalImg(productImgHolder,dest);
        }
    }

    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }

    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) {
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            product.setLastEditTime(new Date());
            if(thumbnail!=null){
                Product tempProduct=productDao.queryProductById(product.getProductId());
                if(tempProduct.getImgAddr()!=null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            if(productImgHolderList!=null&&productImgHolderList.size()>0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgHolderList);
            }
            try{
                int effectedNum=productDao.updateProduct(product);
                if(effectedNum<=0){
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch(Exception e){
                throw new ProductOperationException("更新商品信息失败"+e.toString());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    private void deleteProductImgList(Long productId) {
        List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
        for(ProductImg productImg:productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }
}
