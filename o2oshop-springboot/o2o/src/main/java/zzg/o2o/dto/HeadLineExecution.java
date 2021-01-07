package zzg.o2o.dto;

import zzg.o2o.entity.HeadLine;
import zzg.o2o.enums.HeadLineStateEnum;

import java.util.List;

public class HeadLineExecution {
    private int state;
    private String stateInfo;
    private int count;
    private HeadLine headLine;
    private List<HeadLine> headLineList;

    public HeadLineExecution(){}

    public HeadLineExecution(HeadLineStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    public HeadLineExecution(HeadLineStateEnum stateEnum,HeadLine headLine){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.headLine=headLine;
    }

    public HeadLineExecution(HeadLineStateEnum stateEnum,List<HeadLine> headLineList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.headLineList=headLineList;
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

    public HeadLine getHeadLine() {
        return headLine;
    }

    public void setHeadLine(HeadLine headLine) {
        this.headLine = headLine;
    }

    public List<HeadLine> getHeadLineList() {
        return headLineList;
    }

    public void setHeadLineList(List<HeadLine> headLineList) {
        this.headLineList = headLineList;
    }
}
