package activity;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
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


public class GetJobMetricsActivityTest {
    public static String TEST_CITY = "City-5";
    public static String TEST_MERCHANT_ID = "MerchantId-10";
    public static String TEST_SLOT_START_TIME = "1591758557";
    public static String CITY = "CITY";
    public static String SLOT_START_TIME = "SLOT_START_TIME";

    @Mock
    private GetJobMetricsComponent getJobMetricsComponent;

    @InjectMocks
    private GetJobMetricsActivity getJobMetricsActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getJobMetricsActivityTest() {

        when(getJobMetricsComponent.getJobMetrics(any(GetJobMetricsInputBO.class)))
                .thenReturn(buildGetJobMetricsOutputBO());
        final GetJobMetricsOutput actualGetJobMetricsOutput = getJobMetricsActivity.enact(buildGetJobMetricsInput());
        final GetJobMetricsOutput expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutput();
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(getJobMetricsComponent).getJobMetrics(any(GetJobMetricsInputBO.class));
    }

    private GetJobMetricsInput buildGetJobMetricsInput() {
        GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(TEST_CITY)
                .merchantId(TEST_MERCHANT_ID).serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_cityAndSlotStartTime()).build();
        return getJobMetricsInput;
    }

    private GetJobMetricsOutputBO buildGetJobMetricsOutputBO() {
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1)
                .statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_cityAndSlotStartTime();
        final Map<String, Map<String, String>> metaData = buildMetaData_city();

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private GetJobMetricsOutput buildExpectedGetJobMetricsOutput() {
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1)
                .statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = buildGroupingCriteriaValues_cityAndSlotStartTime();
        Map<String, Map<String, String>> metaData = buildMetaData_city();

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues, jobAggregatedMetricsBO);

        return GetJobMetricsOutput.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private List<String> buildGroupingCriteriaValues_cityAndSlotStartTime() {
        return new ArrayList<String>(Arrays.asList(TEST_CITY, TEST_SLOT_START_TIME));
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

    private List<String> buildGroupingCriteria_cityAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(CITY, SLOT_START_TIME));
    }
}
