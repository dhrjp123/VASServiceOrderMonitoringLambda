package com.amazon.vas.serviceordermonitoringlambda.router;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetServiceCapacityDetailsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetServiceCapacityDetailsOutput;
import com.amazon.vas.serviceordermonitoringlambda.modules.ServiceOrderMonitoringLambdaModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class RequestRouter implements RequestHandler<Map<Object, Object>, Object> {
    private static final Gson GSON = new Gson();

    public String handleRequest(@NonNull final Map<Object, Object> input, @NonNull final Context context) {
        String path = "/path1";  //TODO: Replace this with some expression that will give us the request path.
        if (path.equals("/path1")) {
            final GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity
                    = getGetServiceCapacityDetailsActivityInstance();
            final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput
                    = translateToGetServiceCapacityDetailsInput(input);
            final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput
                    = getServiceCapacityDetailsActivity.handleRequest(getServiceCapacityDetailsInput);
            return GSON.toJson(getServiceCapacityDetailsOutput, GetServiceCapacityDetailsOutput.class);
        } else {
            final GetJobMetricsActivity getJobMetricsActivity = getGetJobMetricsActivityInstance();
            final GetJobMetricsInput getJobMetricsInput = translateToGetJobMetricsInput(input);
            final GetJobMetricsOutput getJobMetricsOutput = getJobMetricsActivity.enact(getJobMetricsInput);
            return GSON.toJson(getJobMetricsOutput);
        }
    }

    private GetJobMetricsInput translateToGetJobMetricsInput(final Map<Object, Object> input) {
        return GetJobMetricsInput.builder().city((String) input.get("city"))
                .merchantId((String) input.get("merchantID"))
                .serviceCategory((String) input.get("serviceCategory"))
                .groupingCriteria((List<String>) input.get("groupingCriteria")).build();
    }

    private GetServiceCapacityDetailsInput translateToGetServiceCapacityDetailsInput(
            final Map<Object, Object> input) {
        return GetServiceCapacityDetailsInput.builder().skillType((String) input.get("skillType")).
                marketplaceId((String) input.get("marketplaceId")).storeName((String) input.get("storeName"))
                .numberOfDays(Integer.parseInt((String) input.get("numberOfDays"))).build();
    }

    private GetServiceCapacityDetailsActivity getGetServiceCapacityDetailsActivityInstance() {
        final Injector injector = Guice.createInjector(new ServiceOrderMonitoringLambdaModule());
        return injector.getInstance(GetServiceCapacityDetailsActivity.class);
    }

    private GetJobMetricsActivity getGetJobMetricsActivityInstance() {
        final Injector injector = Guice.createInjector(new ServiceOrderMonitoringLambdaModule());
        return injector.getInstance(GetJobMetricsActivity.class);
    }
}
