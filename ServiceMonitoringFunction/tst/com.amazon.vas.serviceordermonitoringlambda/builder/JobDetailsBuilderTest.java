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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestConstants.CITY;

public class JobDetailsBuilderTest {

    private DefaultModelBuilders defaultModelBuilders;

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
        defaultModelBuilders = new DefaultModelBuilders();

        when(elasticSearchAccessor.getRecords(any(SearchRequest.class))).thenReturn(defaultModelBuilders.buildJobDetailsJson());
        final ArrayList<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder
                .getJobDetailsBuilder(defaultModelBuilders.buildGetJobMetricsInputBO(CITY));
        final ArrayList<JobDetailsBO> expectedJobDetailsList = defaultModelBuilders.buildJobDetailsList();
        assertEquals(expectedJobDetailsList, actualJobDetailsList);
        verify(elasticSearchAccessor).getRecords(any(SearchRequest.class));
    }

}