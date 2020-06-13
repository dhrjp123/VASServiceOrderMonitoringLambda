package builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobDetailsBuilderTest {
    private final static String TEST_CITY = "City-5";
    private final static String TEST_MERCHANT_ID = "MerchantId-10";
    private final static String TEST_TECHNICIAN_ID = "TechnicianId-3";
    private final static String TEST_SLOT_START_TIME = "1591758557";
    private final static String NOT_SERVICED = "NotServiced";
    private final static String CITY = "CITY";
    private final static String SLOT_START_TIME = "SLOT_START_TIME";

    @Mock
    private ElasticSearchAccessor elasticSearchAccessor;

    @InjectMocks
    private JobDetailsBuilder jobDetailsBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void jobDetailsBuilderTest() {
        when(elasticSearchAccessor.getRecords(any(SearchRequest.class))).thenReturn(buildJobDetailsJson());
        final ArrayList<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder
                .getJobDetailsBuilder(buildGetJobMetricsInputBO());
        final ArrayList<JobDetailsBO> expectedJobDetailsList = buildExpectedJobDetailsList();
        assertEquals(expectedJobDetailsList, actualJobDetailsList);
        verify(elasticSearchAccessor).getRecords(any(SearchRequest.class));
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO() {
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(TEST_CITY)
                .merchantId(TEST_MERCHANT_ID).serviceCategory(null)
                .groupingCriteria(buildGroupingCriteria_cityAndSlotStartTime()).build();
        return getJobMetricsInputBO;
    }
    private List<String> buildGroupingCriteria_cityAndSlotStartTime() {
        return new ArrayList<>(Arrays.asList(CITY, SLOT_START_TIME));
    }
    private ArrayList<JobDetailsBO> buildExpectedJobDetailsList() {
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
}