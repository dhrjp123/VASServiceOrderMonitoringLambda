package com.amazon.vas.serviceordermonitoringlambda.handler;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.module.ServiceOrderMonitoringLambdaModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;
import lombok.Value;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Value
public class App {

    Injector injector = Guice.createInjector(new ServiceOrderMonitoringLambdaModule());

    private final static Gson gson = new Gson();

    public String handleRequest(@NonNull final Map<Object, Object> lambdaInput, @NonNull final Context context) throws IOException, IntrospectionException {
        final LambdaLogger logger = context.getLogger();
        final String city = (String) lambdaInput.get("city");
        final String merchantID = (String) lambdaInput.get("merchantID");
        final String serviceCategory = (String) lambdaInput.get("serviceCategory");
        final List<String> groupingCriteria = (List<String>) lambdaInput.get("groupingCriteria");

        final GetJobMetricsActivity getJobMetricsActivity = injector.getInstance(GetJobMetricsActivity.class);
        final GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(city).merchantId(merchantID).serviceCategory(serviceCategory).groupingCriteria(groupingCriteria).build();
        final GetJobMetricsOutput getJobMetricsOutput = getJobMetricsActivity.enact(getJobMetricsInput);


        final String jobMetricsOutputJson = gson.toJson(getJobMetricsOutput);

        return jobMetricsOutputJson;
    }
}