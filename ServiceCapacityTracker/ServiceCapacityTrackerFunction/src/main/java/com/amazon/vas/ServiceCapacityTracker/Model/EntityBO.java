package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
public class EntityBO
{
    private String entityName;
    private String merchantId;
    private ArrayList<DailyCapacityBO>capacityList;
    public EntityBO(String entityName,String merchantId,ArrayList<DailyCapacityBO>capacityList)
    {
        this.entityName=entityName;
        this.merchantId=merchantId;
        this.capacityList=new ArrayList<>();
        this.capacityList.addAll(capacityList);
    }
}
