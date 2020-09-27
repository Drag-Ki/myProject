package zzg.o2o.web.superadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.ConstantForSuperAdmin;
import zzg.o2o.dto.ShopExecution;
import zzg.o2o.entity.Shop;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.enums.ShopStateEnum;
import zzg.o2o.service.ShopCategoryService;
import zzg.o2o.service.ShopService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    @RequestMapping(value = "/listshops", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ShopExecution se = null;
        int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
        int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
        if (pageIndex > 0 && pageSize > 0) {
            Shop shopCondition = new Shop();
            int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
            if (enableStatus >= 0) {
                shopCondition.setEnableStatus(enableStatus);
            }
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            if (shopCategoryId > 0) {
                ShopCategory sc = new ShopCategory();
                sc.setShopCategoryId(shopCategoryId);
                shopCondition.setShopCategory(sc);
            }
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            if (shopName != null) {
                try {
                    shopCondition.setShopName(URLDecoder.decode(shopName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.toString());
                }
            }
            try {
                se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
            if (se.getShopList() != null) {
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, se.getShopList());
                modelMap.put(ConstantForSuperAdmin.TOTAL, se.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
                modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
                modelMap.put("success", true);
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "空的查询信息");
            return modelMap;
        }
    }

    @RequestMapping(value = "/searchshopbyid", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> searchShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop shop = null;
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > 0) {
            try {
                shop = shopService.getByShopId(shopId);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
            if (shop != null) {
                List<Shop> shopList = new ArrayList<Shop>();
                shopList.add(shop);
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, shopList);
                modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
                modelMap.put("success", true);
            } else {
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
                modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
                modelMap.put("success", true);
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "空的查询信息");
            return modelMap;
        }
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(String shopStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shop != null && shop.getShopId() != null) {
            try {
                shop.setShopName(
                        (shop.getShopName() == null) ? null : (URLDecoder.decode(shop.getShopName(), "UTF-8")));
                shop.setShopDesc(
                        (shop.getShopDesc() == null) ? null : (URLDecoder.decode(shop.getShopDesc(), "UTF-8")));
                shop.setShopAddr(
                        (shop.getShopAddr() == null) ? null : (URLDecoder.decode(shop.getShopAddr(), "UTF-8")));
                shop.setAdvice(((shop.getAdvice() == null) || shop.getAdvice().equals("\"\"")) ? null : (URLDecoder.decode(shop.getAdvice(), "UTF-8")));
                if (shop.getShopCategory() != null && shop.getShopCategory().getShopCategoryId() != null) {
                    ShopCategory sc = shopCategoryService
                            .getShopCategoryById(shop.getShopCategory().getShopCategoryId());
                    shop.setShopCategory(sc);
                }
                ShopExecution ae = shopService.modifyShop(shop, null);
                if (ae.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
        }
        return modelMap;
    }

}

