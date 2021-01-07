package zzg.o2o.dto;

import zzg.o2o.entity.Area;
import zzg.o2o.enums.AreaStateEnum;

import java.util.List;

public class AreaExecution {
    private int state;
    private String stateInfo;
    private int count;
    private Area area;
    private List<Area> areaList;
    public AreaExecution(){}
    public AreaExecution(AreaStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }
    public AreaExecution(AreaStateEnum stateEnum,Area area){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.area=area;
    }
    public AreaExecution(AreaStateEnum stateEnum,List<Area> areaList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.areaList=areaList;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}
