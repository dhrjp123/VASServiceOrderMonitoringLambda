package component;

import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.DefaultModelBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestConstants.CITY;
import static util.TestConstants.MERCHANT_ID;

public class GetJobMetricsComponentTest {

    private DefaultModelBuilders defaultModelBuilders;

    @Mock
    private JobDetailsBuilder jobDetailsBuilder;

    @InjectMocks
    private GetJobMetricsComponent getJobMetricsComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJobMetricsComponent_withCityAndSlotStartTime() {
        defaultModelBuilders = new DefaultModelBuilders();
        final GetJobMetricsInputBO getJobMetricsInputBO = defaultModelBuilders.buildGetJobMetricsInputBO(CITY);
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO))
                .thenReturn(defaultModelBuilders.buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = defaultModelBuilders.buildGetJobMetricsOutputBO(CITY);
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);

        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }

    @Test
    public void testGetJobMetricsComponent_withMerchantIdAndSlotStartTime() {
        defaultModelBuilders = new DefaultModelBuilders();
        final GetJobMetricsInputBO getJobMetricsInputBO = defaultModelBuilders.buildGetJobMetricsInputBO(MERCHANT_ID);
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO)).thenReturn(defaultModelBuilders.buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = defaultModelBuilders.buildGetJobMetricsOutputBO(MERCHANT_ID);
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }

}
