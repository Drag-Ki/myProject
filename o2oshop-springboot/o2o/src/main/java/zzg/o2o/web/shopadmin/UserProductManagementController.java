package zzg.o2o.web.shopadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.EchartSeries;
import zzg.o2o.dto.EchartXAxis;
import zzg.o2o.dto.ShopAuthMapExecution;
import zzg.o2o.dto.UserProductMapExecution;
import zzg.o2o.entity.*;
import zzg.o2o.service.ProductSellDailyService;
import zzg.o2o.service.ProductService;
import zzg.o2o.service.ShopAuthMapService;
import zzg.o2o.service.UserProductMapService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
                    pageSize);
            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfobyShop(HttpServletRequest request) throws ParseException {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            List<ProductSellDaily> productSellDailyList = productSellDailyService
                    .listProductSellDaily(productSellDailyCondition, beginTime, endTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LinkedHashSet<String> legendData = new LinkedHashSet<String>();
            LinkedHashSet<String> xData = new LinkedHashSet<String>();
            List<EchartSeries> series = new ArrayList<EchartSeries>();
            List<Integer> totalList = new ArrayList<Integer>();
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName())
                        && !currentProductName.isEmpty()) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    totalList = new ArrayList<Integer>();
                    currentProductName = productSellDaily.getProduct().getProductName();
                    totalList.add(productSellDaily.getTotal());
                } else {
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                if (i == productSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);
            List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }



    private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo operator) {
        UserProductMap userProductMap = null;
        if (customerId != null && productId != null) {
            userProductMap = new UserProductMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShop(product.getShop());
            userProductMap.setUser(customer);
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
            userProductMap.setOperator(operator);
        }
        return userProductMap;
    }

    private boolean checkShopAuth(long userId, UserProductMap userProductMap) {
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 1, 1000);
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

}

