package zzg.o2o.web.superadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import zzg.o2o.dto.ConstantForSuperAdmin;
import zzg.o2o.dto.ImageHolder;
import zzg.o2o.dto.ShopCategoryExecution;
import zzg.o2o.entity.ShopCategory;
import zzg.o2o.enums.ShopCategoryStateEnum;
import zzg.o2o.service.ShopCategoryService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class ShopCategoryController {
    @Autowired
    private ShopCategoryService shopCategoryService;

    @RequestMapping(value = "/listshopcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> listShopCategorys() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> list = new ArrayList<ShopCategory>();
        try {
            list = shopCategoryService.getShopCategoryList(null);
            list.addAll(shopCategoryService.getShopCategoryList(new ShopCategory()));
            modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
            modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }

    @RequestMapping(value = "/list1stlevelshopcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> list1stLevelShopCategorys() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> list = new ArrayList<ShopCategory>();
        try {
            list = shopCategoryService.getShopCategoryList(null);
            modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
            modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }

    @RequestMapping(value = "/list2ndlevelshopcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> list2ndLevelShopCategorys() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> list = new ArrayList<ShopCategory>();
        try {
            list = shopCategoryService.getShopCategoryList(new ShopCategory());
            modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
            modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }


    @RequestMapping(value = "/addshopcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addShopCategory(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        ShopCategory shopCategory = null;
        String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        try {
            shopCategory = mapper.readValue(shopCategoryStr, ShopCategory.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, "shopCategoryManagementAdd_shopCategoryImg");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopCategory != null && thumbnail != null) {
            try {
                shopCategory.setShopCategoryName((shopCategory.getShopCategoryName() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryName(), "UTF-8")));
                shopCategory.setShopCategoryDesc((shopCategory.getShopCategoryDesc() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryDesc(), "UTF-8")));
                ShopCategoryExecution ae = shopCategoryService.addShopCategory(shopCategory, thumbnail);
                if (ae.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
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
            modelMap.put("errMsg", "请输入店铺类别信息");
        }
        return modelMap;
    }


    @RequestMapping(value = "/modifyshopcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShopCategory(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        ShopCategory shopCategory = null;
        String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        try {
            shopCategory = mapper.readValue(shopCategoryStr, ShopCategory.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, "shopCategoryManagementEdit_shopCategoryImg");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopCategory != null && shopCategory.getShopCategoryId() != null) {
            try {
                shopCategory.setShopCategoryName((shopCategory.getShopCategoryName() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryName(), "UTF-8")));
                shopCategory.setShopCategoryDesc((shopCategory.getShopCategoryDesc() == null) ? null
                        : (URLDecoder.decode(shopCategory.getShopCategoryDesc(), "UTF-8")));
                ShopCategoryExecution ae = shopCategoryService.modifyShopCategory(shopCategory, thumbnail);
                if (ae.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
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
            modelMap.put("errMsg", "请输入店铺类别信息");
        }
        return modelMap;
    }


    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, String flowName)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile(flowName);
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        return thumbnail;
    }
}
