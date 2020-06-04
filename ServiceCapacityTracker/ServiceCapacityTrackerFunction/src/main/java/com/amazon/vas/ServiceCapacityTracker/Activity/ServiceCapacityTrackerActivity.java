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
    public Object handleRequest(final Map<String,String> input)
    {
        validateInput(input);
        ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO =
                translateInputToServiceCapacityTrackerRequestBO(input);
        ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO =
                serviceCapacityTrackerComponent.trackCapacity(serviceCapacityTrackerRequestBO);
        return translateServiceCapacityTrackerResponseBOToJson(serviceCapacityTrackerResponseBO);
    }
    public ServiceCapacityTrackerRequestBO translateInputToServiceCapacityTrackerRequestBO(Map<String,String> input)
    {
        return new ServiceCapacityTrackerRequestBO(input.get("serviceType"),input.get("cityName"));
    }
    public String translateServiceCapacityTrackerResponseBOToJson(ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO)
    {
        Gson gson=new Gson();
        return gson.toJson(serviceCapacityTrackerResponseBO,ServiceCapacityTrackerResponseBO.class);
    }
    public void validateInput(Map<String,String> input)
    {
        if(input.get("serviceType")==null || input.get("serviceType").equals(""))
            throw new InvalidInputException("serviceType cannot be empty");
    }
}
