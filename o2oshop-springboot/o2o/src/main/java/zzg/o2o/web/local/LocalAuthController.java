package zzg.o2o.web.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dao.PersonInfoDao;
import zzg.o2o.dto.LocalAuthExecution;
import zzg.o2o.entity.LocalAuth;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.enums.LocalAuthStateEnum;
import zzg.o2o.exceptions.LocalAuthOperationException;
import zzg.o2o.service.LocalAuthService;
import zzg.o2o.util.CodeUtil;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "local",method = {RequestMethod.GET,RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;
    @Autowired
    private PersonInfoDao personInfoDao;

    @RequestMapping(value = "/bindlocalauth",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> bindLocalAuth(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        String userName= HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        Integer status=HttpServletRequestUtil.getInt(request,"usertype");
        PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
        if(user==null||"".equals(user)){
            if(userName!=null&&password!=null){
                LocalAuth localAuth=localAuthService.getLocalAuthByUsernameAndPwd(userName,password);
                if(localAuth==null){
                    localAuth=new LocalAuth();
                    localAuth.setUsername(userName);
                    localAuth.setPassword(password);
                    user=new PersonInfo();
                    user.setName(userName);
                    user.setCreateTime(new Date());
                    user.setEnableStatus(1);
                    user.setUserType(status);
                    int effectedNum=personInfoDao.insertPersonInfo(user);
                    if(effectedNum<=0){
                        modelMap.put("errMsg","绑定用户信息错误");
                    }
                    localAuth.setPersonInfo(user);
                }
                LocalAuthExecution le=localAuthService.bindLocalAuth(localAuth);
                if(le.getState()== LocalAuthStateEnum.SUCCESS.getState()){
                    request.getSession().setAttribute("user",localAuth.getPersonInfo());
                    modelMap.put("success",true);
                }else if(le.getState()==LocalAuthStateEnum.ONLY_ONE_ACCOUNT.getState()){
                    request.getSession().setAttribute("user",localAuth.getPersonInfo());
                    modelMap.put("success",true);
                    modelMap.put("successMsg", "帐号已绑定，已自动登陆");
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",le.getStateInfo());
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名和密码均不能为空");
          }
        }else{
            modelMap.put("success",true);
            modelMap.put("success","帐号已绑定，已自动登陆");
        }
        return modelMap;
    }

    @RequestMapping(value = "/changelocalpwd",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> changeLocalPwd(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        String userName=HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        String newPassword=HttpServletRequestUtil.getString(request,"newPassword");
        PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
        if(userName!=null&&password!=null&&newPassword!=null&&user!=null&&user.getUserId()!=null&&!password.equals(newPassword)){
            try{
                LocalAuth localAuth=localAuthService.getLocalAuthByUserId(user.getUserId());
                if(localAuth==null||!localAuth.getUsername().equals(userName)){
                    modelMap.put("success",false);
                    modelMap.put("errMsg","输入的帐号非本次登录的帐号");
                    return modelMap;
                }
                LocalAuthExecution le=localAuthService.modifyLocalAuth(user.getUserId(),userName,password,newPassword);
                if(le.getState()==LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",le.getStateInfo());
                }
            }catch (LocalAuthOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入密码");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> logincheck(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        boolean needVerify=HttpServletRequestUtil.getBoolean(request,"needVerify");
        if(needVerify&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        String userName=HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        if(userName!=null&&password!=null){
            LocalAuth localAuth=localAuthService.getLocalAuthByUsernameAndPwd(userName,password);
            if(localAuth!=null){
                modelMap.put("success",true);
                request.getSession().setAttribute("user",localAuth.getPersonInfo());
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码均不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> logout(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap;
    }
}
