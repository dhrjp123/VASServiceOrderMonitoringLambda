package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
public class ServiceCapacityTrackerResponseBO
{
    private ArrayList<EntityBO>entityList;
    public ServiceCapacityTrackerResponseBO(ArrayList<EntityBO>entityList)
    {
        this.entityList=new ArrayList<>();
        this.entityList.addAll(entityList);
    }
}
