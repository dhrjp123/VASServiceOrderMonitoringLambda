package handler;

import activity.GetJobMetricsActivity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import component.GetJobMetricsComponent;
import model.GetJobMetricsInput;
import model.GetJobMetricsOutput;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class App {

    public String handleRequest(Map<Object, Object> lambdaInput, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();

        final String city = (String) lambdaInput.get("city");
        final String sellerID = (String) lambdaInput.get("sellerID");
        final String serviceCategory = (String) lambdaInput.get("serviceCategory");
        final List<String> groupingCriteria = (List<String>) lambdaInput.get("groupingCriteria");


        GetJobMetricsInput input = new GetJobMetricsInput(city,sellerID,serviceCategory,groupingCriteria);
        GetJobMetricsActivity getJobMetricsActivity = new GetJobMetricsActivity();
        GetJobMetricsOutput out = getJobMetricsActivity.enact(input);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(out);

        return jsonStr;
    }

}
