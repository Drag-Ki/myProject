package zzg.o2o.service;

import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ProductExecution;
import zzg.o2o.entity.Product;

import java.util.List;

public interface ProductService {
    ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);

    Product getProductById(long productId);

    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder>productImgList);

    ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList);
}
