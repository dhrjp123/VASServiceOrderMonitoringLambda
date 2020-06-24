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
import util.TestDataBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetJobMetricsComponentTest {

    private TestDataBuilder testDataBuilder;

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
        testDataBuilder = new TestDataBuilder();
        final GetJobMetricsInputBO getJobMetricsInputBO = testDataBuilder.buildGetJobMetricsInputBO_withCityAndSlotStartTime();
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO))
                .thenReturn(testDataBuilder.buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = testDataBuilder.buildGetJobMetricsOutputBO_withCityAndSlotStartTime();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);

        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }

    @Test
    public void testGetJobMetricsComponent_withMerchantIDAndTimeGrouping() {
        testDataBuilder = new TestDataBuilder();
        final GetJobMetricsInputBO getJobMetricsInputBO = testDataBuilder.buildGetJobMetricsInputBO_withMerchantIdAndSlotStartTime();
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO)).thenReturn(testDataBuilder.buildJobDetailsList());
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = testDataBuilder.buildGetJobMetricsOutputBO_withMerchantIdAndSlotStartTime();
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }

}
