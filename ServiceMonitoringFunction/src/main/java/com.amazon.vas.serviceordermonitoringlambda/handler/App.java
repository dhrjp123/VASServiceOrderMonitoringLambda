package com.amazon.vas.serviceordermonitoringlambda.handler;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Value
@RequiredArgsConstructor
public class App {

    @NonNull
    private final GetJobMetricsActivity getJobMetricsActivity;
    private final static Gson gson = new Gson();

    public String handleRequest(@NonNull final Map<Object, Object> lambdaInput, @NonNull final Context context) throws IOException, IntrospectionException {
        final LambdaLogger logger = context.getLogger();
        final String city = (String) lambdaInput.get("city");
        final String merchantID = (String) lambdaInput.get("merchantID");
        final String serviceCategory = (String) lambdaInput.get("serviceCategory");
        final List<String> groupingCriteria = (List<String>) lambdaInput.get("groupingCriteria");


        final GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(city).merchantID(merchantID).serviceCategory(serviceCategory).groupingCriteria(groupingCriteria).build();
        final GetJobMetricsOutput getJobMetricsOutput = getJobMetricsActivity.enact(getJobMetricsInput);


        final String jobMetricsOutputJson = gson.toJson(getJobMetricsOutput);

        return jobMetricsOutputJson;
    }
}