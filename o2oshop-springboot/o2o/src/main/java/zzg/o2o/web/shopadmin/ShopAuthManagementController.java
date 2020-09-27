package zzg.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.ShopAuthMapExecution;
import zzg.o2o.entity.Shop;
import zzg.o2o.entity.ShopAuthMap;
import zzg.o2o.enums.ShopAuthMapStateEnum;
import zzg.o2o.service.PersonInfoService;
import zzg.o2o.service.ShopAuthMapService;
import zzg.o2o.util.CodeUtil;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
                    pageSize);
            modelMap.put("shopAuthMapList", se.getShopAuthMapList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopAuthId != null && shopAuthId > -1) {
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("shopAuthMap", shopAuthMap);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopAuthId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无法对店家本身权限做操作(已是店铺的最高权限)");
                    return modelMap;
                }
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入要修改的授权信息");
        }
        return modelMap;
    }

    private boolean checkPermission(Long shopAuthId) {
        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (grantedPerson.getTitleFlag() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Autowired
    private PersonInfoService personInfoService;




}

