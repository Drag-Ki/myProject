package zzg.o2o.web.superadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.entity.LocalAuth;
import zzg.o2o.service.LocalAuthService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class LoginController {
    @Autowired
    private LocalAuthService localAuthService;

    @ResponseBody
    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    private Map<String,Object> loginCheck(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        String userName= HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        if(userName!=null&&password!=null){
            LocalAuth localAuth=localAuthService.getLocalAuthByUsernameAndPwd(userName,password);
            if(localAuth!=null){
                if(localAuth.getPersonInfo().getUserType()==3){
                    modelMap.put("success",true);
                    request.getSession().setAttribute("user",localAuth.getPersonInfo());
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg","非管理员没有权限访问");
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码均不能空");
        }
        return modelMap;
    }
}
