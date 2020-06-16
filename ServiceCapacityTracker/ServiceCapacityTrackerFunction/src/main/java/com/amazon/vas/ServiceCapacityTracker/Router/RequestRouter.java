package com.amazon.vas.ServiceCapacityTracker.Router;

import com.amazon.vas.ServiceCapacityTracker.Activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Modules.ServiceCapacityTrackerLambdaModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;

import java.util.Map;

public class RequestRouter implements RequestHandler<Map<String, String>, Object> {
    private static final Gson GSON = new Gson();
    private GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity;

    public String handleRequest(@NonNull final Map<String, String> input, @NonNull final Context context) {
        getServiceCapacityDetailsActivity = getGetServiceCapacityDetailsActivityInstance();
        final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput
                = translateInputToGetServiceCapacityDetailsInput(input);
        final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput
                = getServiceCapacityDetailsActivity.handleRequest(getServiceCapacityDetailsInput);
        return translateGetServiceCapacityDetailsOutputToJson(getServiceCapacityDetailsOutput);
    }

    private GetServiceCapacityDetailsInput translateInputToGetServiceCapacityDetailsInput(
            final Map<String, String> input) {
        return GetServiceCapacityDetailsInput.builder().skillType(input.get("skillType")).
                marketplaceId(input.get("marketplaceId")).storeName(input.get("storeName")).build();
    }

    private String translateGetServiceCapacityDetailsOutputToJson(
            final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput) {
        return GSON.toJson(getServiceCapacityDetailsOutput, GetServiceCapacityDetailsOutput.class);
    }

    private GetServiceCapacityDetailsActivity getGetServiceCapacityDetailsActivityInstance() {
        final Injector injector = Guice.createInjector(new ServiceCapacityTrackerLambdaModule());
        return injector.getInstance(GetServiceCapacityDetailsActivity.class);
    }
}
