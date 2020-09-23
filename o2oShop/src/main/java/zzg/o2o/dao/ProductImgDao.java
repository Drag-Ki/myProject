package zzg.o2o.dao;

import org.springframework.stereotype.Repository;
import zzg.o2o.entity.ProductImg;

import java.util.List;

@Repository
public interface ProductImgDao {
    List<ProductImg> queryProductImgList(long productId);

    int batchInsertProductImg(List<ProductImg> productImgList);

    int deleteProductImgByProductId(long productId);
}
