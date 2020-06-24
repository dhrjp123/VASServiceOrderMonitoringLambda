package activity;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
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


public class GetJobMetricsActivityTest {

    private TestDataBuilder testDataBuilder;

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
        testDataBuilder = new TestDataBuilder();
        final GetJobMetricsOutputBO getJobMetricsOutputBO = testDataBuilder.buildGetJobMetricsOutputBO_withCityAndSlotStartTime();
        System.out.println(getJobMetricsOutputBO);
        when(getJobMetricsComponent.getJobMetrics(testDataBuilder.buildGetJobMetricsInputBO_withCityAndSlotStartTime()))
                .thenReturn(getJobMetricsOutputBO);
        final GetJobMetricsOutput actualGetJobMetricsOutput = getJobMetricsActivity.enact(
                testDataBuilder.buildGetJobMetricsInput_withCityAndSlotStartTime());
        final GetJobMetricsOutput expectedGetJobMetricsOutput = testDataBuilder.buildGetJobMetricsOutput_withCityAndSlotStartTime(getJobMetricsOutputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(getJobMetricsComponent).getJobMetrics(testDataBuilder.buildGetJobMetricsInputBO_withCityAndSlotStartTime());
    }

}
