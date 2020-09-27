package zzg.o2o.dto;

import zzg.o2o.entity.UserAwardMap;
import zzg.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

public class UserAwardMapExecution {
    private int state;

    private String stateInfo;

    private Integer count;

    private UserAwardMap userAwardMap;

    private List<UserAwardMap> userAwardMapList;

    public UserAwardMapExecution() {
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, UserAwardMap userAwardMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMapList = userAwardMapList;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }

}

