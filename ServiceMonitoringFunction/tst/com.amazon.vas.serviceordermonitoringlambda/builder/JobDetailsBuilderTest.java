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
import util.TestDataBuilder;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobDetailsBuilderTest {

    private TestDataBuilder testDataBuilder;

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
        testDataBuilder = new TestDataBuilder();

        when(elasticSearchAccessor.getRecords(any(SearchRequest.class))).thenReturn(testDataBuilder.buildJobDetailsJson());
        final ArrayList<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder
                .getJobDetailsBuilder(testDataBuilder.buildGetJobMetricsInputBO_withCityAndSlotStartTime());
        final ArrayList<JobDetailsBO> expectedJobDetailsList = testDataBuilder.buildJobDetailsList();
        assertEquals(expectedJobDetailsList, actualJobDetailsList);
        verify(elasticSearchAccessor).getRecords(any(SearchRequest.class));
    }

}