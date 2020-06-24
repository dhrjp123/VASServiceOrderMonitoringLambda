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

public class TestDataBuilder {

    private final List<String> randomCities = new ArrayList<>();
    private final List<String> randomSlotStartTime = new ArrayList<>();
    private final List<String> randomTechnicianId = new ArrayList<>();
    private final List<String> randomMerchantId = new ArrayList<>();
    private final List<Integer> randomNumberOfJobs = new ArrayList<>();
    private final int numberOfTestAggregatedJobs = 80;
    private final long baseTime = 1592800200;
    private final long timeGapBetweenConsecutiveTimeSlots = 3600;
    public TestDataBuilder(){
        generateRandomCity();
        generateRandomSlotStartTime();
        generateRandomMerchantId();
        generateRandomTechnicianId();
        generateRandomNumberOfJobs();

    }
    private void generateRandomNumberOfJobs(){
        Random random = new Random();
        for(int i=0;i<numberOfTestAggregatedJobs;i++)
            randomNumberOfJobs.add(1+random.nextInt(10));
    }
    private void generateRandomCity(){
        for(int i=0;i<numberOfTestAggregatedJobs;i++)
            randomCities.add("City-"+i%10);
    }
    private void generateRandomMerchantId(){
        Random random = new Random();
        for(int i=0;i<numberOfTestAggregatedJobs;i++)
            randomMerchantId.add("MerchantId-"+i%10);
    }
    private void generateRandomTechnicianId(){
        Random random = new Random();
        for(int i=0;i<numberOfTestAggregatedJobs;i++)
            randomTechnicianId.add("TechnicianId-"+i%10);
    }
    private void generateRandomSlotStartTime(){
        for(int i=0;i<numberOfTestAggregatedJobs;i++)
            randomSlotStartTime.add(Long.toString(baseTime + timeGapBetweenConsecutiveTimeSlots * i/10));
    }



    public GetJobMetricsInputBO buildGetJobMetricsInputBO_withCityAndSlotStartTime(){
        GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_cityAndSlotStartTime()).build();
        return getJobMetricsInputBO;
    }
    public GetJobMetricsInput buildGetJobMetricsInput_withCityAndSlotStartTime(){
        GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(null)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_cityAndSlotStartTime()).build();
        return getJobMetricsInput;
    }

    public GetJobMetricsInputBO buildGetJobMetricsInputBO_withMerchantIdAndSlotStartTime(){
        GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(TEST_CITY)
                .merchantId(null).serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_merchantIdAndSlotStartTime()).build();
        return getJobMetricsInputBO;
    }

    private List<String> buildGroupingCriteria_cityAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(CITY, SLOT_START_TIME));
    }
    private List<String> buildGroupingCriteria_merchantIdAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(MERCHANT_ID, SLOT_START_TIME));
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

    public GetJobMetricsOutput buildGetJobMetricsOutput_withCityAndSlotStartTime(GetJobMetricsOutputBO getJobMetricsOutputBO) {
        return GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData())
                .jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
    }


    public GetJobMetricsOutputBO buildGetJobMetricsOutputBO_withCityAndSlotStartTime() {

        final Map<String, Map<String, String>> metaData = new HashMap<>();
        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        for(int i=0;i<10;i++){
            String tempCity = "City-"+i;
            metaData.put(tempCity, new HashMap<String, String>() {{
                put("name", tempCity);
                put("ID", tempCity);
            }});
        }

        for(int i=0;i<numberOfTestAggregatedJobs;i++){
            int jobsCount = randomNumberOfJobs.get(i);
            final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder()
                    .totalJobsCount(jobsCount)
                    .statusNotUpdatedCount(jobsCount)
                    .otaFailureCount(jobsCount)
                    .etaDelayCount(jobsCount).build();
            final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_cityAndSlotStartTime(i);
            jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);
        }

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    public GetJobMetricsOutputBO buildGetJobMetricsOutputBO_withMerchantIdAndSlotStartTime() {

        final Map<String, Map<String, String>> metaData = new HashMap<>();
        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        for(int i=0;i<10;i++){
            String tempCity = "MerchantId-"+i;
            metaData.put(tempCity, new HashMap<String, String>() {{
                put("name", tempCity);
                put("ID", tempCity);
            }});
        }

        for(int i=0;i<numberOfTestAggregatedJobs;i++){
            int jobsCount = randomNumberOfJobs.get(i);
            final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder()
                    .totalJobsCount(jobsCount)
                    .statusNotUpdatedCount(jobsCount)
                    .otaFailureCount(jobsCount)
                    .etaDelayCount(jobsCount).build();
            final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_merchantIdAndSlotStartTime(i);
            jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);
        }

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }
    private List<String> buildGroupingCriteriaValues_cityAndSlotStartTime(int i){
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(randomCities.get(i),randomSlotStartTime.get(i)));
        return groupingCriteriaValues;
    }

    private List<String> buildGroupingCriteriaValues_merchantIdAndSlotStartTime(int i){
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(randomMerchantId.get(i),randomSlotStartTime.get(i)));
        return groupingCriteriaValues;
    }

    public List<String> buildJobDetailsJson() {
        final List<String> jobDetailsJson = new ArrayList<>();
        final JSONObject json = new JSONObject();
        for(int i=0;i<numberOfTestAggregatedJobs;i++){
            for(int j = 0; j< randomNumberOfJobs.get(i); j++){
                json.put("jdes", NOT_SERVICED);
                json.put("jbd", randomSlotStartTime.get(i));
                json.put("cty", randomCities.get(i));
                json.put("tid", randomTechnicianId.get(i));
                json.put("lse", true);
                json.put("isj", true);
                json.put("mid", randomMerchantId.get(i));

                jobDetailsJson.add(json.toJSONString());
                json.clear();
            }
        }
        return jobDetailsJson;
    }


}
