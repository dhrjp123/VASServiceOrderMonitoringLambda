package component;

import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.DataBuilder.*;

public class GetJobMetricsComponentTest {
    @Mock
    private JobDetailsBuilder jobDetailsBuilder;

    @InjectMocks
    private GetJobMetricsComponent getJobMetricsComponent;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJobMetricsComponent_withCityAndTimeGrouping(){
        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO_withCityAndTimeGrouping();
        when(jobDetailsBuilder.getJobDetailsBuilder(any(GetJobMetricsInputBO.class))).thenReturn(buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutputBO_withCityAndTimeGrouping();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput,actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(any(GetJobMetricsInputBO.class));
    }

    @Test
    public void testGetJobMetricsComponent_withMerchantIDAndTimeGrouping() {
        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO_withMerchantIDAndTimeGrouping();
        when(jobDetailsBuilder.getJobDetailsBuilder(any(GetJobMetricsInputBO.class))).thenReturn(buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutputBO_withMerchantIDAndTimeGrouping();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput,actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(any(GetJobMetricsInputBO.class));
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO_withCityAndTimeGrouping(){
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(null).merchantID(null).serviceCategory(null).groupingCriteria(new ArrayList<String>(Arrays.asList("city","time"))).build();
        return getJobMetricsInputBO;
    }
    private GetJobMetricsInputBO buildGetJobMetricsInputBO_withMerchantIDAndTimeGrouping(){
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(CITY_NAME).merchantID(null).serviceCategory(null).groupingCriteria(new ArrayList<String>(Arrays.asList("merchantID","time"))).build();
        return getJobMetricsInputBO;
    }
    private GetJobMetricsOutputBO buildExpectedGetJobMetricsOutputBO_withCityAndTimeGrouping(){
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(CITY_NAME,EPOCH_TIME));

        final Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
            put(CITY_NAME,new HashMap<String, String>(){{
                put("name",CITY_NAME);
                put("ID",CITY_NAME);
            }});
        }};

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private GetJobMetricsOutputBO buildExpectedGetJobMetricsOutputBO_withMerchantIDAndTimeGrouping(){
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(MERCHANT_ID,EPOCH_TIME));

        final Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
            put(MERCHANT_ID,new HashMap<String, String>(){{
                put("name",MERCHANT_ID);
                put("ID",MERCHANT_ID);
            }});
        }};

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private ArrayList<JobDetailsBO> buildJobDetailsList(){
        final Gson gson = new Gson();
        final List<String> jobDetailsJson = buildJobDetailsJson();
        final ArrayList<JobDetailsBO> expectedJobDetailsList = new ArrayList<>();
        for(String job:jobDetailsJson){
            expectedJobDetailsList.add(gson.fromJson(job,JobDetailsBO.class));
        }
        return expectedJobDetailsList;
    }
    private List<String> buildJobDetailsJson(){
        JSONObject json = new JSONObject();
        json.put("jdes", NOT_SERVICED);
        json.put("jbd",EPOCH_TIME);
        json.put("cty",CITY_NAME);
        json.put("tid",TECHNICIAN_ID);
        json.put("lse",true);
        json.put("isj" , true);
        json.put("mid",MERCHANT_ID);

        return new ArrayList<>(Arrays.asList(json.toJSONString()));
    }

}
