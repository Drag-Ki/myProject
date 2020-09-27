package zzg.o2o.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET, RequestMethod.POST })
public class SuperAdminController {

    @RequestMapping(value = "/areamanage", method = RequestMethod.GET)
    private String areamanagement() {
        return "superadmin/areamanage";
    }

    @RequestMapping(value = "/headlinemanage", method = RequestMethod.GET)
    private String headLinemanagement() {
        return "superadmin/headlinemanage";
    }

    @RequestMapping(value = "/shopcategorymanage", method = RequestMethod.GET)
    private String shopCategorymanage() {
        return "superadmin/shopcategorymanage";
    }

    @RequestMapping(value = "/shopmanage", method = RequestMethod.GET)
    private String shopmanage() {
        return "superadmin/shopmanage";
    }

    @RequestMapping(value = "/personinfomanage", method = RequestMethod.GET)
    private String personInfomanage() {
        return "superadmin/personinfomanage";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    private String main() {
        return "superadmin/main";
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    private String top() {
        return "superadmin/top";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login() {
        return "superadmin/login";
    }

}

