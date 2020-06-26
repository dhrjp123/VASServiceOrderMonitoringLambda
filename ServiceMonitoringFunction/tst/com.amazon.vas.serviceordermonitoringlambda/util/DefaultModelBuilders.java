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
import java.util.stream.Collectors;

import static util.JobAggregatedMetricsConstants.*;

public class DefaultModelBuilders {

    private final int numberOfTestJobs = 100;
    private final long baseTime = 1592800200;
    private final long timeBias = 3600;

    public GetJobMetricsInputBO buildGetJobMetricsInputBO(String groupingKey){
        GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(new ArrayList<>(Arrays.asList(groupingKey, SLOT_START_TIME))).build();
        return getJobMetricsInputBO;
    }
    public GetJobMetricsInput buildGetJobMetricsInput(String groupingKey){
        GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(new ArrayList<>(Arrays.asList(groupingKey, SLOT_START_TIME))).build();
        return getJobMetricsInput;
    }

    public List<JobDetailsBO> buildJobDetailsList(JobAggregatedMetricsInputFilters filters) {
        final Gson gson = new Gson();
        final List<String> jobDetailsJson = buildJobDetailsJson(filters);
        final List<JobDetailsBO> expectedJobDetailsList = jobDetailsJson.stream()
                .map(job -> gson.fromJson(job, JobDetailsBO.class)).collect(Collectors.toList());
        return expectedJobDetailsList;
    }

    public GetJobMetricsOutput buildGetJobMetricsOutput(GetJobMetricsOutputBO getJobMetricsOutputBO) {
        return GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData())
                .jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
    }


    public GetJobMetricsOutputBO buildGetJobMetricsOutputBO(String groupingKey) {

        final Map<String, Map<String, String>> metaData = new HashMap<>();
        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        for(int i = 0; i< numberOfTestJobs; i++){
            String groupingKeyValue = groupingKey+i;
            metaData.put(groupingKeyValue, new HashMap<String, String>() {{
                put("name", groupingKeyValue);
                put("ID", groupingKeyValue);
            }});
        }

        for(int i = 0; i< numberOfTestJobs; i++){
            final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder()
                    .totalJobsCount(1)
                    .statusNotUpdatedCount(1)
                    .otaFailureCount(1)
                    .etaDelayCount(1).build();
            final List<String> groupingCriteriaValues = new ArrayList<>(Arrays.asList(groupingKey+i,Long.toString(baseTime + timeBias * i)));
            jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);
        }

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    public List<String> buildJobDetailsJson(JobAggregatedMetricsInputFilters filters) {
        final List<String> jobDetailsJson = new ArrayList<>();
        final JSONObject json = new JSONObject();
        for(int i = 0; i< numberOfTestJobs; i++){
            final String city = filters.getCity() == null? CITY+i:filters.getCity() ;
            final String merchantId = filters.getMerchantId()  == null? MERCHANT_ID+i:filters.getMerchantId() ;
            final String technicianId = filters.getTechnicianId()  == null? TECHNICIAN_ID+i:filters.getTechnicianId();
            json.put("jdes", NOT_SERVICED);
            json.put("jbd", Long.toString(baseTime + timeBias * i));
            json.put("cty", city);
            json.put("mid", merchantId);
            json.put("tid", technicianId);
            json.put("lse", true);
            json.put("isj", true);

            jobDetailsJson.add(json.toJSONString());
            json.clear();
        }
        return jobDetailsJson;
    }

}
