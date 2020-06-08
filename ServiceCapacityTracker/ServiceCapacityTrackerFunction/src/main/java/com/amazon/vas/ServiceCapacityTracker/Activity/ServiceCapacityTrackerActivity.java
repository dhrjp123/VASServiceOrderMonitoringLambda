package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import org.json.simple.JSONObject;
import java.util.Map;

public class ServiceCapacityTrackerActivity
{
    private final ServiceCapacityTrackerComponent serviceCapacityTrackerComponent;
    public ServiceCapacityTrackerActivity(@NonNull final ServiceCapacityTrackerComponent serviceCapacityTrackerComponent)
    {
        this.serviceCapacityTrackerComponent=serviceCapacityTrackerComponent;
    }
    public ServiceCapacityTrackerResponseBO handleRequest(@NonNull final ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO)
    {
        return serviceCapacityTrackerComponent.trackCapacity(serviceCapacityTrackerRequestBO);
    }
}
