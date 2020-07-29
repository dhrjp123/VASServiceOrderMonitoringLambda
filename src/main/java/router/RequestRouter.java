package router;

import activity.GetJobMetricsActivity;
import activity.GetServiceCapacityDetailsActivity;
import model.activity.GetJobMetricsInput;
import model.activity.GetJobMetricsOutput;
import model.activity.GetServiceCapacityDetailsInput;
import model.activity.GetServiceCapacityDetailsOutput;
import modules.ServiceOrderMonitoringLambdaModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RequestRouter implements RequestHandler<Map<Object, Object>, Object> {
    private static final Gson GSON = new Gson();

    public Object handleRequest(@NonNull final Map<Object, Object> input, @NonNull final Context context) {
        System.out.println("DheerajDebug:"+GSON.toJson(input));
//        return GSON.toJson(GetJobMetricsOutput.builder().jobMetricsMap(ImmutableMap.of()).build());

        Map<Object, Object> map;
        try {
            map = new ObjectMapper().readValue((String)input.get("body"), new TypeReference<HashMap<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("DheerajDebug2:"+GSON.toJson(map));

        String path = (String)input.get("path");  //TODO: Replace this with some expression that will give us the
        // request path.
        if (path.contains("/test")) {
            final GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity
                    = getGetServiceCapacityDetailsActivityInstance();
            final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput
                    = translateToGetServiceCapacityDetailsInput(map);
            final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput
                    = getServiceCapacityDetailsActivity.handleRequest(getServiceCapacityDetailsInput);
            //return GSON.toJson(getServiceCapacityDetailsOutput, GetServiceCapacityDetailsOutput.class);

            JSONObject responseJson = new JSONObject();
            responseJson.put("statusCode", 200);
            responseJson.put("isBase64Encoded", false);
            responseJson.put("headers",
                    ImmutableMap.of("Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type",
                            "Access-Control-Allow-Methods", "OPTIONS,POST,GET,PUT"));
            responseJson.put("body", GSON.toJson(getServiceCapacityDetailsOutput));
            return responseJson;

        } else {
            final GetJobMetricsActivity getJobMetricsActivity = getGetJobMetricsActivityInstance();
            final GetJobMetricsInput getJobMetricsInput = translateToGetJobMetricsInput(map);
            final GetJobMetricsOutput getJobMetricsOutput = getJobMetricsActivity.enact(getJobMetricsInput);
            //return GSON.toJson(getJobMetricsOutput);

            JSONObject responseJson = new JSONObject();
            responseJson.put("statusCode", 200);
            responseJson.put("isBase64Encoded", false);
            responseJson.put("headers",
                    ImmutableMap.of("Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type",
                            "Access-Control-Allow-Methods", "OPTIONS,POST,GET,PUT"));
            responseJson.put("body", GSON.toJson(getJobMetricsOutput));
            return responseJson;
        }
    }

    private GetJobMetricsInput translateToGetJobMetricsInput(final Map<Object, Object> input) {
        return GetJobMetricsInput.builder().city((String) input.get("city"))
                .merchantId((String) input.get("merchantId"))
                .serviceCategory((String) input.get("serviceCategory"))
                .groupingCriteria((List<String>) input.get("groupingCriteria")).build();
    }

    private GetServiceCapacityDetailsInput translateToGetServiceCapacityDetailsInput(
            final Map<Object, Object> input) {
        return GetServiceCapacityDetailsInput.builder().skillType((String) input.get("skillType")).
                marketplaceId((String) input.get("marketplaceId")).storeName((String) input.get("storeName"))
                .numberOfDays((Integer)input.get("numberOfDays")).build();
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
