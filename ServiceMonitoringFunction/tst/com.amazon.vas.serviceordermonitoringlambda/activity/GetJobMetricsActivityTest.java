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
import util.DefaultModelBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestConstants.CITY;


public class GetJobMetricsActivityTest {

    private DefaultModelBuilders defaultModelBuilders;

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
        defaultModelBuilders = new DefaultModelBuilders();
        final GetJobMetricsOutputBO getJobMetricsOutputBO = defaultModelBuilders.buildGetJobMetricsOutputBO(CITY);
        when(getJobMetricsComponent.getJobMetrics(defaultModelBuilders.buildGetJobMetricsInputBO(CITY)))
                .thenReturn(getJobMetricsOutputBO);
        final GetJobMetricsOutput actualGetJobMetricsOutput = getJobMetricsActivity.enact(
                defaultModelBuilders.buildGetJobMetricsInput(CITY));
        final GetJobMetricsOutput expectedGetJobMetricsOutput = defaultModelBuilders.buildGetJobMetricsOutput(getJobMetricsOutputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(getJobMetricsComponent).getJobMetrics(defaultModelBuilders.buildGetJobMetricsInputBO(CITY));
    }

}
