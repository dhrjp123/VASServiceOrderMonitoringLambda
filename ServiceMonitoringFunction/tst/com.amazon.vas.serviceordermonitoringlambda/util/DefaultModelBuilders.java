package util;

import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.util.*;

import static util.TestConstants.*;

public class DefaultModelBuilders {

    private final int numberOfTestJobs = 100;
    private final long baseTime = 1592800200;
    private final long timeBias = 3600;

    public GetJobMetricsInputBO buildGetJobMetricsInputBO(String filteringKey){
        GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(new ArrayList<>(Arrays.asList(filteringKey, SLOT_START_TIME))).build();
        return getJobMetricsInputBO;
    }
    public GetJobMetricsInput buildGetJobMetricsInput(String filteringKey){
        GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(new ArrayList<>(Arrays.asList(filteringKey, SLOT_START_TIME))).build();
        return getJobMetricsInput;
    }

    public ArrayList<JobDetailsBO> buildJobDetailsList() {
        final Gson gson = new Gson();
        final List<String> jobDetailsJson = buildJobDetailsJson();
        final ArrayList<JobDetailsBO> expectedJobDetailsList = new ArrayList<>();
        for (String job : jobDetailsJson) {
            expectedJobDetailsList.add(gson.fromJson(job, JobDetailsBO.class));
        }
        return expectedJobDetailsList;
    }

    public GetJobMetricsOutput buildGetJobMetricsOutput(GetJobMetricsOutputBO getJobMetricsOutputBO) {
        return GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData())
                .jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
    }


    public GetJobMetricsOutputBO buildGetJobMetricsOutputBO(String filteringKey) {

        final Map<String, Map<String, String>> metaData = new HashMap<>();
        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        for(int i = 0; i< numberOfTestJobs; i++){
            String filteringKeyValue = filteringKey+i;
            metaData.put(filteringKeyValue, new HashMap<String, String>() {{
                put("name", filteringKeyValue);
                put("ID", filteringKeyValue);
            }});
        }

        for(int i = 0; i< numberOfTestJobs; i++){
            final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder()
                    .totalJobsCount(1)
                    .statusNotUpdatedCount(1)
                    .otaFailureCount(1)
                    .etaDelayCount(1).build();
            final List<String> groupingCriteriaValues = new ArrayList<>(Arrays.asList(filteringKey+i,Long.toString(baseTime + timeBias * i)));
            jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);
        }

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    public List<String> buildJobDetailsJson() {
        final List<String> jobDetailsJson = new ArrayList<>();
        final JSONObject json = new JSONObject();
        for(int i = 0; i< numberOfTestJobs; i++){

                json.put("jdes", NOT_SERVICED);
                json.put("jbd", Long.toString(baseTime + timeBias * i));
                json.put("cty", CITY+i);
                json.put("mid", MERCHANT_ID+i);
                json.put("tid", TECHNICIAN_ID+i);
                json.put("lse", true);
                json.put("isj", true);

                jobDetailsJson.add(json.toJSONString());
                json.clear();
        }
        return jobDetailsJson;
    }

}
