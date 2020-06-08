package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import java.util.Map;

public class ServiceCapacityTrackerActivity
{
    private ServiceCapacityTrackerComponent serviceCapacityTrackerComponent;
    public ServiceCapacityTrackerActivity(ServiceCapacityTrackerComponent serviceCapacityTrackerComponent)
    {
        this.serviceCapacityTrackerComponent=serviceCapacityTrackerComponent;
    }
    public ServiceCapacityTrackerResponseBO handleRequest(ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO)
    {
        return serviceCapacityTrackerComponent.trackCapacity(serviceCapacityTrackerRequestBO);
    }
}
