package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerActivityInput;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerActivityOutput;
import com.amazon.vas.ServiceCapacityTracker.Modules.ServiceCapacityTrackerLambdaModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;

import java.util.Map;

public class RequestRouter implements RequestHandler<Map<String, String>, Object> {
    private final Gson gson = new Gson();
    private ServiceCapacityTrackerActivity serviceCapacityTrackerActivity;

    public String handleRequest(@NonNull final Map<String, String> input, Context context) {
        serviceCapacityTrackerActivity = getServiceCapacityTrackerActivityInstance();
        ServiceCapacityTrackerActivityInput serviceCapacityTrackerActivityInput
                = translateInputToServiceCapacityTrackerActivityInput(input);
        ServiceCapacityTrackerActivityOutput serviceCapacityTrackerActivityOutput
                = serviceCapacityTrackerActivity.handleRequest(serviceCapacityTrackerActivityInput);
        return translateServiceCapacityTrackerActivityOutputToJson(serviceCapacityTrackerActivityOutput);
    }

    public ServiceCapacityTrackerActivityInput translateInputToServiceCapacityTrackerActivityInput(
            @NonNull final Map<String, String> input) {
        return ServiceCapacityTrackerActivityInput.builder().skillType(input.get("skillType")).
                marketplaceId(input.get("marketplaceId")).storeName(input.get("storeName")).build();
    }

    public String translateServiceCapacityTrackerActivityOutputToJson(
            @NonNull final ServiceCapacityTrackerActivityOutput serviceCapacityTrackerActivityOutput) {
        return gson.toJson(serviceCapacityTrackerActivityOutput, ServiceCapacityTrackerActivityOutput.class);
    }

    public ServiceCapacityTrackerActivity getServiceCapacityTrackerActivityInstance() {
        Injector injector = Guice.createInjector(new ServiceCapacityTrackerLambdaModule());
        return injector.getInstance(ServiceCapacityTrackerActivity.class);
    }
}
