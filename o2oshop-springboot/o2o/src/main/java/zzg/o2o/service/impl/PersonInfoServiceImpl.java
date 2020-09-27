package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzg.o2o.dao.PersonInfoDao;
import zzg.o2o.dto.PersonInfoExecution;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.enums.PersonInfoStateEnum;
import zzg.o2o.exceptions.PersonInfoOperationException;
import zzg.o2o.service.PersonInfoService;
import zzg.o2o.util.PageCalculator;

import java.util.List;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;
    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }

    @Override
    public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize) {
        int rowIndex= PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<PersonInfo> personInfoList=personInfoDao.queryPersonInfoList(personInfoCondition,rowIndex,pageSize);
        int count=personInfoDao.queryPersonInfoCount(personInfoCondition);
        PersonInfoExecution se=new PersonInfoExecution();
        if(personInfoList!=null){
            se.setPersonInfoList(personInfoList);
            se.setCount(count);
        }else {
            se.setState(PersonInfoStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
    @Transactional
    public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) {
        if(personInfo==null||personInfo.getUserId()==null){
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }else{
            try{
                int effectedNum=personInfoDao.updatePersonInfo(personInfo);
                if(effectedNum<=0){
                    return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
                }else{
                    personInfo=personInfoDao.queryPersonInfoById(personInfo.getUserId());
                    return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS,personInfo);
                }
            }catch (Exception e){
                throw new PersonInfoOperationException("updatePersonInfo error:"+e.getMessage());
            }
        }
    }
}
