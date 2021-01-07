package zzg.o2o.web.superadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.AreaExecution;
import zzg.o2o.entity.Area;
import zzg.o2o.enums.AreaStateEnum;
import zzg.o2o.service.AreaService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
    @Autowired
    AreaService areaService;
    @RequestMapping(value = "/listarea",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listArea(){
        Map<String,Object> modelMap=new HashMap<>();
        List<Area> list=new ArrayList<>();

        try{
            list=areaService.getAreaList();
            modelMap.put("rows",list);
            modelMap.put("total",list.size());
        }catch(Exception e){
            e.printStackTrace();
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        return modelMap;
    }

    @RequestMapping(value = "/addarea",method =RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addArea(String areaStr, HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        ObjectMapper mapper=new ObjectMapper();
        Area area=null;
        try{
            area=mapper.readValue(areaStr,Area.class);
            area.setAreaName((area.getAreaName()==null)?null: URLDecoder.decode(area.getAreaName(),"UTF-8"));

        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if(area!=null&&area.getAreaName()!=null){
            try{
                AreaExecution ae=areaService.addArea(area);
                if(ae.getState()== AreaStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",ae.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入区域信息");
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyarea",method = RequestMethod.POST)
    private Map<String,Object> modifyArea(String areaStr,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        ObjectMapper mapper=new ObjectMapper();
        Area area=null;
        try{
            area=mapper.readValue(areaStr,Area.class);
            area.setAreaName((area.getAreaName()==null)?null:URLDecoder.decode(area.getAreaName(),"UTF-8"));
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if(area!=null&&area.getAreaId()!=null){
            try{
                AreaExecution ae=areaService.modifyArea(area);
                if(ae.getState()==AreaStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",ae.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入区信息");
        }
        return modelMap;
    }
}
