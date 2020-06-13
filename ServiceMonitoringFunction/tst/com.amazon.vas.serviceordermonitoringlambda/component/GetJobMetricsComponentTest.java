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

public class GetJobMetricsComponentTest {
    private final static String TEST_CITY = "City-5";
    private final static String TEST_MERCHANT_ID = "MerchantId-10";
    private final static String TEST_TECHNICIAN_ID = "TechnicianId-3";
    private final static String TEST_SLOT_START_TIME = "1591758557";
    private final static String NOT_SERVICED = "NotServiced";
    private final static String CITY = "CITY";
    private final static String MERCHANT_ID = "MERCHANT_ID";
    private final static String SLOT_START_TIME = "SLOT_START_TIME";
    @Mock
    private JobDetailsBuilder jobDetailsBuilder;

    @InjectMocks
    private GetJobMetricsComponent getJobMetricsComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJobMetricsComponent_withCityAndTimeGrouping() {
        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO_withCityAndTimeGrouping();
        when(jobDetailsBuilder.getJobDetailsBuilder(any(GetJobMetricsInputBO.class))).thenReturn(buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutputBO_withCityAndTimeGrouping();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(any(GetJobMetricsInputBO.class));
    }

    @Test
    public void testGetJobMetricsComponent_withMerchantIDAndTimeGrouping() {
        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO_withMerchantIDAndTimeGrouping();
        when(jobDetailsBuilder.getJobDetailsBuilder(any(GetJobMetricsInputBO.class))).thenReturn(buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutputBO_withMerchantIDAndTimeGrouping();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(any(GetJobMetricsInputBO.class));
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO_withCityAndTimeGrouping() {
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(null).merchantId(null)
                .serviceCategory(null).groupingCriteria(buildGroupingCriteria_cityAndSlotStartTime())
                .build();
        return getJobMetricsInputBO;
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO_withMerchantIDAndTimeGrouping() {
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(TEST_CITY).merchantId(null)
                .serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_merchantIdAndSlotStartTime()).build();
        return getJobMetricsInputBO;
    }

    private GetJobMetricsOutputBO buildExpectedGetJobMetricsOutputBO_withCityAndTimeGrouping() {
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1)
                .statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_cityAndSlotStartTime();

        final Map<String, Map<String, String>> metaData = buildMetaData_city();

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private GetJobMetricsOutputBO buildExpectedGetJobMetricsOutputBO_withMerchantIDAndTimeGrouping() {
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1)
                .statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_merchantIdAndSlotStartTime();

        final Map<String, Map<String, String>> metaData = buildMetaData_mercahntId();

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private ArrayList<JobDetailsBO> buildJobDetailsList() {
        final Gson gson = new Gson();
        final List<String> jobDetailsJson = buildJobDetailsJson();
        final ArrayList<JobDetailsBO> expectedJobDetailsList = new ArrayList<>();
        for (String job : jobDetailsJson) {
            expectedJobDetailsList.add(gson.fromJson(job, JobDetailsBO.class));
        }
        return expectedJobDetailsList;
    }

    private List<String> buildJobDetailsJson() {
        final JSONObject json = new JSONObject();
        json.put("jdes", NOT_SERVICED);
        json.put("jbd", TEST_SLOT_START_TIME);
        json.put("cty", TEST_CITY);
        json.put("tid", TEST_TECHNICIAN_ID);
        json.put("lse", true);
        json.put("isj", true);
        json.put("mid", TEST_MERCHANT_ID);

        return new ArrayList<>(Arrays.asList(json.toJSONString()));
    }

    private List<String> buildGroupingCriteria_cityAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(CITY, SLOT_START_TIME));
    }
    private List<String> buildGroupingCriteria_merchantIdAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(MERCHANT_ID, SLOT_START_TIME));
    }
    private List<String> buildGroupingCriteriaValues_cityAndSlotStartTime() {
        return new ArrayList<String>(Arrays.asList(TEST_CITY, TEST_SLOT_START_TIME));
    }
    private List<String> buildGroupingCriteriaValues_merchantIdAndSlotStartTime() {
        return new ArrayList<String>(Arrays.asList(TEST_MERCHANT_ID, TEST_SLOT_START_TIME));
    }
    private Map<String, Map<String, String>> buildMetaData_city() {
        Map<String, Map<String, String>> metaData = new HashMap<String, Map<String, String>>() {{
            put(TEST_CITY, new HashMap<String, String>() {{
                put("name", TEST_CITY);
                put("ID", TEST_CITY);
            }});
        }};
        return metaData;
    }
    private Map<String, Map<String, String>> buildMetaData_mercahntId(){
        Map<String, Map<String, String>> metaData = new HashMap<String, Map<String, String>>() {{
            put(TEST_MERCHANT_ID, new HashMap<String, String>() {{
                put("name", TEST_MERCHANT_ID);
                put("ID", TEST_MERCHANT_ID);
            }});
        }};
        return metaData;
    }

}
