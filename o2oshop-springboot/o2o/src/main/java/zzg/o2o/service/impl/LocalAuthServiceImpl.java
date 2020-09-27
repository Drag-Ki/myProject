package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.LocalAuthDao;
import zzg.o2o.dto.LocalAuthExecution;
import zzg.o2o.entity.LocalAuth;
import zzg.o2o.enums.LocalAuthStateEnum;
import zzg.o2o.exceptions.LocalAuthOperationException;
import zzg.o2o.service.LocalAuthService;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;
    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(username,password);
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
        if(localAuth==null||localAuth.getPassword()==null||localAuth.getUsername()==null||localAuth.getPersonInfo()==null||localAuth.getPersonInfo().getUserId()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        LocalAuth tempAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if(tempAuth!=null){
            return new LocalAuthExecution((LocalAuthStateEnum.ONLY_ONE_ACCOUNT));
        }
        try{
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(localAuth.getPassword());
            int effectedNum=localAuthDao.insertLocalAuth(localAuth);
            if(effectedNum<=0){
                throw new LocalAuthOperationException("帐号绑定失败");
            }else{
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error+"+e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
        if(userId!=null&&username!=null&&password!=null&&newPassword!=null&&!password.equals(newPassword)){
            try{
                int effectedNum=localAuthDao.updateLocalAuth(userId,username,password,newPassword,new Date());
                if(effectedNum<=0){
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("更新密码失败");
            }
        }else{
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
