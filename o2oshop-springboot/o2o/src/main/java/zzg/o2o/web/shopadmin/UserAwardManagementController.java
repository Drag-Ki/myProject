package zzg.o2o.web.shopadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.ShopAuthMapExecution;
import zzg.o2o.dto.UserAwardMapExecution;
import zzg.o2o.entity.*;
import zzg.o2o.service.PersonInfoService;
import zzg.o2o.service.ShopAuthMapService;
import zzg.o2o.service.UserAwardMapService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(currentShop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            UserAwardMapExecution ue = userAwardMapService.listReceivedUserAwardMap(userAwardMap, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }


    private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = null;
        if (customerId != null && userAwardId != null && operator != null) {
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUsedStatus(1);
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
        }
        return userAwardMap;
    }

    private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId && shopAuthMap.getEnableStatus() == 1) {
                return true;
            }
        }
        return false;
    }
}

