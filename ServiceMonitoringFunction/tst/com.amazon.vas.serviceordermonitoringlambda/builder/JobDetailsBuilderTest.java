package builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import org.elasticsearch.action.search.SearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.DefaultModelBuilders;
import util.JobAggregatedMetricsInputFilters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static constants.JobAggregatedMetricsConstants.CITY;

public class JobDetailsBuilderTest {

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
        final JobAggregatedMetricsInputFilters filters = new JobAggregatedMetricsInputFilters();
        when(elasticSearchAccessor.getRecords(any(SearchRequest.class))).thenReturn(defaultModelBuilders.buildJobDetailsJson(filters));
        final List<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder
                .getJobDetailsBuilder(defaultModelBuilders.buildGetJobMetricsInputBO(CITY));
        final List<JobDetailsBO> expectedJobDetailsList = defaultModelBuilders.buildJobDetailsList(filters);
        assertEquals(expectedJobDetailsList, actualJobDetailsList);
        verify(elasticSearchAccessor).getRecords(any(SearchRequest.class));
    }

    private final DefaultModelBuilders defaultModelBuilders = new DefaultModelBuilders();
}