package zzg.o2o.dto;

import zzg.o2o.entity.PersonInfo;
import zzg.o2o.enums.PersonInfoStateEnum;

import java.util.List;

public class PersonInfoExecution {
    private int state;
    private String stateInfo;
    private int count;
    private PersonInfo personInfo;
    private List<PersonInfo> personInfoList;

    public PersonInfoExecution(){}

    public PersonInfoExecution(PersonInfoStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    public PersonInfoExecution(PersonInfoStateEnum stateEnum,PersonInfo personInfo){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.personInfo=personInfo;
    }

    public PersonInfoExecution(PersonInfoStateEnum stateEnum,List<PersonInfo> personInfoList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.personInfoList=personInfoList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public List<PersonInfo> getPersonInfoList() {
        return personInfoList;
    }

    public void setPersonInfoList(List<PersonInfo> personInfoList) {
        this.personInfoList = personInfoList;
    }
}
