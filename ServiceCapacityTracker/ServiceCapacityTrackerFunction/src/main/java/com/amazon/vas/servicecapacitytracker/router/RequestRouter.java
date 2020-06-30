package com.amazon.vas.servicecapacitytracker.router;

import com.amazon.vas.servicecapacitytracker.activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.servicecapacitytracker.model.activity.GetServiceCapacityDetailsInput;
import com.amazon.vas.servicecapacitytracker.model.activity.GetServiceCapacityDetailsOutput;
import com.amazon.vas.servicecapacitytracker.modules.ServiceCapacityTrackerLambdaModule;
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
                = translateToGetServiceCapacityDetailsInput(input);
        final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput
                = getServiceCapacityDetailsActivity.handleRequest(getServiceCapacityDetailsInput);
        return translateToJson(getServiceCapacityDetailsOutput);
    }

    private GetServiceCapacityDetailsInput translateToGetServiceCapacityDetailsInput(
            final Map<String, String> input) {
        return GetServiceCapacityDetailsInput.builder().skillType(input.get("skillType")).
                marketplaceId(input.get("marketplaceId")).storeName(input.get("storeName"))
                .numberOfDays(Integer.parseInt(input.get("numberOfDays"))).build();
    }

    private String translateToJson(
            final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput) {
        return GSON.toJson(getServiceCapacityDetailsOutput, GetServiceCapacityDetailsOutput.class);
    }

    private GetServiceCapacityDetailsActivity getGetServiceCapacityDetailsActivityInstance() {
        final Injector injector = Guice.createInjector(new ServiceCapacityTrackerLambdaModule());
        return injector.getInstance(GetServiceCapacityDetailsActivity.class);
    }
}
