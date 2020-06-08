package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import java.util.Map;

public class RequestRouter implements RequestHandler<Map<String,String>, Object>
{
    Gson gson=new Gson();
    public Object handleRequest(final Map<String,String> input, final Context context)
    {
        LambdaLogger logger=context.getLogger();
        ServiceCapacityTrackerActivity serviceCapacityTrackerActivity=
                new ServiceCapacityTrackerActivity(new ServiceCapacityTrackerComponent());
        ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO
                =translateInputToServiceCapacityTrackerRequestBO(input);
        logger.log("Request : "+gson.toJson(serviceCapacityTrackerRequestBO,ServiceCapacityTrackerRequestBO.class));
        ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO
                = serviceCapacityTrackerActivity.handleRequest(serviceCapacityTrackerRequestBO);
        String serviceCapacityTrackerResponse= translateServiceCapacityTrackerResponseBOToJson(serviceCapacityTrackerResponseBO);
        logger.log("Response : "+serviceCapacityTrackerResponse);
        return serviceCapacityTrackerResponse;
    }
    public ServiceCapacityTrackerRequestBO translateInputToServiceCapacityTrackerRequestBO(Map<String,String> input)
    {
        return ServiceCapacityTrackerRequestBO.builder().skillType(input.get("skillType")).
                marketplaceId(input.get("marketplaceId")).pinCode(input.get("pinCode")).
                storeId(input.get("storeId")).build();
    }
    public String translateServiceCapacityTrackerResponseBOToJson(ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO)
    {
        return gson.toJson(serviceCapacityTrackerResponseBO,ServiceCapacityTrackerResponseBO.class);
    }
}
