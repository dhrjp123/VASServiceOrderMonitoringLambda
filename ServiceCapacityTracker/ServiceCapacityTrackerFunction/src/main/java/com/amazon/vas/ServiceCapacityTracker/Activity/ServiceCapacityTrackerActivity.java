package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import java.util.Map;

public class ServiceCapacityTrackerActivity implements RequestHandler<Map<String,String>, Object>
{
    public Object handleRequest(final Map<String,String> input, final Context context)
    {
        try
        {
            LambdaLogger logger=context.getLogger();
            ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO =
                    translateInputToServiceCapacityTrackerRequestBO(input);
            ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO =
                    new ServiceCapacityTrackerComponent().trackCapacity(serviceCapacityTrackerRequestBO);
            String serviceCapacityTrackerResponse =
                    translateServiceCapacityTrackerResponseBOToJson(serviceCapacityTrackerResponseBO);
            logger.log(serviceCapacityTrackerResponse);
            return serviceCapacityTrackerResponse;
        }
        catch (Exception e)
        {
            return new JSONObject();
        }
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
}
