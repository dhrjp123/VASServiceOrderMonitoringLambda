package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class RequestRouter implements RequestHandler<Map<String,String>, Object>
{
    public Object handleRequest(final Map<String,String> input, final Context context)
    {
        ServiceCapacityTrackerActivity serviceCapacityTrackerActivity=
                new ServiceCapacityTrackerActivity(new ServiceCapacityTrackerComponent());
        return serviceCapacityTrackerActivity.handleRequest(input);
    }
}
