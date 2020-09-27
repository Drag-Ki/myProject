package zzg.o2o.dto;

import zzg.o2o.entity.Award;
import zzg.o2o.enums.AwardStateEnum;

import java.util.List;

public class AwardExecution {
    private int state;

    private String stateInfo;

    private int count;

    private Award award;

    private List<Award> awardList;

    public AwardExecution() {
    }

    public AwardExecution(AwardStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public AwardExecution(AwardStateEnum stateEnum, Award award) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.award = award;
    }

    public AwardExecution(AwardStateEnum stateEnum,
                          List<Award> awardList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.awardList = awardList;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

}
