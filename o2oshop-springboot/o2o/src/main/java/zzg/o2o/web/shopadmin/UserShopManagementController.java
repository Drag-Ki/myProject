package zzg.o2o.web.shopadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.UserShopMapExecution;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.entity.Shop;
import zzg.o2o.entity.UserShopMap;
import zzg.o2o.service.UserShopMapService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserShopManagementController {
    @Autowired
    private UserShopMapService userShopMapService;

    @RequestMapping(value = "/listusershopmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserShopMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request, "userName");
            if (userName != null) {
                PersonInfo customer = new PersonInfo();
                customer.setName(userName);
                userShopMapCondition.setUser(customer);
            }
            UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
            modelMap.put("userShopMapList", ue.getUserShopMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

}

