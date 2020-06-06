package handler;

import activity.GetJobMetricsActivity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.Value;
import model.GetJobMetricsInput;
import model.GetJobMetricsOutput;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Value
public class App {

    private final GetJobMetricsActivity getJobMetricsActivity = new GetJobMetricsActivity();

    public String handleRequest(@NonNull Map<Object, Object> lambdaInput, @NonNull Context context) throws IOException, IntrospectionException {
        LambdaLogger logger = context.getLogger();
        final String city = (String) lambdaInput.get("city");
        final String sellerID = (String) lambdaInput.get("sellerID");
        final String serviceCategory = (String) lambdaInput.get("serviceCategory");
        final List<String> groupingCriteria = (List<String>) lambdaInput.get("groupingCriteria");


        final GetJobMetricsInput getJobMetricsInput = new GetJobMetricsInput(city,sellerID,serviceCategory,groupingCriteria);
        final GetJobMetricsOutput getJobMetricsOutput = getJobMetricsActivity.enact(getJobMetricsInput);

        Gson gson = new Gson();
        final String jsonJobMetricsOutput = gson.toJson(getJobMetricsOutput);

        return jsonJobMetricsOutput;
    }

}
