package builder;

import accessor.ElasticSearchAccessor;
import com.google.gson.Gson;
import model.GetJobMetricsInput;
import model.GetJobDetailsInput;
import model.JobDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobDetailsBuilder {

    ElasticSearchAccessor accessor = new ElasticSearchAccessor();

    private JobDetails transformJsonToJavaObject(String jsonJobDetails){
        Gson gson = new Gson();
        JobDetails jobDetails = gson.fromJson(jsonJobDetails, JobDetails.class);
        return jobDetails;
    }
    private GetJobDetailsInput makeInputForAccessor(GetJobMetricsInput input){
        GetJobDetailsInput getJobDetailsInput = new GetJobDetailsInput(input.getCity(),input.getSellerID(),input.getServiceCategory());
        return getJobDetailsInput;
    }
    public ArrayList<JobDetails> GetJobDetailsBuilder(GetJobMetricsInput input) throws IOException {
        GetJobDetailsInput getJobDetailsInput = makeInputForAccessor(input);
        List<String> jsonJobDetailsList = accessor.getJobDetailsAccessor(getJobDetailsInput);
        ArrayList<JobDetails> jobDetailsList = new ArrayList<>();
        for(String job:jsonJobDetailsList){
            jobDetailsList.add(transformJsonToJavaObject(job));
        }
        return jobDetailsList;
    }

}
