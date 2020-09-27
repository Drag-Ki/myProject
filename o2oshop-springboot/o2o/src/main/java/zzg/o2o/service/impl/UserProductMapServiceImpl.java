package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.UserProductMapDao;
import zzg.o2o.dao.UserShopMapDao;
import zzg.o2o.dto.UserProductMapExecution;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.entity.Shop;
import zzg.o2o.entity.UserProductMap;
import zzg.o2o.entity.UserShopMap;
import zzg.o2o.enums.UserProductMapStateEnum;
import zzg.o2o.exceptions.UserProductMapOperationException;
import zzg.o2o.service.UserProductMapService;
import zzg.o2o.util.PageCalculator;

import java.util.Date;
import java.util.List;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
                                                      Integer pageSize) {
        if (userProductCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition,
                    beginIndex, pageSize);
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap)
            throws UserProductMapOperationException {
        if (userProductMap != null && userProductMap.getUser().getUserId() != null
                && userProductMap.getShop().getShopId() != null) {
            userProductMap.setCreateTime(new Date());
            try {
                int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
                if (effectedNum <= 0) {
                    throw new UserProductMapOperationException("添加消费记录失败");
                }
                if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),
                            userProductMap.getShop().getShopId());
                    if (userShopMap != null && userShopMap.getUserShopId() != null) {
                        userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
                        effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (effectedNum <= 0) {
                            throw new UserProductMapOperationException("更新积分信息失败");
                        }
                    } else {
                        userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
                                userProductMap.getShop().getShopId(), userProductMap.getPoint());
                        effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectedNum <= 0) {
                            throw new UserProductMapOperationException("积分信息创建失败");
                        }
                    }
                }
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
            } catch (Exception e) {
                throw new UserProductMapOperationException("添加授权失败:" + e.toString());
            }
        } else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    /**
     * 封装顾客积分信息
     *
     * @param userId
     * @param shopId
     * @param point
     * @return
     */
    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {

        UserShopMap userShopMap = null;
        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(userId);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setUser(customer);
            userShopMap.setShop(shop);
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }

}
