package zzg.o2o.service;

import zzg.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {
    public static final String HLLISTKEY="headlinelist";

    List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
