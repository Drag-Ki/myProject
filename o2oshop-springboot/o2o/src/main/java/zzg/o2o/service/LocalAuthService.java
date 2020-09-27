package zzg.o2o.service;

import zzg.o2o.dto.LocalAuthExecution;
import zzg.o2o.entity.LocalAuth;

public interface LocalAuthService {
    LocalAuth getLocalAuthByUsernameAndPwd(String username,String password);

    LocalAuth getLocalAuthByUserId(long userId);

    LocalAuthExecution bindLocalAuth(LocalAuth localAuth);

    LocalAuthExecution modifyLocalAuth(Long userId,String username,String password,String newPassword);
}
