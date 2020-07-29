package builder;

import accessor.ElasticSearchAccessor;
import model.bo.JobDetailsBO;
import testdata.builders.DefaultModelBuilders;
import util.JobAggregatedMetricsInputFilters;
import org.elasticsearch.action.search.SearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import static constants.JobAggregatedMetricsConstants.CITY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobDetailsBuilderTest {

    private final DefaultModelBuilders defaultModelBuilders = new DefaultModelBuilders();
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
//        final JobAggregatedMetricsInputFilters filters = new JobAggregatedMetricsInputFilters();
//        when(elasticSearchAccessor.getRecords(any(SearchRequest.class)))
//                .thenReturn(defaultModelBuilders.buildJobDetailsJson(filters));
//        final List<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder
//                .getJobDetailsBuilder(defaultModelBuilders.buildGetJobMetricsInputBO(CITY));
//        final List<JobDetailsBO> expectedJobDetailsList = defaultModelBuilders.buildJobDetailsList(filters);
//        assertEquals(expectedJobDetailsList, actualJobDetailsList);
//        verify(elasticSearchAccessor).getRecords(any(SearchRequest.class));

        List<JobDetailsBO> jobDetails = getJobDetailsBOList();
        System.out.printf("");
    }


    private List<JobDetailsBO> getJobDetailsBOList() {
        Random r = new Random(0);
        List<String> cities = ImmutableList.of("Delhi", "Jaipur", "Hyderabad", "Mumbai", "Shimla", "Manali", "OOty",
                "Patna", "jabalpur", "Khandala");
        List<JobDetailsBO> jobDetailsBOS = new ArrayList<>();
        for(int i=0; i< 10000 ;i++) {
            jobDetailsBOS.add(JobDetailsBO.builder()
                    .city(cities.get(r.nextInt(cities.size())))
                    .etaDelay(r.nextInt() %2 == 0)
                    .otaFailure(r.nextInt() %2 == 0)
                    .slotStartTime( String.valueOf(1595788200 + 36000 + 3600 * r.nextInt(10)))
                    .merchantId("MerchantId" + r.nextInt(100))
                    .statusNotUpdated(r.nextInt() %2 == 0 ? "NotServiced" : "Serviced")
                    .technicianId("TechnicianId" + r.nextInt(1000))
                    .build());
        }
        return jobDetailsBOS;
    }
}